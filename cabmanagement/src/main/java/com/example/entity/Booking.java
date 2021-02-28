package com.example.entity;

import java.util.*;

public class Booking {

	private long id;
	private long cabId;
	private long fromCityId;
	private long toCityId;
	private Date startDate;
	private Date endDate;

	public Booking(long id, long cabId,  long fromCityId,long toCityId) {
		this.id= id;
		this.cabId = cabId;
		this.fromCityId = fromCityId;
		this.toCityId = toCityId;
		this.startDate = new Date();
	}

	public long getId() {
		return id;
	}

	public long getFromCityId() {
		return fromCityId;
	}

	public long getToCityId() {
		return toCityId;
	}

	public long getCabId() {
		return cabId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void endRide() {
		endDate = new Date();
	}

}
