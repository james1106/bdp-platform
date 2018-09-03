package com.bdp.tx.model;

import java.io.Serializable;

/**
 * 封装事务方法的调用信息，包括调用的类，方法名，参数，参数类型方便记录日志及其它模块用到，如在事务补偿中会用到
 */
public class TransactionInvocation implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 事务执行器
	 */
	private Class<?> targetClazz;
	/**
	 * 方法
	 */
	private String method;
	/**
	 * 参数值
	 */
	private Object[] argumentValues;

	/**
	 * 参数类型
	 */
	@SuppressWarnings("rawtypes")
	private Class[] parameterTypes;

	/**
	 * 方法字符串
	 */
	private String methodStr;

	public TransactionInvocation() {
	}

	@SuppressWarnings("rawtypes")
	public TransactionInvocation(Class<?> targetClazz, String method, String methodStr, Object[] argumentValues,
			Class[] parameterTypes) {
		this.targetClazz = targetClazz;
		this.method = method;
		this.methodStr = methodStr;
		this.argumentValues = argumentValues;
		this.parameterTypes = parameterTypes;
	}

	public String getMethodStr() {
		return methodStr;
	}

	public void setMethodStr(String methodStr) {
		this.methodStr = methodStr;
	}

	public Class<?> getTargetClazz() {
		return targetClazz;
	}

	public void setTargetClazz(Class<?> targetClazz) {
		this.targetClazz = targetClazz;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object[] getArgumentValues() {
		return argumentValues;
	}

	public void setArgumentValues(Object[] argumentValues) {
		this.argumentValues = argumentValues;
	}

	@SuppressWarnings("rawtypes")
	public Class[] getParameterTypes() {
		return parameterTypes;
	}

	@SuppressWarnings("rawtypes")
	public void setParameterTypes(Class[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
}
