package com.cloud.secure.client.pojo;

import java.io.Serializable;

public class UserLoginResponse extends GenericResponse implements Serializable{
	String userId;
	String name;
	String password;
	String clickDelayInSeconds;
	String uploadIntervalInSeconds;
	
	public UserLoginResponse(){
		super();
		userId=name=password=clickDelayInSeconds=uploadIntervalInSeconds=null;
	}
	
	public UserLoginResponse(String code,String message,String userid,String name,String password,String clickDelayInSeconds,String uploadIntervalInSeconds){
		super(code,message);
		this.userId=userid;
		this.name=name;
		this.password=password;
		this.clickDelayInSeconds=clickDelayInSeconds;
		this.uploadIntervalInSeconds=uploadIntervalInSeconds;		
	}

	public String getUserid() {
		return userId;
	}

	public void setUserid(String userid) {
		this.userId = userid;
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

	public String getClickDelayInSeconds() {
		return clickDelayInSeconds;
	}

	public void setClickDelayInSeconds(String clickDelayInSeconds) {
		this.clickDelayInSeconds = clickDelayInSeconds;
	}

	public String getUploadIntervalInSeconds() {
		return uploadIntervalInSeconds;
	}

	public void setUploadIntervalInSeconds(String uploadIntervalInSeconds) {
		this.uploadIntervalInSeconds = uploadIntervalInSeconds;
	}

}
