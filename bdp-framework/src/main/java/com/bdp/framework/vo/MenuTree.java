package com.bdp.framework.vo;

import com.bdp.common.vo.TreeNode;

public class MenuTree extends TreeNode {
	String code;
	String icon;
	String title;
	String href;
	boolean expanded = false;// 是否展开
	String type;

	public MenuTree() {
	}

	public MenuTree(int id, String name, int parentId) {
		this.id = id;
		this.parentId = parentId;
		this.title = name;
	}

	public MenuTree(int id, String name, MenuTree parent) {
		this.id = id;
		this.parentId = parent.getId();
		this.title = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
