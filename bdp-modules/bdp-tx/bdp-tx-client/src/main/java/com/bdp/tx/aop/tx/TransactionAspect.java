package com.bdp.tx.aop.tx;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.bdp.tx.aop.service.AspectBeforeService;

/**
 * LCN 事务拦截器，会拦截被TxTransaction注解标注的方法，以开启或加入事务组
 */

@Aspect
@Component
public class TransactionAspect implements Ordered {

	private Logger logger = LoggerFactory.getLogger(TransactionAspect.class);

	@Autowired
	private TxGroupInterceptor txGroupInterceptor;

	@Autowired
	private AspectBeforeService aspectBeforeService;

	@Around("@annotation(com.bdp.tx.annotation.TxTransaction)")
	public Object transactionRunning(ProceedingJoinPoint point) throws Throwable {
		logger.debug("annotation-TransactionRunning-start---->");
		String groupId = txGroupInterceptor.obtainTxGroupId();
		Object obj = aspectBeforeService.around(groupId, point);
		logger.debug("annotation-TransactionRunning-end---->");
		return obj;
	}

	@Around("this(com.bdp.tx.annotation.ITxTransaction) && execution( * *(..))")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		logger.debug("interface-ITransactionRunning-start---->");
		String groupId = txGroupInterceptor.obtainTxGroupId();
		Object obj = aspectBeforeService.around(groupId, point);
		logger.debug("interface-ITransactionRunning-end---->");
		return obj;
	}

	// 很重要，保证当前切面会把其它切面包裹住,类似于Servlet的过滤器
	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}
}
