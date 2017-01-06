package org.wang.chuangen.webvs.vo;

public class ResourceRely {

	private int id;
	private int relyId;
	
	public ResourceRely(){}

	public ResourceRely(int id, int relyId) {
		this.id = id;
		this.relyId = relyId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRelyId() {
		return relyId;
	}

	public void setRelyId(int relyId) {
		this.relyId = relyId;
	}
}
