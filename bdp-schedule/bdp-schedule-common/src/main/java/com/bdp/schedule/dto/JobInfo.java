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
	
	//作业名称
	private String jobName;

	// 回调服务的客户端类型,根据不同的类型选择不同的job子类
	private String clientType = "http";

	// 回调路径,无论是哪种方式的回调，回调路径都是一个字符串，<br/>
	// 这个字符串对于每种回调类型应该有不同的格式.由相应的job子类负责解析<br/>
	// 这种设计也是为了使用API尽量灵活。通常情况就是一个Restful的URL
	// 某些调度这个参数不需要可以为空
	private String callBackPath;

	// 调用方式 GET POST PUT DELETE等
	private String method;

	// 扩展参数，最后会当成参数传入到回调的客户端，这要求回调的客户端只能接收一个Map<String, Object>类型的参数。如果该集合为空，传入空Map
	private Map<String, Object> extralParams = new HashMap<String, Object>();

	/**
	 * 作业名称
	 * @return
	 */
	public String getJobName() {
		return jobName;
	}

	void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * 回调服务的客户端类型,根据不同的类型选择不同的job子类 <br/>
	 * 目前取值仅支持 http
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
	 * 这种设计也是为了使用API尽量灵活。通常情况就是一个Restful的URL<br/>
	 * 某些JOB不需要回调，则此字段可为空。对于注册到eureka上的服务需要如下格式:<br/>
	 * http://serviceId/user/1000 serviceId即注册到eureka上的微服务Id<br/>
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
	 * 扩展参数，最后会当成参数传入到回调的客户端，这要求回调的客户端只能接收一个Map<String, Object>类型的参数。<br/>
	 * 对于http的调用，会将这个集合项转为url参数。<br/>
	 * 如果该集合为空，也会执行一次调度，传入空Map。<br/>
	 * 这样设计是为了以后扩展，比如调度服务支持如果支持定时发送邮件的功能，这个参数就可以把发送邮件相关的信息放在里面，在调度时解析使用<br/>
	 * 
	 * @return
	 */
	public Map<String, Object> getExtralParams() {
		return extralParams;
	}

	public void setExtralParams(Map<String, Object> extralParams) {
		this.extralParams = extralParams;
	}
}
