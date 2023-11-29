package com.jbs.universe.gamedata.world.planet;

import com.jbs.universe.gamedata.world.area.Area;
import com.jbs.universe.screen.components.PointF;
import com.jbs.universe.screen.console.ColorString;

import java.util.ArrayList;

public class Planet {
    public ArrayList<Area> areaList;

    public ColorString name;

    PointF location;

    int minutesInDay;
    int minutesInYear;
    int totalMinutesInDay;
    int totalMinutesInYear;

    int distanceFromSun;
    int diameter;
    float axialTilt;
    int orbitType;

    public Planet(ColorString name, int distanceFromSun, int totalMinutesInDay, int totalMinutesInYear, float axialTilt, int diameter) {
        areaList = new ArrayList<>();

        this.name = name;

        location = new PointF(0, 0);

        this.totalMinutesInDay = totalMinutesInDay;
        this.totalMinutesInYear = totalMinutesInYear;
        minutesInDay = 0;
        minutesInYear = 0;

        this.distanceFromSun = distanceFromSun;
        this.axialTilt = axialTilt;
        this.diameter = diameter;
        orbitType = -1; // CCW Orbit
    }

    public void update() {
        minutesInDay++;
        minutesInYear++;

        updatePosition();
        if(minutesInDay >= totalMinutesInDay) {
            minutesInDay = 0;
            updateNightDayTimers();
        }
    }

    public void updatePosition() {
        if(minutesInYear != 0) {
            location.x = (float) Math.cos(Math.toRadians(((minutesInYear + .0) / totalMinutesInYear) * 360)) * distanceFromSun;
            location.y = (float) (((Math.sin(Math.toRadians(((minutesInYear + .0) / totalMinutesInYear) * 360)) * distanceFromSun) / 1.4) * orbitType);
        }
    }

    public void updateNightDayTimers() {

    }
}
