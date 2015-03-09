package com.cloud.secure.pojo;

public class UserSignUpRequest {
	String id;
	String name;
	String password;
	String clickDelayInSeconds;
	String uploadIntervalInSeconds;

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
