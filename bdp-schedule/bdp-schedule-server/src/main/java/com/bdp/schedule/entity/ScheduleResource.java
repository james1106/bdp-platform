package com.bdp.schedule.entity;

import java.io.Serializable;

/**
 * Schedule资源注册
 * 
 * @author JACK
 *
 */
public class ScheduleResource implements Serializable {

	private static final long serialVersionUID = 1L;

	String id;

	// 资源名称
	private String name;

	// 第三方应用ID
	private String appId;

	// 第三方应用名称
	private String appName;

	// 应用角色，对应于多租户切库
	private String appRole;

	// 调度分类
	private String scheduleType;

	// 分组路径。通常情况就是一个Restful的URL,返回GroupInfo的JSON集合
	private String groupUrl;

	// 调度资源路径。通常情况就是一个Restful的URL，返回ScheduleInfo的JSON集合
	private String scheduleInfoUrl;

	// 调用方式 GET POST UPDATE DELETE等
	private String method;

	// 备注
	private String remark;

	// 排序
	private String orderNo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppRole() {
		return appRole;
	}

	public void setAppRole(String appRole) {
		this.appRole = appRole;
	}

	public String getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}

	public String getGroupUrl() {
		return groupUrl;
	}

	public void setGroupUrl(String groupUrl) {
		this.groupUrl = groupUrl;
	}

	public String getScheduleInfoUrl() {
		return scheduleInfoUrl;
	}

	public void setScheduleInfoUrl(String scheduleInfoUrl) {
		this.scheduleInfoUrl = scheduleInfoUrl;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

}
