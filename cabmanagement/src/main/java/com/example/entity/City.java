package com.example.entity;

public class City {
    
    private long id;
    private String city;

    public City(long id, String city) {
        this.id= id;
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }
}
