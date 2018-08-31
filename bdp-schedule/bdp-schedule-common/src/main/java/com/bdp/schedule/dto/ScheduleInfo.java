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

	// 应用ID，如果是微服务应用，此值应该为Eureka中注册的服务ID
	private String appId;

	// 应用租户
	private String appTenant = "default";

	// 调度分类，即一个应用可以多种类型调度，通过这个属性做区分
	private String scheduleType;

	// 关联的触发器信息
	private TriggerInfo triggerInfo;

	// 关联的JOB信息
	private JobInfo jobInfo;

	// 当前调度的状态，不用设置，查询返回时由调度服务器设置
	private String state;

	// 排序
	private int orderNO;

	public String getId() {
		return id;
	}

	/**
	 * 业务系统中调度的ID，这个id最终会与appId,appTenant组成各组件Key，<br/>
	 * 所以调度服务使用者需要保证在其应用内这三个值的组合是唯一的
	 * 
	 * @return
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	/**
	 * 调度名称
	 * 
	 * @return
	 */
	public void setName(String name) {
		this.name = name;
		this.setTriggerInfo(this.triggerInfo);
		this.setJobInfo(this.jobInfo);
	}

	public String getGroupId() {
		return groupId;
	}

	/**
	 * 应用内的分组ID，在界面展示时使用，如果业务系统中调度没有分类，则可设置为null
	 * 
	 * @return
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public int getOrderNO() {
		return orderNO;
	}

	/**
	 * 排序，主要在界面展示时使用
	 * 
	 * @return
	 */
	public void setOrderNO(int orderNO) {
		this.orderNO = orderNO;
	}

	public String getScheduleType() {
		return scheduleType;
	}

	/**
	 * 调度类型，用于在应用内把调度分类,比如一个应用即可以有数据加工的调度，还可以有定时索引的调度等
	 * 
	 * @return
	 */
	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}

	public String getAppId() {
		return appId;
	}

	/**
	 * 第三方应用ID，即为调度服务使用者分配的ID。<br/>
	 * 这个ID与参与到创建各组件Key当中，保证应用内唯一<br/>
	 * 如果是微服务应用，此值应该为Eureka中注册的服务ID
	 * 
	 * @return
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppTenant() {
		if (appTenant == null) {
			appTenant = "default";
		}
		return appTenant;
	}

	/**
	 * 第三方应用租户,默认为default
	 * 
	 * @return
	 */
	public void setAppTenant(String appTenant) {
		this.appTenant = appTenant;
	}

	public TriggerInfo getTriggerInfo() {
		return triggerInfo;
	}

	/**
	 * 关联的触发器信息
	 * 
	 * @return
	 */
	public void setTriggerInfo(TriggerInfo triggerInfo) {
		if (StringUtils.isNotEmpty(name) && triggerInfo != null) {
			triggerInfo.setTriggerName(name);
		}
		this.triggerInfo = triggerInfo;
	}

	public JobInfo getJobInfo() {
		return jobInfo;
	}

	/**
	 * 关联的作业信息
	 * 
	 * @return
	 */
	public void setJobInfo(JobInfo jobInfo) {
		if (StringUtils.isNotEmpty(name) && jobInfo != null) {
			jobInfo.setJobName(name);
		}
		this.jobInfo = jobInfo;
	}

	public String getState() {
		return state;
	}

	/**
	 * 调度状态，不用设置，查询返回时由调度服务器设置
	 * 
	 * @return
	 */
	public void setState(String state) {
		this.state = state;
	}
}
