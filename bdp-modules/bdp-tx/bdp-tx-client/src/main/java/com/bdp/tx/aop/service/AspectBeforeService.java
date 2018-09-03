package com.bdp.tx.aop.service;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 根据切入点配置及当前上下文对事务信息封装
 * 
 * @author jack
 *
 */
public interface AspectBeforeService {
	Object around(String groupId, ProceedingJoinPoint point) throws Throwable;
}
