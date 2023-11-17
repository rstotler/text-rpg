package com.jbs.universe.gamedata.world;

import java.util.ArrayList;

public class Planet {
    public ArrayList<Area> areaList;

    String type;

    int minutesInDay;

    int currentMinutesInDay;

    float dawnPercent;
    float sunsetPercent;

    public Planet() {
        areaList = new ArrayList<Area>();
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
