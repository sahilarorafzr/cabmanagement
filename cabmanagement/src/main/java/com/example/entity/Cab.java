package com.example.entity;

import com.example.constants.Constants;
import java.util.*;

public class Cab {

    private  long id;
	private  String cabNumber;
	private  String driverLicenseId;
	private  String driverName;
	private  long cityId;
    private  String state;
	private  long idleTimeMs;
	private Date lastIdleFrom;

	public Cab(long id, long cityId, String cabNumber, String driverLicenseId, String driverName) {
        this.id=id;
        this.cityId = cityId;
        this.cabNumber = cabNumber;
        this.driverLicenseId = driverLicenseId;
        this.driverName = driverName;
        state = Constants.CAB_STATE_IDLE;
		lastIdleFrom = new Date();
	}

	public long getId() {
		return id;
	}

    public long getCityId() {
		return cityId;
	}

    public void setCityId(long cityId) {
		this.cityId = cityId;
	}

    public String getCabNumber() {
		return cabNumber;
	}

    public String getState() {
		return state;
	}

	public long getIdleTime() {
		return idleTimeMs;
	}

	/**
	 * 
	
	idle->offduty 
	idle->on_trip 
	++ (now-lastIdleFrom)

	on_trip->idle 
	offduty->idle
	update lastIdleFrom to now

	 */
	public void setState(String state) {
		if (this.state.equals(Constants.CAB_STATE_IDLE) && 
			(state.equals(Constants.CAB_STATE_OFF_DUTY) || state.equals(Constants.CAB_STATE_ON_TRIP)) ){
				idleTimeMs =  idleTimeMs + (new Date().getTime()-lastIdleFrom.getTime());
		} else if ( (this.state.equals(Constants.CAB_STATE_OFF_DUTY) || this.state.equals(Constants.CAB_STATE_ON_TRIP)) &&
			state.equals(Constants.CAB_STATE_IDLE)) {
				lastIdleFrom = new Date();
		}
		this.state = state;
	}
}
