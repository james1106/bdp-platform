package com.bdp.tx.aop.service.impl;

import com.bdp.tx.Constants;
import com.bdp.tx.aop.bean.TxCompensateLocal;
import com.bdp.tx.aop.bean.TxTransactionInfo;
import com.bdp.tx.aop.bean.TxTransactionLocal;
import com.bdp.tx.aop.service.TransactionServer;
import com.bdp.tx.commons.utils.KidUtils;
import com.bdp.tx.framework.task.TaskGroupManager;
import com.bdp.tx.framework.task.TaskState;
import com.bdp.tx.framework.task.TxTask;
import com.bdp.tx.netty.service.MQTxManagerService;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分布式事务发起时的业务处理
 */
@Service(value = "txStartTransactionServer")
public class TxStartTransactionServerImpl implements TransactionServer {

	private Logger logger = LoggerFactory.getLogger(TxStartTransactionServerImpl.class);

	@Autowired
	protected MQTxManagerService txManagerService;

	@Override
	public Object execute(ProceedingJoinPoint point, final TxTransactionInfo info) throws Throwable {
		// 分布式事务开始执行
		logger.debug("--->begin start transaction");

		final long start = System.currentTimeMillis();

		int state = 0;

		// 生成事务组，注意：这里先判断了当前是否是补偿事务，如果是，直接使用补偿事务的ID，这里与补偿事务实现机制有关
		final String groupId = TxCompensateLocal.current() == null ? KidUtils.generateShortUuid()
				: TxCompensateLocal.current().getGroupId();

		// 创建事务组,表示当前为分布式事务的发起方
		txManagerService.createTransactionGroup(groupId);

		TxTransactionLocal txTransactionLocal = new TxTransactionLocal();
		txTransactionLocal.setGroupId(groupId);
		// 标记当前为分布式事务的发起方
		txTransactionLocal.setHasStart(true);
		txTransactionLocal.setMaxTimeOut(Constants.txServer.getCompensateMaxWaitTime());
		TxTransactionLocal.setCurrent(txTransactionLocal);

		try {
			// 执行业务逻辑,注意：如果此时有spring等框架对事务进行管理，根据sprign事务配置，
			// 这个方法执行完成后,框架对事务的提交或或回滚已经执行，即已经调用过了Connection的commit或rollback方法。
			// 因为Connection已经补代理，所以Spring对事务的提交或回滚都不会起作用
			Object obj = point.proceed();
			// 说明此时业务正常进行，执行完成后的状态，这个状态会提交给tm,tm分根据这个状态通过事务参与方提交或者回滚
			state = 1;
			return obj;
		} catch (Throwable e) {
			// 根据异常类型及@TxTransaction配置来决定是否回滚，返回值会在finally中使用
			state = rollbackException(info, e);
			throw e;
		} finally {
			final String type = txTransactionLocal.getType();
			// 向tm发送关闭事务组的通知，将执行完成状态发送过去，根据这个状态tm确定是进行回滚还是提交操作，
			// 并获取tm返回的其它事务参与方事务提交状态的结果
			int remoteState = txManagerService.closeTransactionGroup(groupId, state);
			int lastState = remoteState == -1 ? 0 : state;
			int executeConnectionError = 0;
			// 控制本地事务的数据提交
			final TxTask waitTask = TaskGroupManager.getInstance().getTask(groupId, type);
			if (waitTask != null) {
				waitTask.setState(lastState);
				// 这里会唤醒被阻塞的线程，该线程在LCNStartConnection中被阻塞，可以参考该类中的transaction()方法，
				// 该类中会根据waitTask最终的状态确定是提交还是回滚.并且会再次根据提交结果设置waitTask.setState()
				waitTask.signalTask();
				// 通过循环进行线程锁定，LCNStartConnection中提交完成后会将remove设置为true,线程继续
				while (!waitTask.isRemove()) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// 如果本地提交失败。TaskState.connectionError.getCode()是在LCNStartConnection提交失败后设置的
				if (waitTask.getState() == TaskState.connectionError.getCode()) {
					// 本地执行失败.
					executeConnectionError = 1;
					lastState = 0;
				}
			}
			final TxCompensateLocal compensateLocal = TxCompensateLocal.current();
			if (compensateLocal == null) {
				long end = System.currentTimeMillis();
				long time = end - start;
				// 本地提交失败且远程其它事务提交成功 或者 本地提交成功并且远程其它事务参与方提交失败 都进行事务补偿
				if ((executeConnectionError == 1 && remoteState == 1) || (lastState == 1 && remoteState == 0)) {
					// 记录补偿日志，并将本地提交成功与否的状态发给TxManager,下次补偿时会用到
					txManagerService.sendCompensateMsg(groupId, time, info, executeConnectionError);
				}
			} else {// 如果当前是补偿事务
				if (remoteState == 1) {
					lastState = 1;
				} else {
					lastState = 0;
				}
			}
			TxTransactionLocal.setCurrent(null);
			logger.debug("<---end start transaction");
			logger.debug("start transaction over, res -> groupId:" + groupId + ", now state:"
					+ (lastState == 1 ? "commit" : "rollback"));
		}
	}

	/**
	 * 判断是否回滚，这里会检测返回异常是否属于指定的不回滚异常，见代码
	 * 
	 * @param info
	 * @param throwable
	 * @return
	 */
	private int rollbackException(TxTransactionInfo info, Throwable throwable) {
		// spring 事务机制默认回滚异常，即如果当前异常为RuntimeException或者Error，默认都回滚异常.
		// 否则按@TxTransaction的配置来决定是否回滚事务
		if (RuntimeException.class.isAssignableFrom(throwable.getClass())) {
			return 0;
		}
		if (Error.class.isAssignableFrom(throwable.getClass())) {
			return 0;
		}
		// 回滚异常检测.
		for (Class<? extends Throwable> rollbackFor : info.getTransaction().rollbackFor()) {
			// 存在关系
			if (rollbackFor.isAssignableFrom(throwable.getClass())) {
				return 0;
			}
		}
		// 不回滚异常检测.
		for (Class<? extends Throwable> rollbackFor : info.getTransaction().noRollbackFor()) {
			// 存在关系
			if (rollbackFor.isAssignableFrom(throwable.getClass())) {
				return 1;
			}
		}
		return 1;
	}
}
