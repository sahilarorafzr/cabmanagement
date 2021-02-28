package com.example.restservice;

import com.example.exceptions.AppException;

import com.example.entity.City;
import com.example.service.CityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@RestController
public class CityController {

	@Autowired	
	CityService cityService;

	@RequestMapping(value = "/registerCity", method = RequestMethod.POST)
	public Map<String, String> registerCity(@RequestBody Map<String, Object> payload) throws AppException{
		String cityName = (String)payload.get("city");
		long registeredCityId = 0;
		Map<String, String> result = new HashMap<>();
		if (cityService.validateNewCity(cityName)) {
			registeredCityId = cityService.registerCity(cityName);
			result.put("cityId", String.valueOf(registeredCityId));
			result.put("status", "registered");
		} else {
			result.put("status", "already registered in system");
		}		
		return result;
	}

	@GetMapping("/listAllCities")
	public Map<String, Object> listAllCities() throws AppException{
		Map<String, Object> result = new HashMap<>();
		List<City> cities = cityService.listAll();
		result.put("cities", cities);
		result.put("status", "success");
		return result;
	}
}
