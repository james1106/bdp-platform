package com.bdp.tx.aop.service;

import org.aspectj.lang.ProceedingJoinPoint;

import com.bdp.tx.aop.bean.TxTransactionInfo;

/**
 * 根据实现类不同执行不同的事务行为，如新建事务组，加入事务组等<br/>
 */
public interface TransactionServer {
	Object execute(ProceedingJoinPoint point, TxTransactionInfo info) throws Throwable;
}
