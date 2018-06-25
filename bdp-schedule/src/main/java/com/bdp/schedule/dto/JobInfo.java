package com.bdp.schedule.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装的是job调用的相关信息
 * 
 * @author JACK
 */
public class JobInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	// 回调服务的客户端类型,根据不同的类型选择不同的job子类
	private String clientType;

	// 回调路径,无论是哪种方式的回调，回调路径都是一个字符串，<br/>
	// 这个字符串对于每种回调类型应该有不同的格式.由相应的job子类负责解析<br/>
	// 这种设计也是为了使用API尽量灵活。通常情况就是一个Restful的URL
	private String callBackPath;

	// 调用方式 GET POST PUT DELETE等
	private String method;

	// 回调参数，这要求回调的客户端只能接收一个Map<String, Object>类型的参数。如果该集合为空，传入空Map
	private Map<String, Object> callbackParams = new HashMap<String, Object>();

	/**
	 * 回调服务的客户端类型,根据不同的类型选择不同的job子类
	 * 
	 * @return
	 */
	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	/**
	 * 回调路径,无论是哪种方式的回调，回调路径都是一个字符串，<br/>
	 * 这个字符串对于每种回调类型应该有不同的格式.由相应的job子类负责解析<br/>
	 * 这种设计也是为了使用API尽量灵活。通常情况就是一个Restful的URL
	 * 
	 * @return
	 */
	public String getCallBackPath() {
		return callBackPath;
	}

	public void setCallBackPath(String callBackPath) {
		this.callBackPath = callBackPath;
	}

	/**
	 * 回调方式 GET POST PUT DELETE等
	 * 
	 * @return
	 */
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * 回调参数，这要求回调的客户端只能接收一个Map<String, Object>类型的参数。<br/>
	 * 对于http的调用，会将这个集合项转为url参数。<br/>
	 * 如果该集合为空，也会执行一次调度，传入空Map。<br/>
	 * 这样设计具有最大的灵活性，这就允许调度服务使用者自己组织参数。完全可以根据回调参数做复杂的调度任务<br/>
	 * 
	 * @return
	 */
	public Map<String, Object> getCallbackParams() {
		return callbackParams;
	}

	public void setCallbackParams(Map<String, Object> callbackParams) {
		this.callbackParams = callbackParams;
	}
}
