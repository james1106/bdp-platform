package com.bdp.framework.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "bdp_fw_role")
public class Role {

	@Id
	private String id;

	private String name;

	@Column(length = 2000)
	private String description;

	private String orderNO;

	@ManyToMany(cascade = CascadeType.REFRESH)
	@JoinTable(name = "bdp_fw_Role_System", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
			@JoinColumn(name = "system_id") }, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@OrderBy("orderNO asc")
	private List<System> systems;

	@ManyToMany(cascade = CascadeType.REFRESH)
	@JoinTable(name = "bdp_fw_Role_Menu", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
			@JoinColumn(name = "menu_id") }, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@OrderBy("orderNO asc")
	private List<Menu> menus;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrderNO() {
		return orderNO;
	}

	public void setOrderNO(String orderNO) {
		this.orderNO = orderNO;
	}

	public List<System> getSystems() {
		return systems;
	}

	public void setSystems(List<System> systems) {
		this.systems = systems;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

}
