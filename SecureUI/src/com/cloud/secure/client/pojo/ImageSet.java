package com.cloud.secure.client.pojo;

import java.io.Serializable;
import java.util.List;

public class ImageSet implements Serializable {
	String day;
	int count;
	List<Image> imgs;

	public ImageSet() {
	}

	public ImageSet(String day, List<Image> imgs,int count) {
		this.day = day;
		this.imgs = imgs;
		this.count=count;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public List<Image> getImgs() {
		return imgs;
	}

	public void setImgs(List<Image> imgs) {
		this.imgs = imgs;
	}

}
