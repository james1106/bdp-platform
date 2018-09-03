package com.bdp.tx.aop.service.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bdp.tx.annotation.TxTransaction;
import com.bdp.tx.aop.bean.TxTransactionInfo;
import com.bdp.tx.aop.bean.TxTransactionLocal;
import com.bdp.tx.aop.service.AspectBeforeService;
import com.bdp.tx.aop.service.TransactionServer;
import com.bdp.tx.aop.service.TransactionServerFactoryService;
import com.bdp.tx.model.TransactionInvocation;

import java.lang.reflect.Method;

/**
 * 将事务组ID与当前切入点ProceedingJoinPoint进行关联，对分布式事务信息进行封装，以便后续使用。
 * 里面调用了TransactionServer，这个类会根据封装后的事务信息来确定具体事务行为，新建事务组，还是加入事务组等
 */
@Service
public class AspectBeforeServiceImpl implements AspectBeforeService {

	@Autowired
	private TransactionServerFactoryService transactionServerFactoryService;

	private Logger logger = LoggerFactory.getLogger(AspectBeforeServiceImpl.class);

	public Object around(String groupId, ProceedingJoinPoint point) throws Throwable {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		Class<?> clazz = point.getTarget().getClass();
		Object[] args = point.getArgs();
		Method thisMethod = clazz.getMethod(method.getName(), method.getParameterTypes());

		TxTransaction transaction = thisMethod.getAnnotation(TxTransaction.class);

		//注意，如果当前分布式事务还没开始，则此时获取为空
		TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();

		logger.debug("around--> groupId-> " + groupId + ",txTransactionLocal->" + txTransactionLocal);

		// 封装事务方法的调用信息，包括调用的类，方法名，参数，参数类型方便记录日志及其它模块用到，如在事务补偿中会用到
		TransactionInvocation invocation = new TransactionInvocation(clazz, thisMethod.getName(), thisMethod.toString(),
				args, method.getParameterTypes());

		// 封装事务相关信息
		TxTransactionInfo info = new TxTransactionInfo(transaction, txTransactionLocal, invocation, groupId);

		TransactionServer server = transactionServerFactoryService.createTransactionServer(info);

		return server.execute(point, info);
	}
}
