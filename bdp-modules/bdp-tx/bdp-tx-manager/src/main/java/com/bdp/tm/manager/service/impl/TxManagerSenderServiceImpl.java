package com.bdp.tm.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bdp.tm.Constants;
import com.bdp.tm.compensate.service.CompensateService;
import com.bdp.tm.config.ConfigReader;
import com.bdp.tm.framework.utils.SocketManager;
import com.bdp.tm.framework.utils.SocketUtils;
import com.bdp.tm.manager.service.TxManagerSenderService;
import com.bdp.tm.manager.service.TxManagerService;
import com.bdp.tm.model.ChannelSender;
import com.bdp.tm.netty.model.TxGroup;
import com.bdp.tm.netty.model.TxInfo;
import com.bdp.tm.redis.service.RedisServerService;
import com.bdp.tx.commons.utils.KidUtils;
import com.bdp.tx.commons.utils.task.ConditionUtils;
import com.bdp.tx.commons.utils.task.IBack;
import com.bdp.tx.commons.utils.task.Task;
import com.bdp.tx.commons.utils.thread.CountDownLatchHelper;
import com.bdp.tx.commons.utils.thread.IExecute;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Service
public class TxManagerSenderServiceImpl implements TxManagerSenderService {

	private Logger logger = LoggerFactory.getLogger(TxManagerSenderServiceImpl.class);

	private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(100);

	private Executor threadPool = Executors.newFixedThreadPool(100);

	@Autowired
	private TxManagerService txManagerService;

	@Autowired
	private RedisServerService redisServerService;

	@Autowired
	private ConfigReader configReader;

	@Autowired
	private CompensateService compensateService;

	@Override
	public int confirm(TxGroup txGroup) {
		// 绑定管道对象，检查网络
		setChannel(txGroup.getList());

		// 事务不满足直接回滚事务
		if (txGroup.getState() == 0) {
			transaction(txGroup, 0);
			return 0;
		}

		// 如果是回滚的，就强制回滚
		if (txGroup.getRollback() == 1) {
			transaction(txGroup, 0);
			return -1;
		}

		boolean hasOk = transaction(txGroup, 1);
		txManagerService.dealTxGroup(txGroup, hasOk);
		return hasOk ? 1 : 0;
	}

	/**
	 * 匹配管道
	 *
	 * @param list
	 */
	private void setChannel(List<TxInfo> list) {
		for (TxInfo info : list) {
			if (Constants.address.equals(info.getAddress())) {
				Channel channel = SocketManager.getInstance().getChannelByModelName(info.getChannelAddress());
				if (channel != null && channel.isActive()) {
					ChannelSender sender = new ChannelSender();
					sender.setChannel(channel);
					info.setChannel(sender);
				}
			} else {
				ChannelSender sender = new ChannelSender();
				sender.setAddress(info.getAddress());
				sender.setModelName(info.getChannelAddress());

				info.setChannel(sender);
			}
		}
	}

	/**
	 * 事务提交或回滚事务组，TM最核心方法
	 * 
	 * @param txGroup
	 *            事务组
	 * @param checkSate
	 *            提交 1 回滚0
	 * @return
	 */
	private boolean transaction(final TxGroup txGroup, final int checkSate) {

		if (checkSate == 1) {// 提交事务
			// 补偿请求，加载历史数据，根据之前提交的结果还确定本次究竟是提交还是回滚
			// 之前正常提交，则本次回滚，反之则本次提交。事务补偿的原理
			if (txGroup.getIsCompensate() == 1) {
				compensateService.reloadCompensate(txGroup);
			}

			CountDownLatchHelper<Boolean> countDownLatchHelper = new CountDownLatchHelper<Boolean>();
			for (final TxInfo txInfo : txGroup.getList()) {
				if (txInfo.getIsGroup() == 0) {
					countDownLatchHelper.addExecute(new IExecute<Boolean>() {
						@Override
						public Boolean execute() {
							if (txInfo.getChannel() == null) {
								return false;
							}

							final JSONObject jsonObject = new JSONObject();

							// 会调到客户端的ActionTServiceImpl实现类
							jsonObject.put("a", "t");

							if (txGroup.getIsCompensate() == 1) { // 补偿请求
								jsonObject.put("c", txInfo.getIsCommit());
							} else { // 正常业务
								jsonObject.put("c", checkSate);
							}

							jsonObject.put("t", txInfo.getKid());
							final String key = KidUtils.generateShortUuid();
							jsonObject.put("k", key);

							Task task = ConditionUtils.getInstance().createTask(key);

							// 在这个方法中设置超时，即在指定时间内当前被阻塞的线程未被唤醒，则由当前方法内的线程唤醒，同时设置返回值为-2,即状态为超时
							ScheduledFuture<?> future = schedule(key, configReader.getTransactionNettyDelayTime());

							// 向事务的参与方发送消息，并且在响应中会唤醒当前被阻塞的线程
							threadAwaitSend(task, txInfo, jsonObject.toJSONString());

							// 阻塞当前线程
							task.awaitTask();

							// 执行到此处说明当前线程已经被唤醒，如果不是被future唤醒的则是被其它线程唤醒，取消future
							if (!future.isDone()) {
								future.cancel(false);
							}

							try {
								String data = (String) task.getBack().doing();
								// 成功 :1 、失败 :0 、task为空:-1 、超时:-2
								boolean res = "1".equals(data);

								if (res) {
									txInfo.setNotify(1);
								}

								return res;
							} catch (Throwable throwable) {
								throwable.printStackTrace();
								return false;
							} finally {
								task.remove();
							}
						}
					});
				}
			}

			List<Boolean> hasOks = countDownLatchHelper.execute().getData();

			String key = configReader.getKeyPrefix() + txGroup.getGroupId();
			redisServerService.saveTransaction(key, txGroup.toJsonString());

			boolean hasOk = true;
			for (boolean bl : hasOks) {
				if (!bl) {
					hasOk = false;
					break;
				}
			}
			logger.info("--->" + hasOk + ",group:" + txGroup.getGroupId() + ",state:" + checkSate + ",list:"
					+ txGroup.toJsonString());
			return hasOk;
		} else {
			// 回滚操作只发送通知不需要等待确认，因为即便是通知没有发到，客户端超时后依然会回滚，
			// 即回滚甚至可以不发送通知,发送通知可以提升客户端性能，尽快释放事务锁定的资源
			for (TxInfo txInfo : txGroup.getList()) {
				if (txInfo.getChannel() != null) {
					if (txInfo.getIsGroup() == 0) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("a", "t");
						jsonObject.put("c", checkSate);
						jsonObject.put("t", txInfo.getKid());
						String key = KidUtils.generateShortUuid();
						jsonObject.put("k", key);
						txInfo.getChannel().send(jsonObject.toJSONString());
					}
				}
			}
			txManagerService.deleteTxGroup(txGroup);
			return true;
		}

	}

	@Override
	public String sendCompensateMsg(String model, String groupId, String data, int startState) {
		JSONObject newCmd = new JSONObject();
		newCmd.put("a", "c");
		newCmd.put("d", data);
		newCmd.put("ss", startState);
		newCmd.put("g", groupId);
		newCmd.put("k", KidUtils.generateShortUuid());
		return sendMsg(model, newCmd.toJSONString(), configReader.getRedisSaveMaxTime());
	}

	@Override
	public String sendMsg(final String model, final String msg, int delay) {
		JSONObject jsonObject = JSON.parseObject(msg);
		String key = jsonObject.getString("k");

		// 创建Task
		final Task task = ConditionUtils.getInstance().createTask(key);

		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				while (!task.isAwait() && !Thread.interrupted()) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				Channel channel = SocketManager.getInstance().getChannelByModelName(model);
				if (channel != null && channel.isActive()) {
					SocketUtils.sendMsg(channel, msg);
				}
			}
		});

		ScheduledFuture<?> future = schedule(key, delay);

		task.awaitTask();

		if (!future.isDone()) {
			future.cancel(false);
		}

		try {
			return (String) task.getBack().doing();
		} catch (Throwable throwable) {
			return "-1";
		} finally {
			task.remove();
		}
	}

	private void threadAwaitSend(final Task task, final TxInfo txInfo, final String msg) {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				// 很重要，做个自循环等待，可能执行到这里时候主线程还没有阻塞，所以等待被阻塞后再执行保证逻辑严密
				while (!task.isAwait() && !Thread.interrupted()) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (txInfo != null && txInfo.getChannel() != null) {
					// 向客户端发送信息，客户端接收类为TransactionControlServiceImpl，
					// 客户端会根据msg中a属性是t还是c分别调用ActionTServiceImpl和ActionCServiceImpl，
					// 并将这两个类执行结果再次发送到tm端，tm端的接收类分别为ActionTServiceImpl和ActionCServiceImpl，
					// 注意，客户端与服务端类名是对应的，但不是一个包下面的，不是相同的类。
					// 由代码可知服务端的两个类都实现BaseSignalTaskService，什么都没做，调用父类的方法，把客户端的状态直接返回,并且唤醒被阻塞的线程
					txInfo.getChannel().send(msg, task);
				} else {
					// 事务信息不完整，当成超时返回
					task.setBack(new IBack() {
						@Override
						public Object doing(Object... objs) throws Throwable {
							return "-2";
						}
					});
					// 唤醒被阻塞线程
					task.signalTask();
				}
			}
		});

	}

	private ScheduledFuture<?> schedule(final String key, int delayTime) {
		ScheduledFuture<?> future = executorService.schedule(new Runnable() {
			@Override
			public void run() {
				Task task = ConditionUtils.getInstance().getTask(key);
				if (task != null && !task.isNotify()) {
					task.setBack(new IBack() {
						@Override
						public Object doing(Object... objs) throws Throwable {
							return "-2";
						}
					});
					task.signalTask();
				}
			}
		}, delayTime, TimeUnit.SECONDS);

		return future;
	}

}
