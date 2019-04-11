package com.bdp.tx.aop.service.impl;

import com.bdp.tx.Constants;
import com.bdp.tx.aop.bean.TxTransactionInfo;
import com.bdp.tx.aop.bean.TxTransactionLocal;
import com.bdp.tx.aop.service.TransactionServer;
import com.bdp.tx.commons.exception.ServiceException;
import com.bdp.tx.commons.utils.KidUtils;
import com.bdp.tx.datasource.ILCNTransactionControl;
import com.bdp.tx.framework.task.TaskGroupManager;
import com.bdp.tx.framework.task.TxTask;
import com.bdp.tx.model.TxGroup;
import com.bdp.tx.netty.service.MQTxManagerService;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 分布式事务参与方的业务处理
 */
@Service(value = "txDoTransactionServer")
public class TxDoTransactionServerImpl implements TransactionServer {

	@Autowired
	private MQTxManagerService txManagerService;

	@Autowired
	private ILCNTransactionControl transactionControl;

	private Logger logger = LoggerFactory.getLogger(TxDoTransactionServerImpl.class);

	@Override
	public Object execute(final ProceedingJoinPoint point, final TxTransactionInfo info) throws Throwable {

		String kid = KidUtils.generateShortUuid();
		String txGroupId = info.getTxGroupId();
		logger.debug("--->begin running transaction,groupId:" + txGroupId);
		long t1 = System.currentTimeMillis();

		//很重要，表示分布式事务在当前模块已经存在
		boolean isHasIsGroup = transactionControl.hasGroup(txGroupId);

		TxTransactionLocal txTransactionLocal = new TxTransactionLocal();
		txTransactionLocal.setGroupId(txGroupId);
		txTransactionLocal.setHasStart(false);
		txTransactionLocal.setKid(kid);
		txTransactionLocal.setHasIsGroup(isHasIsGroup);
		txTransactionLocal.setMaxTimeOut(Constants.txServer.getCompensateMaxWaitTime());
		TxTransactionLocal.setCurrent(txTransactionLocal);

		try {

			// 处理业务
			Object res = point.proceed();

			// 写操作 处理
			if (!txTransactionLocal.isReadOnly()) {

				String methodStr = info.getInvocation().getMethodStr();

				TxGroup resTxGroup = txManagerService.addTransactionGroup(txGroupId, kid, isHasIsGroup, methodStr);

				// 已经进入过该模块的，不再执行此方法
				if (!isHasIsGroup) {
					String type = txTransactionLocal.getType();

					//TxTask是在LCNDBConnection构造函数里面实现的，所以如果当前业务方法加上@TxTransaction，
					//但是方法内并没有使用到数据库的连接，那么获取的TxTask就为空
					TxTask waitTask = TaskGroupManager.getInstance().getTask(kid, type);

					// 自循环线程等待，因为到这一步.LCNDBConnection.transaction()方法可能还没走到调线程阻塞方法处
					// 如果此处不自循环且上面加入事务组失败时，调用下面waitTask.signalTask()，则LCNDBConnection.transaction()
					// 线程阻塞以后就无法通过TxManager发送的命令被被唤醒，因为加入事务组失败，TxManager是不知道有这个参与方的，最后只能通过
					// 超时来结束LCNDBConnection.transaction()的阻塞，即便这种情况遇到的很少，但是很影响性能。所以这里要极短暂的循环一下.
					while (waitTask != null && !waitTask.isAwait()) {
						TimeUnit.MILLISECONDS.sleep(1);
					}

					// 添加事务组失败，直接唤醒等待的事务线程
					if (resTxGroup == null) {
						// 通知业务回滚事务
						if (waitTask != null) {
							// 修改事务组状态异常
							waitTask.setState(-1);
							// 这里会唤醒被阻塞的线程，该线程在LCNDBConnection中被阻塞，可以参考该类中的transaction()方法，
							// 该类中会根据waitTask最终的状态确定是提交还是回滚
							waitTask.signalTask();
							// 这一句很重要，事务发起方会接收到这个异常，然后通知TxMananger回滚事务
							throw new ServiceException("update TxGroup error, groupId:" + txGroupId);
						}
					} else {
						// 如果添加事务组成功，则不做任务事情，此时整个业务方法已经完成。在分布式事务发起方流程可以继续往下走了。
						// 但是这个方法中的事务并未提交或回滚，在LCNDBConnection被阻塞，等待TxManager通知是提交还是回滚。
						// 这里也就解释了为什么事务提交或回滚要与业务方法的主线程分开，就因为主线程必须返回到事务发起方。
					}
				}
			}
			return res;
		} catch (Throwable e) {
			throw e;
		} finally {
			TxTransactionLocal.setCurrent(null);
			long t2 = System.currentTimeMillis();
			logger.debug("<---end running transaction,groupId:" + txGroupId + ",execute time:" + (t2 - t1));

		}
	}

}
