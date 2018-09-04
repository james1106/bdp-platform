package com.bdp.tx.springcloud.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdp.tx.aop.bean.TxTransactionLocal;

/**
 * 拦截Feign的所有请求，如果当前开启了分布式事务，则在请求头加上事务组ID<br/>
 * 在HttpReqTxGroupInterceptor中会获取这个事务组ID值<br/>
 * 注意：执行这个请求拦截方法在使用hystrix线程池策略时的线程与调用Feign客户端的线程不是同一线程，
 * 获取有关线程绑定的变量会有问题，即使使用InheritableThreadLocal来绑定也一样无法保证能获取到线程绑定变量。
 */
public class TransactionRestTemplateInterceptor implements RequestInterceptor {

	private Logger logger = LoggerFactory.getLogger(TransactionRestTemplateInterceptor.class);

	@Override
	public void apply(RequestTemplate requestTemplate) {
		TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
		String groupId = txTransactionLocal == null ? null : txTransactionLocal.getGroupId();
		logger.info("LCN-SpringCloud TxGroup info -> groupId:" + groupId);
		if (txTransactionLocal != null) {
			requestTemplate.header("tx-group", groupId);
		}
	}
}
