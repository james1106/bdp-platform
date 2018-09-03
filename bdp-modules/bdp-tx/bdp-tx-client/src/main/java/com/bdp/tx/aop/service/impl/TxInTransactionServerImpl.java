package com.bdp.tx.aop.service.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;

import com.bdp.tx.aop.bean.TxTransactionInfo;
import com.bdp.tx.aop.service.TransactionServer;

/**
 * 默认事务处理,这个类并不做什么，如果当前线程上下文中已经存在分布式事务，则使用该类做处理。<br/>
 * 这主要实现了事务的共用，即如果当前线程已经在其它事务方法中开启了事务，则再调用其它事务方法共用原来的事务，不再做任务处理
 * 
 */
@Service(value = "txInTransactionServer")
public class TxInTransactionServerImpl implements TransactionServer {

	@Override
	public Object execute(ProceedingJoinPoint point, TxTransactionInfo info) throws Throwable {
		return point.proceed();
	}
}
