package com.bdp.tx.springcloud.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.bdp.tx.aop.bean.TxTransactionLocal;

import java.io.IOException;

/**
 * 拦截所有的RestTemplate的所有请求，如果当前开启了分布式事务，则在请求头加上事务组ID<br/>
 * 在HttpReqTxGroupInterceptor中会获取这个事务组ID值
 */
public class TransactionHttpRequestInterceptor implements ClientHttpRequestInterceptor {

	private Logger logger = LoggerFactory.getLogger(TransactionHttpRequestInterceptor.class);

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
		String groupId = txTransactionLocal == null ? null : txTransactionLocal.getGroupId();

		logger.info("LCN-SpringCloud TxGroup info -> groupId:" + groupId);

		if (txTransactionLocal != null) {
			request.getHeaders().add("tx-group", groupId);
		}
		return execution.execute(request, body);
	}
}
