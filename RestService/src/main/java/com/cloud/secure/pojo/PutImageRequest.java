package com.cloud.secure.pojo;

public class PutImageRequest {
	String userId;
	String sessionId;
	String snapedAt;
	String data;
	String imageId;

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSnapedAt() {
		return snapedAt;
	}

	public void setSnapedAt(String snapedAt) {
		this.snapedAt = snapedAt;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
