package com.bdp.schedule.dto;

import java.io.Serializable;
import java.util.List;

public class GroupInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private String remark;

	private String parentId;

	private List<GroupInfo> children;

	private int orderNO;

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<GroupInfo> getChildren() {
		return children;
	}

	public void setChildren(List<GroupInfo> children) {
		this.children = children;
	}

	public int getOrderNO() {
		return orderNO;
	}

	public void setOrderNO(int orderNO) {
		this.orderNO = orderNO;
	}
}
