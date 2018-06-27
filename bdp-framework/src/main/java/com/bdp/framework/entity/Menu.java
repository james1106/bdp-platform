package com.bdp.framework.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "bdp_fw_menu")
public class Menu {

	@Id
	private String id;

	private String code;

	private String title;

	private String icon;

	private String href;

	private String type;// nomal,popup,blank,redirect

	@Column(name = "parentId")
	private String parentId;

	@Column(name = "orderNo")
	private int orderNo;

	private String description;
	
	@Column(name = "busiSysId")
	private String busiSysId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBusiSysId() {
		return busiSysId;
	}

	public void setBusiSysId(String busiSysId) {
		this.busiSysId = busiSysId;
	}
}
