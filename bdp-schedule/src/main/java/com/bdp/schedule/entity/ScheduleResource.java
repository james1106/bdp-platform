package com.bdp.schedule.entity;

import java.io.Serializable;

/**
 * Schedule资源，通过URL获取资源，返回的应用是ScheduleInfo集合的json数据
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

	// 资源路径。通常情况就是一个Restful的URL
	private String resourceUrl;

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

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
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
