package com.bdp.schedule.dto;

import java.io.Serializable;

/**
 * 封装的是schedule的相关信息，这个对象最终会放在JobDetail的jobDataMap中，<br/>
 * key为SCHEDULEINFO_KEY_IN_JOBDETAIL,在Job子类中可通过这个key值获取当前的ScheduleInfo
 * 
 * @author JACK
 *
 */
public class ScheduleInfo implements Serializable {

	public static final String SCHEDULEINFO_KEY_IN_JOBDETAIL = "scheduleInfo_key";

	private static final long serialVersionUID = 1L;

	private String appId;

	private String group;

	private String name;

	private TriggerInfo triggerInfo;

	private JobInfo jobInfo;

	/**
	 * 第三方应用ID，即为调度服务使用者分配的ID。<br/>
	 * 这个ID与参与到创建各组件Key当中，保证应用内唯一
	 * 
	 * @return
	 */
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * 分组名称
	 * 
	 * @return
	 */
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * name与group两个属性最终会与appId组成各组件Key，<br/>
	 * 所以调度服务使用都需要保证在其应用内这两个值的组合是唯一的
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 关联的调度信息
	 * 
	 * @return
	 */
	public TriggerInfo getTriggerInfo() {
		return triggerInfo;
	}

	public void setTriggerInfo(TriggerInfo triggerInfo) {
		this.triggerInfo = triggerInfo;
	}

	/**
	 * 关联的作业信息
	 * 
	 * @return
	 */
	public JobInfo getJobInfo() {
		return jobInfo;
	}

	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}
}
