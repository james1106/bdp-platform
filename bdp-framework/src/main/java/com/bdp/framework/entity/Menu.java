package com.bdp.framework.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "bdp_fw_menu")
public class Menu {

	public static final String TYPE_FRAME = "frame";
	public static final String TYPE_LAYER = "layer";
	public static final String TYPE_BLANK = "blank";
	public static final String TYPE_SELF = "self";
	public static final String TYPE_JS = "javascript";

	@Id
	private String id;

	private String code;

	private String title;

	private String icon;

	private String href;

	private String type;

	@Column(length = 2000)
	private String description;

	@ManyToOne(cascade = { CascadeType.REFRESH }, optional = true)
	@JoinColumn(name = "parent_id")
	@JsonIgnore
	private Menu parent;

	@OneToMany(mappedBy = "parent", cascade = { CascadeType.ALL })
	@OrderBy("orderNO asc")
	private List<Menu> children;

	private int orderNO;

	@ManyToOne(cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(name = "busisys_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	@JsonIgnore
	private Busisys busisys;;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getOrderNO() {
		return orderNO;
	}

	public void setOrderNO(int orderNO) {
		this.orderNO = orderNO;
	}

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	public Busisys getBusisys() {
		return busisys;
	}

	public void setBusisys(Busisys busisys) {
		this.busisys = busisys;
	}

}
