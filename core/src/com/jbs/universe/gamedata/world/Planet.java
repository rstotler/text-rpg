package com.jbs.universe.gamedata.world;

import com.jbs.universe.screen.console.ColorString;

import java.util.ArrayList;

public class Planet {
    public int galaxy;
    public int system;
    public int planet;
    public ArrayList<Area> areaList;

    public ColorString name;

    String type;
    long distanceFromSun;
    int diameter;
    float axialTilt;
    public String orbit;

    int minutesInDay;
    int minutesInYear;

    public int currentMinutesInDay;
    public int currentMinutesInYear;

    float dawnPercent;
    float sunsetPercent;

    public Planet(int galaxy, int system, int planet, ColorString name, String type, long distanceFromSun, int minutesInDay, int minutesInYear, int diameter, float axialTilt) {
        this.galaxy = galaxy;
        this.system = system;
        this.planet = planet;
        areaList = new ArrayList<Area>();

        this.name = name;

        this.type = type;
        this.distanceFromSun = distanceFromSun;
        this.diameter = diameter;
        this.axialTilt = axialTilt;
        orbit = "Counter Clockwise";

        this.minutesInDay = minutesInDay;
        this.minutesInYear = minutesInYear;

        currentMinutesInDay = 0;
        currentMinutesInYear = 0;
    }

    public boolean dayCheck() {
        boolean dayCheck = false;

        if(type.equals("Star")) {
            dayCheck = true;
        } else {
            dayCheck = currentMinutesInDay / (minutesInDay + .0) >= dawnPercent && currentMinutesInDay / (minutesInDay + .0) < sunsetPercent;
        }

        return dayCheck;
    }
}
