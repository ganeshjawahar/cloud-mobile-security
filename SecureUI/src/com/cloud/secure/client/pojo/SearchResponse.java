package com.cloud.secure.client.pojo;

import java.util.List;

public class SearchResponse extends GenericResponse {
	public SearchResponse(String code, String message,List<User> userList) {
		super(code, message);
		this.userList=userList;
	}

	List<User> userList;

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

}
