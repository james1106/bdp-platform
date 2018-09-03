package com.bdp.tx.framework.thread;

import java.util.concurrent.TimeUnit;

import com.bdp.tx.Constants;

/**
 * 该类为虚拟机注册回调钩子，当虚拟机关闭时，如果还有未结束的事物。</br>
 * 则通过自循环保证线程不结束，以延迟虚拟机的关机，直到事务结束。
 */
public abstract class HookRunnable implements Runnable {

	private volatile boolean hasOver;

	@Override
	public void run() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				Constants.hasExit = true;
				while (!hasOver) {
					try {
						TimeUnit.MILLISECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		if (!Constants.hasExit) {
			Runtime.getRuntime().addShutdownHook(thread);
		} else {
			System.out.println("jvm has exit..");
			return;
		}
		try {
			run0();
		} finally {
			hasOver = true;
			if (!thread.isAlive()) {
				Runtime.getRuntime().removeShutdownHook(thread);
			}
		}
	}

	public abstract void run0();
}
