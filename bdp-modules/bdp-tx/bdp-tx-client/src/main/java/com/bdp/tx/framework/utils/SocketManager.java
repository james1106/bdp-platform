package com.bdp.tx.framework.utils;

import com.bdp.tx.model.Request;
import com.lorne.core.framework.utils.task.ConditionUtils;
import com.lorne.core.framework.utils.task.IBack;
import com.lorne.core.framework.utils.task.Task;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 客户端与服务器端交互类，主要实现客户端向TM发送消息并接收TM返回的消息 <br/>
 * 发送接收在不同线程中，依靠线程同步做到消息发送并同步接收到返回结果
 */
public class SocketManager {

	private final static Logger logger = LoggerFactory.getLogger(SocketManager.class);

	private ChannelHandlerContext ctx;

	/**
	 * 自动返回数据时间，必须要小于事务模块最大相应时间.(通过心跳获取)
	 */

	private volatile int delay = 1;

	/**
	 * false 未链接 true 连接中
	 */
	private volatile boolean netState = false;

	private static SocketManager manager = null;

	private ExecutorService threadPool = Executors.newFixedThreadPool(max_size);

	private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(max_size);

	private final static int max_size = 50;

	public static SocketManager getInstance() {
		if (manager == null) {
			synchronized (SocketManager.class) {
				if (manager == null) {
					manager = new SocketManager();
				}
			}
		}
		return manager;
	}

	private SocketManager() {
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public boolean isNetState() {
		return netState;
	}

	public void setNetState(boolean netState) {
		this.netState = netState;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * 向TM发送请求
	 * 
	 * @param task
	 * @param request
	 */
	private void sleepSend(Task task, Request request) {
		// 只有当前线程处于等待状态时才发送消息，否则如果线程没有等待，消息已经发送并且得到返回，
		// 则返回处理时调用的唤醒线程的方法就没有用了，则线程再被阻塞就只能依赖超时唤醒，没法达到请求发送并获取返回结果的目的
		while (!task.isAwait() && !Thread.interrupted()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// 因为这个方法调用的是ChannelHandlerContext.writeAndFlush()方法，这个方法发送消息本身也是异步的，<br/>
		// 所以上面必须做个循环等待，如果这个方法发送时是同步的，当前的sleepSend也没有必要放在一个新线程里面执行了,<br/>
		// 这些应该也是netty编程，线程同步的一些技巧
		SocketUtils.sendMsg(ctx, request.toMsg());
		logger.info("send-msg->" + request.toMsg());
	}

	/**
	 * 向服务器发送消息请求，并获取返回结果
	 * 
	 * @param request
	 * @return
	 */
	public String sendMsg(final Request request) {
		final String key = request.getKey();
		if (ctx != null && ctx.channel() != null && ctx.channel().isActive()) {
			// 创建任务与当前key绑定的任务
			final Task task = ConditionUtils.getInstance().createTask(key);
			// 这里是做一个超时处理，由代码可知道，当超过了指定时间delay后会 将当前当前线程唤醒
			ScheduledFuture<?> future = executorService.schedule(new Runnable() {
				@Override
				public void run() {
					Task task = ConditionUtils.getInstance().getTask(key);
					if (task != null && !task.isNotify()) {
						task.setBack(new IBack() {
							@Override
							public Object doing(Object... objs) throws Throwable {
								return null;
							}
						});
						// 唤醒等待线程
						task.signalTask();
					}
				}
			}, delay, TimeUnit.SECONDS);

			// 在这个线程中做请求的发送操作，为什么重开线程，参考该方法实现的注释
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					sleepSend(task, request);
				}
			});

			// 当前线程等待,在NettyControlServiceImpl.responseMsg()被唤醒
			task.awaitTask();

			// 如果当前future未执行，则关闭，即当前线程不是由超时唤醒的
			if (!future.isDone()) {
				future.cancel(false);
			}

			try {
				// 获取服务器端的返回值，注意这里的task.getBack()获得的Back是在NettyControlServiceImpl.responseMsg()方法里面设置的
				// 这种线程通过线程同步的方式很有用。虽然收发消息是异步的，但却实现了同步效果，在一个方法里面实现了异步发送，异步接收，方法却是同步返回的。
				Object msg = task.getBack().doing();
				return (String) msg;
			} catch (Throwable e) {
			} finally {
				task.remove();
			}
		}
		return null;

	}

	public void close() {
		if (threadPool != null) {
			threadPool.shutdown();
		}
		if (executorService != null) {
			executorService.shutdown();
		}
	}
}
