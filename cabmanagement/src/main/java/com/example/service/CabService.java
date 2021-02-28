package com.example.service;

import com.example.exceptions.AppException;

import com.example.dao.CabDao;
import com.example.entity.Cab;
import com.example.entity.CabState;
import com.example.entity.Booking;
import com.example.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CabService {
    
    @Autowired
    CabDao cabDao;

    @Autowired
    CityService cityService;

    public long registerCab(Map<String, Object> cabDetails) throws AppException {
        // TODO add validations for cabDetails
        long cityId = ((Number)cabDetails.get("cityId")).longValue();
        if (!cityService.validateCity(cityId)) {
            throw new AppException("Invalid cityId "+ cityId+". City not being served");
        }
        return cabDao.addNewCab(cabDetails);
    }

    public boolean updateCabLocation(Map<String, Object> payload) throws AppException {
        long cabId = ((Number)payload.get("cabId")).longValue();
        long cityId = ((Number)payload.get("cityId")).longValue();
        if (!cityService.validateCity(cityId)) {
            throw new AppException("Invalid cityId "+ cityId+". City not being served");
        }
        // TODO
        // if (!validateLocation(payload)) {
        //     throw new AppException("invalid location entered");
        // }
        return cabDao.updateCabLocationAndState(cabId, cityId);
    }

    public boolean updateCabState(Map<String, Object> payload) throws AppException {
        String duty = (String)payload.get("duty");
        if (duty.equals("ON")) {
            payload.put("state", Constants.CAB_STATE_IDLE);
        } else if (duty.equals("OFF")){
            payload.put("state", Constants.CAB_STATE_OFF_DUTY);
        } else {
            throw new AppException("Invalid duty passed "+ duty+". Available values ON/OFF");
        }
        return cabDao.updateCabState(payload);
    }

    public Booking book(Map<String, Object> bookingDetails) throws AppException {
        Long fromCity = ((Number)bookingDetails.get("fromCity")).longValue();
        Long toCity = ((Number)bookingDetails.get("toCity")).longValue();
        if (!cityService.validateCity(fromCity)) {
            throw new AppException("Invalid fromCity "+ fromCity+". City not being served");
        }
        if (!cityService.validateCity(toCity)) {
            throw new AppException("Invalid toCity "+ toCity+". City not being served");
        }
        return cabDao.book(fromCity, toCity);
    }

    public Booking endBooking(Map<String, Object> bookingDetails) throws AppException {
        Long bookingId = ((Number)bookingDetails.get("bookingId")).longValue();
        return cabDao.endBooking(bookingId);
    }

    public Map<String, String> getCabIdleTime(long cabId, Date fromDate, Date toDate) throws AppException {
        Date currentTime = new Date();
        if (toDate.getTime() > currentTime.getTime()) {
            toDate = currentTime;
        }
        return cabDao.getCabIdleTime(cabId, fromDate, toDate);
    }

    public Map<Long,HashMap<Integer,Integer>> getHighDemandStats() throws AppException {
        return cabDao.getHighDemandStats();
    }

    public List<CabState> getCabStates(long cabId) throws AppException {
        return cabDao.getCabStates(cabId);
    }

    public List<Cab> listAll() {
        return cabDao.listAll();
    }

}
