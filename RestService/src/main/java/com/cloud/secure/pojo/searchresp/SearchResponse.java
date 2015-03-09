package com.cloud.secure.pojo.searchresp;

import java.util.List;

import com.cloud.secure.pojo.GenericResponse;

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
