package com.bdp.schedule.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "bdp_shd_log")
public class ScheduleLog {

	public static final String STATUS_SUCCESS = "success";
	public static final String STATUS_FAILURE = "failure";
	public static final String STATUS_RUNNING = "running";

	@Id
	@Column(name = "id")
	private String id;

	/**
	 * 应用ID
	 */
	@Column(name = "appId")
	private String appId;

	/**
	 * 应用租户
	 */
	@Column(name = "appTenant")
	private String appTenant;

	/**
	 * 调度ID
	 */
	@Column(name = "scheduleInfoId")
	private String scheduleInfoId;

	/**
	 * 开始时间
	 */
	@Column(name = "startDate")
	private Date startDate;

	/**
	 * 结束时间
	 */
	@Column(name = "endDate")
	private Date endDate;

	/**
	 * 调度结果 success,failure,running
	 */
	@Column(name = "status")
	private String status;

	/**
	 * 执行信息
	 */
	@Lob
	@Column(name = "logMsg")
	private String logMsg;

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

	public String getAppTenant() {
		return appTenant;
	}

	public void setAppTenant(String appTenant) {
		this.appTenant = appTenant;
	}

	public String getScheduleInfoId() {
		return scheduleInfoId;
	}

	public void setScheduleInfoId(String scheduleInfoId) {
		this.scheduleInfoId = scheduleInfoId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLogMsg() {
		return logMsg;
	}

	public void setLogMsg(String logMsg) {
		this.logMsg = logMsg;
	}
}
