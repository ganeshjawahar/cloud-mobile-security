package com.cloud.secure.client.pojo;

import java.io.Serializable;


public class Image implements Serializable {
	String imageId;
	String snapedAt;
	String location;
	String resourcePath;

	public Image() {

	}

	public Image(String imageId, String snapedAt, String location, String url) {
		this.imageId = imageId;
		this.snapedAt = snapedAt;
		this.location = location;
		this.resourcePath = url;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getSnapedAt() {
		return snapedAt;
	}

	public void setSnapedAt(String snapedAt) {
		this.snapedAt = snapedAt;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String url) {
		this.resourcePath = url;
	}
}
