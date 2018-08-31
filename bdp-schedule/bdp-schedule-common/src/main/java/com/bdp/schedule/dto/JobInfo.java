package com.bdp.schedule.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


/**
 * 封装的是job调用的相关信息
 * 
 * @author JACK
 */
public class JobInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	// 作业名称
	private String jobName;

	// 回调服务的客户端类型,根据不同的类型选择不同的job子类
	private String clientType = "http";

	// 回调路径,无论是哪种方式的回调，回调路径都是一个字符串，<br/>
	// 对于不是微服务实现的外部系统，使用以http开头的url绝对路径，<br/>
	// 否则应为有意义的回调名称，会根据这个名称从spring容器中查找的所有ScheduleCallBack接口实现<br/>
	// 最后判断哪个实现可以支持本次调度的执行。这种设计也是为了使API尽量灵活
	private String callBack;

	// 执行过程是否为同步执行，当callBack为spring bean名称时此值会固定为false
	private boolean syncExecute = true;

	// 调用方式 GET POST PUT DELETE等
	private String method = "POST";

	// 扩展参数，最后会当成参数传入到回调的客户端，这要求回调的客户端只能接收一个Map<String, Object>类型的参数。如果该集合为空，传入空Map
	private Map<String, Object> extralParams = new HashMap<String, Object>();

	public String getJobName() {
		return jobName;
	}

	void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * 作业名称
	 * 
	 * @return
	 */
	public String getClientType() {
		return clientType;
	}

	/**
	 * 回调服务的客户端类型,根据不同的类型选择不同的job子类 <br/>
	 * 目前取值仅支持 http
	 * 
	 * @return
	 */
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getCallBack() {
		return callBack;
	}

	/**
	 * 回调路径,无论是哪种方式的回调，回调路径都是一个字符串，<br/>
	 * 对于不是微服务实现的外部系统，使用以http开头的url绝对路径，<br/>
	 * 否则应为有意义的回调名称，会根据这个名称从spring容器中查找的所有ScheduleCallBack接口实现<br/>
	 * 最后判断哪个实现可以支持本次调度的执行。这种设计也是为了使API尽量灵活，此时回调的路径为:<br/>
	 * http://appId/scheduleClient/execute,这个路径在使用bdp-schedule-client时会自动引入,<br/>
	 * 在这个回调中会异步调用ScheduleCallBack接口实现类来实际执行调度任务，并且执行结果会再次写到调度服务器上。<br/>
	 * 这样可以减少使用调度框架复杂度，提升调度服务器的性能.另外如果不想使用bdp-schedule-client自带的回调方式，<br/>
	 * 同样可以写绝对路径，对于微服务应用格式为：http://appId/xxx/xxx,其中appId为Eureka中注册的ID
	 * 
	 * @return
	 */
	public void setCallBack(String callBackPath) {
		this.callBack = callBackPath;
	}

	public String getMethod() {
		return method;
	}

	/**
	 * 回调方式 GET POST PUT DELETE等
	 * 
	 * @return
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	public boolean isSyncExecute() {
		// 不以http开头，则必为异步
		if (!StringUtils.startsWith(callBack, "http")) {
			syncExecute = false;
		}
		return syncExecute;
	}

	/**
	 * 回调执行是否为同步执行
	 * 
	 * @return
	 */
	public void setSyncExecute(boolean syncExecute) {
		this.syncExecute = syncExecute;
	}

	public Map<String, Object> getExtralParams() {
		return extralParams;
	}

	/**
	 * 扩展参数，最后会当成参数传入到回调的客户端，这要求回调的客户端只能接收一个Map<String, Object>类型的参数。<br/>
	 * 对于http的调用，会将这个集合项转为url参数，如果没有参数则置为空即可。<br/>
	 * 这样设计是为了以后扩展，比如调度服务支持如果支持定时发送邮件的功能，这个参数就可以把发送邮件相关的信息放在里面，在调度时解析使用<br/>
	 * 另外请求时会将当前执行的日志ID以logId为键放入请求头中传回，便于异步调用时日志回写。注意是请求头，不是参数
	 * 
	 * @return
	 */
	public void setExtralParams(Map<String, Object> extralParams) {
		this.extralParams = extralParams;
	}
}
