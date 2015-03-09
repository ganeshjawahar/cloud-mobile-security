package com.cloud.secure.client.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 908671012757978977L;
	String dateStart;
	String dateEnd;
	List<String> userList;
	List<String> locationList;

	public SearchRequest() {
	}

	public String getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	public List<String> getUserList() {
		return userList;
	}

	public void setUserList(List<String> userList) {
		this.userList = userList;
	}

	public List<String> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<String> locationList) {
		this.locationList = locationList;
	}

}
