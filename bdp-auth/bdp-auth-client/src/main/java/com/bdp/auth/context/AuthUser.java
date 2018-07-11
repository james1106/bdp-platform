package com.bdp.auth.context;

import java.util.Date;

public class AuthUser {

	private String id;

	private String username;

	private String realname;

	private String sex;

	private Date birthday;

	private String image;

	private String address;

	public AuthUser() {
	}

	public AuthUser(String id, String username, String realname, String sex, Date birthday, String image,
			String address) {
		this.id = id;
		this.username = username;
		this.realname = realname;
		this.sex = sex;
		this.birthday = birthday;
		this.image = image;
		this.address = address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
