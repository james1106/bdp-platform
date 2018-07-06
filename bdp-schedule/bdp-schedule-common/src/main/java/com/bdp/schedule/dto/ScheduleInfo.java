package com.bdp.schedule.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

/**
 * 封装的是schedule的相关信息，这个对象最终会放在JobDetail的jobDataMap中，<br/>
 * key为SCHEDULEINFO_KEY_IN_JOBDETAIL,在Job子类中可通过这个key值获取当前的ScheduleInfo
 * 
 * @author JACK
 *
 */
public class ScheduleInfo implements Serializable {

	public static final String SCHEDULEINFO_KEY = "scheduleInfo_key";

	private static final long serialVersionUID = 1L;

	// 调度ID
	private String id;

	// 调度名称
	private String name;

	// 应用内的分组ID
	private String groupId;

	// 应用ID
	private String appId;

	// 应用角色，对应于多租户
	private String appRole;

	// 调度分类，即一个应用可以多种类型调度，通过这个属性做区分
	private String scheduleType;

	// 关联的触发器信息
	private TriggerInfo triggerInfo;

	// 关联的JOB信息
	private JobInfo jobInfo;

	// 排序
	private int orderNO;

	/**
	 * id最终会与appId,appRole组成各组件Key，<br/>
	 * 所以调度服务使用者需要保证在其应用内这三个值的组合是唯一的
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 调度名称
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.setTriggerInfo(this.triggerInfo);
		this.setJobInfo(this.jobInfo);
	}

	/**
	 * 应用内的分组ID，在界面展示时使用
	 * 
	 * @return
	 */
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * 排序，主要在界面展示时使用
	 * 
	 * @return
	 */
	public int getOrderNO() {
		return orderNO;
	}

	public void setOrderNO(int orderNO) {
		this.orderNO = orderNO;
	}

	/**
	 * 调度类型，用于在应用内把调度分类
	 * 
	 * @return
	 */
	public String getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}

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
	 * 第三方应用角色，对应于多租户
	 * 
	 * @return
	 */
	public String getAppRole() {
		return appRole;
	}

	public void setAppRole(String appRole) {
		this.appRole = appRole;
	}

	/**
	 * 关联的触发器信息
	 * 
	 * @return
	 */
	public TriggerInfo getTriggerInfo() {
		return triggerInfo;
	}

	public void setTriggerInfo(TriggerInfo triggerInfo) {
		if (StringUtils.isNotEmpty(name) && triggerInfo != null) {
			triggerInfo.setTriggerName(name);
		}
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
		if (StringUtils.isNotEmpty(name) && jobInfo != null) {
			jobInfo.setJobName(name);
		}
		this.jobInfo = jobInfo;
	}

}
