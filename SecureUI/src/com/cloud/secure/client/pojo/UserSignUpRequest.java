package com.cloud.secure.client.pojo;

import java.io.Serializable;

public class UserSignUpRequest implements Serializable {
	String id;
	String name;
	String password;
	String clickDelayInSeconds;
	String uploadIntervalInSeconds;
	
	public UserSignUpRequest(){
		super();
		id=name=password=clickDelayInSeconds=uploadIntervalInSeconds=null;
	}
	
	public UserSignUpRequest(String id,String name,String password,String clickDelay,String uploadInterval){
		this.id=id;
		this.name=name;
		this.password=password;
		this.clickDelayInSeconds=clickDelay;
		this.uploadIntervalInSeconds=uploadInterval;				
	}

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
