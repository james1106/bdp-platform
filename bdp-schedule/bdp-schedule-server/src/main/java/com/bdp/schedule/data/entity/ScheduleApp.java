package com.bdp.schedule.data.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 使用调度服务的第三方应用注册信息
 * 
 * @author JACK
 *
 */
@Entity
@Table(name = "bdp_shd_app")
public class ScheduleApp implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	String id;

	// 第三方应用ID
	@Column(name = "appId")
	private String appId;

	// 第三方应用名称
	@Column(name = "appName")
	private String appName;

	// 应用租户
	@Column(name = "appTenant")
	private String appTenant;

	// 备注
	@Column(name = "remark", length = 4000)
	private String remark;

	// 排序
	@Column(name = "orderNo")
	private String orderNo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getAppTenant() {
		return appTenant;
	}

	public void setAppTenant(String appTenant) {
		this.appTenant = appTenant;
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
