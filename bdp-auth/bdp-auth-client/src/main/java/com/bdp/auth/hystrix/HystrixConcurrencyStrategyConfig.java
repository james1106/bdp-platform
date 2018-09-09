package com.bdp.auth.hystrix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import com.netflix.hystrix.strategy.properties.HystrixProperty;

/**
 * 自定义HystrixConcurrencyStrategy，保证使用hystrix的THREAD隔离策略时<br/>
 * Feign请求拦截器可以拦截到线程上下文信息
 * 
 * @author jack
 *
 */
@Configuration
@ComponentScan
@ConditionalOnProperty(prefix = "hystrix.command.default.execution.isolation", name = "strategy", havingValue = "THREAD", matchIfMissing = false)
public class HystrixConcurrencyStrategyConfig {

	@Autowired
	private List<ThreadLocalShareRegister> registers;

	private HystrixConcurrencyStrategy existingConcurrencyStrategy;

	@PostConstruct
	public void init() {
		this.existingConcurrencyStrategy = HystrixPlugins.getInstance().getConcurrencyStrategy();
		if (this.existingConcurrencyStrategy instanceof ThreadLocalShareConcurrencyStrategy) {
			return;
		}
		// 保证原始设置，重置后将HystrixConcurrencyStrategy设置为自定义类型，然后再还原初始设置
		HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance().getEventNotifier();
		HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher();
		HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance().getPropertiesStrategy();
		HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins.getInstance().getCommandExecutionHook();
		HystrixPlugins.reset();
		HystrixPlugins.getInstance()
				.registerConcurrencyStrategy(new ThreadLocalShareConcurrencyStrategy(existingConcurrencyStrategy));
		HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
		HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
		HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
		HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
	}

	class ThreadLocalShareConcurrencyStrategy extends HystrixConcurrencyStrategy {
		private HystrixConcurrencyStrategy existingConcurrencyStrategy;

		public ThreadLocalShareConcurrencyStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy) {
			this.existingConcurrencyStrategy = existingConcurrencyStrategy;
		}

		@Override
		public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
			return existingConcurrencyStrategy != null ? existingConcurrencyStrategy.getBlockingQueue(maxQueueSize)
					: super.getBlockingQueue(maxQueueSize);
		}

		@Override
		public <T> HystrixRequestVariable<T> getRequestVariable(HystrixRequestVariableLifecycle<T> rv) {
			return existingConcurrencyStrategy != null ? existingConcurrencyStrategy.getRequestVariable(rv)
					: super.getRequestVariable(rv);
		}

		@Override
		public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
				HystrixProperty<Integer> corePoolSize, HystrixProperty<Integer> maximumPoolSize,
				HystrixProperty<Integer> keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
			return existingConcurrencyStrategy != null
					? existingConcurrencyStrategy.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize,
							keepAliveTime, unit, workQueue)
					: super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		}

		@Override
		public <T> Callable<T> wrapCallable(Callable<T> callable) {
			return existingConcurrencyStrategy != null
					? existingConcurrencyStrategy.wrapCallable(new ThreadLocalShareContextCallable<T>(callable))
					: super.wrapCallable(new ThreadLocalShareContextCallable<T>(callable));
		}
	}

	@SuppressWarnings("rawtypes")
	class ThreadLocalShareContextCallable<V> implements Callable<V> {
		private final Callable<V> delegate;
		private final Map<ThreadLocal, Object> delegateObjects;
		private Map<ThreadLocal, Object> originalObjects;

		public ThreadLocalShareContextCallable(Callable<V> delegate) {
			this.delegate = delegate;
			this.delegateObjects = new HashMap<>();
			getThreadLocalObjects(this.delegateObjects);
		}

		private void getThreadLocalObjects(Map<ThreadLocal, Object> store) {
			if (CollectionUtils.isNotEmpty(registers)) {
				for (ThreadLocalShareRegister register : registers) {
					List<ThreadLocal> locals = register.getThreadLocals();
					if (CollectionUtils.isNotEmpty(locals)) {
						for (ThreadLocal<?> threadLocal : locals) {
							store.put(threadLocal, threadLocal.get());
						}
					}
				}
			}
		}

		@SuppressWarnings("unchecked")
		private void setThreadLocalObjects(Map<ThreadLocal, Object> store) {
			for (ThreadLocal local : store.keySet()) {
				Object obj = store.get(local);
				local.set(obj);
			}
		}

		public V call() throws Exception {
			try {
				this.originalObjects = new HashMap<>();
				getThreadLocalObjects(this.originalObjects);
				setThreadLocalObjects(this.delegateObjects);
				V reV = this.delegate.call();
				return reV;
			} finally {
				setThreadLocalObjects(this.originalObjects);
				this.originalObjects = null;
			}

		}

		public String toString() {
			return this.delegate.toString();
		}
	}
}