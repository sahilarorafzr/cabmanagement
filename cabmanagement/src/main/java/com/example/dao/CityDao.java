package com.example.dao;

import com.example.entity.City;
import com.example.exceptions.AppException;

import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class CityDao {

    private long lastUsedId = 0;
    private Map<Long, City> registeredCities = new HashMap<>();
    private Map<String, City> registeredCitiesByName = new HashMap<>();
    
    public long save(String cityName) throws AppException{
        if (exists(cityName)) {
            throw new AppException("City already registered");
        }
        long newId = ++lastUsedId;
        City newCity = new City(newId, cityName);
        registeredCities.put(newId, newCity);
        registeredCitiesByName.put(cityName, newCity);
        return newId;
    }

    public boolean exists(String cityName) {
        return registeredCitiesByName.containsKey(cityName);
    }

    public boolean existsCityId(long cityId) {
        return registeredCities.containsKey(cityId);
    }

    public List<City> listAll() {
        return new ArrayList<>(registeredCities.values());
    }
}
