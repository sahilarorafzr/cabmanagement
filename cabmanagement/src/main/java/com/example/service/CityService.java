package com.example.service;

import com.example.exceptions.AppException;

import com.example.dao.CityDao;
import com.example.entity.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CityService {
    
    @Autowired
    CityDao cityDao;

    public long registerCity(String city) throws AppException{
        return cityDao.save(city);
    }

    /**
     * checks if new city is valid to be registered
     */
    public boolean validateNewCity(String city) {
        if (null != city && !city.equals("") && !cityDao.exists(city)) {
            return true;
        }
        return false;
    }

    public boolean validateCity(long cityId) {
        return cityDao.existsCityId(cityId);
    }

    public List<City> listAll() {
        return cityDao.listAll();
    }
}
