package com.example.dao;

import com.example.entity.Cab;
import com.example.entity.Booking;
import com.example.entity.CabState;
import com.example.constants.Constants;
import com.example.exceptions.AppException;
import com.example.exceptions.AppExceptionNotFound;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class CabDao {
    
    private long lastUsedCabId = 0;
    private long lastUsedStateId = 0;
    private long lastUsedBookingId = 0;
    
    //cabid -> cabstate
    Map<Long,List<CabState>> cabStates = new HashMap<>();

    //bookingId->booking
    Map<Long,Booking> cabBookings = new HashMap<>();

    //cabid -> cab
    Map<Long,Cab> allCabs = new HashMap<>();
    
    public long addState(Long cabId,Long cityId, String state) {
        long newStateId = ++lastUsedStateId;
        CabState cabState = new CabState(newStateId, cabId, cityId, state);
        if (cabStates.containsKey(cabId)) {
            cabStates.get(cabId).add(cabState);
        } else {
            List<CabState> states = new ArrayList<>();
            states.add(cabState);
            cabStates.put(cabId, states);
        }
        // update state in cab
        allCabs.get(cabId).setState(state);
        return newStateId;
    }

    public long addNewCab(Map<String, Object> cabDetails) {
        long newCabId = ++lastUsedCabId;
        long cityId = ((Number)cabDetails.get("cityId")).longValue();
        Cab newCab = new Cab(
            newCabId,
            cityId,
            (String) cabDetails.get("cabNumber"),
            (String) cabDetails.get("lisenceId"),
            (String) cabDetails.get("driverName")
        );
        allCabs.put(newCabId,newCab);
        addState(
            newCab.getId(),
            newCab.getCityId(),
            newCab.getState()
        );
        return newCabId;
    }

    public boolean updateCabLocation(long cabId, long cityId) {
        if (!allCabs.containsKey(cabId)) {
            throw new AppException("Invalid cab id "+cabId+". Cab not registered");
        }
        allCabs.get(cabId).setCityId(cityId);
        return true;
    }

    public boolean updateCabLocationAndState(long cabId, long cityId) {
        updateCabLocation(cabId, cityId);
        List<CabState> states = cabStates.get(cabId);
        addState(cabId, cityId, states.get(states.size()-1).getState());
        return true;
    }

    public boolean updateCabState(Map<String, Object> cabDetails) {
        
        long cabId = ((Number)cabDetails.get("cabId")).longValue();
        String state = (String)cabDetails.get("state");
        if (!allCabs.containsKey(cabId)) {
            throw new AppException("Invalid cab id "+cabId+". Cab not registered");
        }
        Cab cab = allCabs.get(cabId);
        if (cab.getState().equals(Constants.CAB_STATE_IDLE) && state.equals(Constants.CAB_STATE_IDLE)) {
            throw new AppException("Already on duty");
        }
        if (cab.getState().equals(Constants.CAB_STATE_ON_TRIP)) {
            throw new AppException("Can not update while on trip");
        }
        addState(
            cabId,
            allCabs.get(cabId).getCityId(),
            state
        );
        return true;
    }

    public Booking book(long fromCity, long toCity) throws AppException{
        Cab cab = allCabs.entrySet().stream()
            .map((entry)->entry.getValue()) 
            .filter((cabObj) -> cabObj.getCityId()==fromCity && cabObj.getState().equals(Constants.CAB_STATE_IDLE))
            .max(Comparator.comparing(Cab::getIdleTime))
            .orElse(null);
            // .orElseThrow(NoSuchElementException::new);;
        if (null == cab) {
            throw new AppExceptionNotFound("No cabs available in your area");
        }
        addState(
            cab.getId(),
            fromCity,
            Constants.CAB_STATE_ON_TRIP
        );
        long bookingId = addBooking(fromCity, toCity, cab.getId());
        Booking booking = cabBookings.get(bookingId);
        return booking;
    }

    private long addBooking(long fromCity, long toCity, long cabId) {
        long bookingId = ++lastUsedBookingId;
        Booking booking = new Booking(bookingId, cabId, fromCity, toCity);
        cabBookings.put(bookingId, booking);
        return bookingId;
    }

    public Booking endBooking(long bookingId) throws AppException {
        Booking booking = cabBookings.get(bookingId);
        if (booking == null) {
            throw new AppException("Invalid booking id " + bookingId);
        }
        if (null != booking.getEndDate()) {
            throw new AppException("Booking already ended");
        }
        booking.endRide();
        updateCabLocation(booking.getCabId(), booking.getToCityId());
        addState(
            booking.getCabId(),
            booking.getToCityId(),
            Constants.CAB_STATE_IDLE
        );
        
        return booking;
    }

    public Map<String, String> getCabIdleTime(long cabId, Date fromDate, Date toDate) throws AppException {
        long idleTimeMs = 0;
        Map<String, String> result = new HashMap<>();
        if (!cabStates.containsKey(cabId)) {
            throw new AppException("Invalid cab id. Cab not registered");
        }
        List<CabState> states = cabStates.get(cabId);
        CabState previous = states.get(0);
        System.out.println(previous.getState());
        System.out.println(previous.getDate());
        for(CabState state: states) {
            if (state.getDate().getTime() > toDate.getTime()) {
                break;
            }
            if (state.getDate().getTime() >= fromDate.getTime() && state.getDate().getTime() <= toDate.getTime()) {
                if (previous.getState().equals(Constants.CAB_STATE_IDLE)) {
                    if(previous.getDate().getTime() >=fromDate.getTime() ){
                        idleTimeMs =  idleTimeMs + (state.getDate().getTime()-previous.getDate().getTime());
                    } else {
                        idleTimeMs =  idleTimeMs + (state.getDate().getTime()-fromDate.getTime());
                    }
                }
            }
            previous = state;
        }
        if (previous.getState().equals(Constants.CAB_STATE_IDLE) 
                && previous.getDate().getTime() < toDate.getTime()) {
            idleTimeMs = idleTimeMs + toDate.getTime() - previous.getDate().getTime();
        }
        result.put("units", "ms");
        result.put("time", String.valueOf(idleTimeMs));
        return result;
    }

    public Map<Long,HashMap<Integer,Integer>> getHighDemandStats() throws AppException {
        // cityid-> dateTime->count
        Map<Long,HashMap<Integer,Integer>> bookings = cabBookings.entrySet().stream().map(entry->entry.getValue()).collect(Collectors.toMap(
            Booking::getFromCityId,
            (booking) -> {
                HashMap<Integer,Integer> count = new HashMap<>();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(booking.getStartDate());
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                count.merge(hours, 1, (v11,v12)-> v11+v12);
                return count;
            },
            (v1,v2) -> {
                v2.forEach((key,value)->{
                    v1.merge(key, value, (v10,v20)-> v10+v20);
                });
                return v1;
            }
        ));
        return bookings;
    }

    public List<CabState> getCabStates(long cabId) throws AppException {
        if (!cabStates.containsKey(cabId)) {
            throw new AppException("Invalid cab id. Cab not registered");
        }
        return cabStates.get(cabId);
    }

    public List<Cab> listAll() {
        return new ArrayList<>(allCabs.values());
    }
}
