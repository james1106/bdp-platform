package com.bdp.ribbon.loadbalancer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.netflix.loadbalancer.ServerListFilter;
import com.netflix.loadbalancer.ServerListUpdater;

/**
 * 自定义ILoadBalancer，很有必要，如果一个A服务中的am1方法,调用了B服务的bm1,bm2方法，
 * 通过自定义的ILoadBalancer，可以保证这两次调用会负载到同一个B服务实例上面。
 * bm1对数据库做的修改，此时事务未提交，因为在同一个实例下面，bm2与bm1用的是同一个数据库连接，
 * 数据的修改对bm2是可见,并且允许bm2继续操作。如果不在同一个实例下面，则bm2会被阻塞，am1无法执行。
 * 另外还有其它好处，如果允许用户使用任意的ILoadBalancer，有些ILoadBalancer是有重试机制，就有可能一个服务被调用多次
 * 
 * @author jack
 *
 */
@Configuration
public class LcnRibbonConfiguration {

	/**
	 * 为ribbon的loadbalancer做代理，相比于重写IRULE,重写loadbalancer更有利于用户自选LB算法，而且有默认LB算法可用
	 * 
	 * @param config
	 * @param serverList
	 * @param serverListFilter
	 * @param rule
	 * @param ping
	 * @param serverListUpdater
	 * @return
	 */
	@Bean
	public ILoadBalancer ribbonLoadBalancer(IClientConfig config, ServerList<Server> serverList,
			ServerListFilter<Server> serverListFilter, IRule rule, IPing ping, ServerListUpdater serverListUpdater) {
		return new LcnZoneAwareLoadBalancerProxy(config, rule, ping, serverList, serverListFilter, serverListUpdater);
	}
}
