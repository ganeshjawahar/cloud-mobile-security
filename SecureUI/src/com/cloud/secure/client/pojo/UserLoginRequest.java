package com.cloud.secure.client.pojo;

import java.io.Serializable;

public class UserLoginRequest implements Serializable {

	String userId;
	String password;
	
	public UserLoginRequest(){
		super();
		userId=password=null;
	}
	
	public UserLoginRequest(String user,String pwd){
		super();
		this.userId=user;
		this.password=pwd;
	}

	public String getUserid() {
		return userId;
	}

	public void setUserid(String userid) {
		this.userId = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
