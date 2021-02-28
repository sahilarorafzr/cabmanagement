package com.example.restservice;

import com.example.service.CabService;
import com.example.exceptions.AppException;
import com.example.entity.Booking;
import com.example.entity.Cab;
import com.example.entity.CabState;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

@RestController
public class CabController {

	@Autowired	
	CabService cabService;

	@RequestMapping(value = "/registerCab", method = RequestMethod.POST)
	public Map<String, String>  registerCab(@RequestBody Map<String, Object> payload) throws AppException {
		long id = cabService.registerCab(payload);
		Map<String, String> result = new HashMap<>();
		result.put("cabId", String.valueOf(id));
		result.put("status", "registered");
		return result;
	}

	@RequestMapping(value = "/updateCabLocation", method = RequestMethod.PUT)
	public Map<String, String> updateCabLocation(@RequestBody Map<String, Object> payload) throws AppException{
		Map<String, String> result = new HashMap<>();
		cabService.updateCabLocation(payload);
		result.put("status", "updated");	
		return result;
	}

	@RequestMapping(value = "/updateCabDuty", method = RequestMethod.PUT)
	public Map<String, String> updateCabDuty(@RequestBody Map<String, Object> payload) throws AppException{
		Map<String, String> result = new HashMap<>();
		cabService.updateCabState(payload);
		result.put("status", "updated");	
		return result;
	}

	@RequestMapping(value = "/book", method = RequestMethod.POST)
	public Map<String, Object> book(@RequestBody Map<String, Object> payload) throws AppException{
		Map<String, Object> result = new HashMap<>();
		Booking booking = cabService.book(payload);
		result.put("booking", booking);
		result.put("status", "booked");
		return result;
	}

	@RequestMapping(value = "/endBooking", method = RequestMethod.POST)
	public Map<String, Object> endBooking(@RequestBody Map<String, Object> payload) throws AppException{
		Map<String, Object> result = new HashMap<>();
		Booking booking = cabService.endBooking(payload);
		result.put("booking", booking);
		result.put("status", "trip_ended");
		return result;
	}

	@GetMapping("/getCabIdleTime")
	public Map<String, Object> getCabIdleTime(@RequestParam(value = "cabId") long cabId,
			@RequestParam(value = "from") @DateTimeFormat(pattern="yyyy-MM-dd-HH-mm")  Date fromDate,
			@RequestParam(value = "to") @DateTimeFormat(pattern="yyyy-MM-dd-HH-mm")  Date toDate) throws AppException{
		Map<String, Object> result = new HashMap<>();
		Map<String, String> idleTime = cabService.getCabIdleTime(cabId, fromDate, toDate);
		result.put("idleTime", idleTime);
		result.put("status", "success");
		return result;
	}

	@GetMapping("/getCityDemandStats")
	public Map<Long,HashMap<Integer,Integer>> getHighDemandStats() throws AppException {
		Map<Long,HashMap<Integer,Integer>> stats = cabService.getHighDemandStats();
		return stats;
	}

	@GetMapping("/getCabStates")
	public HashMap<String, Object> getCabStates(@RequestParam(value = "cabId") long cabId) throws AppException {
		List<CabState> states = cabService.getCabStates(cabId);
		HashMap<String, Object> result = new HashMap<>();
		result.put("status", "success");
		result.put("states", states);
 		return result;
	}
	
	@GetMapping("/listAllCabs")
	public Map<String, Object> listAllCabs() throws AppException{
		Map<String, Object> result = new HashMap<>();
		List<Cab> cabs = cabService.listAll();
		result.put("cabs", cabs);
		result.put("status", "success");
		return result;
	}

}
