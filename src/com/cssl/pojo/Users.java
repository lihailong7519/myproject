package com.cssl.pojo;

public class Users {

	private Integer uid;
	private String name;		//账号
	private String password;	//密码
	private String telephone;
	private String username;	//真实用户名
	
		
	public Users() {
		super();
	}
	public Users(String name, String password, String telephone, String username) {
		super();
		this.name = name;
		this.password = password;
		this.telephone = telephone;
		this.username = username;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
