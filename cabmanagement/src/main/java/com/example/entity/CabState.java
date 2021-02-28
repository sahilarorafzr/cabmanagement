package com.example.entity;

import java.util.*;

public class CabState {

	private long id;
	private long cabId;
	private long cityId;
	private String state;
	private Date date;

	public CabState(long id, long cabId,  long cityId, String state) {
		this.id= id;
		this.cabId = cabId;
		this.cityId = cityId;
		this.state = state;
		date = new Date();
	}

	public long getId() {
		return id;
	}

	public long getCityId() {
		return cityId;
	}

	public long getCabId() {
		return cabId;
	}

	public String getState() {
		return state;
	}

	public Date getDate() {
		return date;
	}

}
