package com.cloud.secure.client.pojo;

import java.util.List;

public class FetchAvailableUsersResponse extends GenericResponse {
	List<String> userList;

	public List<String> getUserList() {
		return userList;
	}

	public void setUserList(List<String> userList) {
		this.userList = userList;
	}

	public FetchAvailableUsersResponse(String code, String message,List<String> userList) {
		super(code, message);
		this.userList=userList;
	}

}
