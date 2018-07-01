package com.bdp.config.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bdp_cfg_property")
public class Property {

	@Id
	private String id;

	private String proKey;

	private String proValue;

	private String application;

	private String profile;

	private String label;

	private int orderNO;

	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProKey() {
		return proKey;
	}

	public void setProKey(String proKey) {
		this.proKey = proKey;
	}

	public String getProValue() {
		return proValue;
	}

	public void setProValue(String proValue) {
		this.proValue = proValue;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getOrderNO() {
		return orderNO;
	}

	public void setOrderNO(int orderNO) {
		this.orderNO = orderNO;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
