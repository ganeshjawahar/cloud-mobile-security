package com.cloud.secure.client.pojo;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
	String userid;
	int count;
	List<ImageSet> imgSet;

	public User() {

	}

	public User(String userid, List<ImageSet> imgSet,int count) {
		this.userid = userid;
		this.imgSet = imgSet;
		this.count=count;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public List<ImageSet> getImgSet() {
		return imgSet;
	}

	public void setImgSet(List<ImageSet> imgSet) {
		this.imgSet = imgSet;
	}

}
