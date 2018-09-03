package com.bdp.tx.aop.tx;

/**
 * 获取请求头的事务组ID信息，不同的RPC框架，获取请求参数的方式不同，<br/>
 * 所以这里作个接口，方便spring cloud,dubbo等框架扩展
 */
public interface TxGroupInterceptor {
	/**
	 * 获取当前请求的参数tx-group值，根据框架各自实现。 <br/>
	 * 目前已知框架当前请求基本都会绑定在线程上下文中，很容易通过全局静态方法获取，所以该方法没有参数。
	 * 
	 * @return
	 * @throws Throwable
	 */
	public String obtainTxGroupId() throws Throwable;
}
