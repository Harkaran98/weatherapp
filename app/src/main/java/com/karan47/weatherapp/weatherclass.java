package com.karan47.weatherapp;

public class weatherclass {
    String main;
    String description;

    @Override
    public String toString() {
        return "Current Weather : " +main +
                "\nDescription : " + description;
    }
}
