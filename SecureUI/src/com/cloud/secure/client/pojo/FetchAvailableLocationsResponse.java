package com.cloud.secure.client.pojo;

import java.util.List;

public class FetchAvailableLocationsResponse extends GenericResponse {
	List<String> locationList;

	public FetchAvailableLocationsResponse(String code, String message,
			List<String> locationList) {
		super(code, message);
		this.locationList = locationList;
	}

	public List<String> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<String> locationList) {
		this.locationList = locationList;
	}

}
