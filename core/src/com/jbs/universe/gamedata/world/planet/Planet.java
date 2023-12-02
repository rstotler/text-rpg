package com.jbs.universe.gamedata.world.planet;

import com.jbs.universe.gamedata.player.Player;
import com.jbs.universe.gamedata.world.Location;
import com.jbs.universe.gamedata.world.area.Area;
import com.jbs.universe.gamedata.world.galaxy.Galaxy;
import com.jbs.universe.gamedata.world.room.Room;
import com.jbs.universe.screen.components.PointF;
import com.jbs.universe.screen.console.ColorString;
import com.jbs.universe.screen.console.Console;

import java.util.ArrayList;

public class Planet {
    public Location location;
    public ArrayList<Area> areaList;

    public ColorString name;

    PointF coordinates;

    public int minutesInDay;
    public int minutesInYear;
    int totalMinutesInDay;
    int totalMinutesInYear;

    float dawnPercent;
    float sunrisePercent;
    float noonPercent;
    float duskPercent;
    float sunsetPercent;

    boolean dawnMessage;
    boolean sunriseMessage;
    boolean noonMessage;
    boolean duskMessage;
    boolean sunsetMessage;

    int distanceFromSun;
    int diameter;
    float axialTilt;
    int orbitType;

    public Planet(ColorString name, Location location, int distanceFromSun, int totalMinutesInDay, int totalMinutesInYear, float axialTilt, int diameter) {
        this.location = location;
        areaList = new ArrayList<>();

        this.name = name;

        coordinates = new PointF(0, 0);

        this.totalMinutesInDay = totalMinutesInDay;
        this.totalMinutesInYear = totalMinutesInYear;
        minutesInDay = 0;
        minutesInYear = 0;

        this.distanceFromSun = distanceFromSun;
        this.axialTilt = axialTilt;
        this.diameter = diameter;
        orbitType = -1; // CCW Orbit
    }

    public void update(Console console, Player player) {
        minutesInDay++;
        minutesInYear++;

        updatePosition();
        displayMessageCheck(console, player);

        if(minutesInDay >= totalMinutesInDay) {
            minutesInDay = 0;
            updateNightDayTimers();
        }
        if(minutesInYear >= totalMinutesInYear) {
            minutesInYear = 0;
        }
    }

    // Utility Functions //
    public void updatePosition() {
        if(minutesInYear != 0) {
            coordinates.x = (float) Math.cos(Math.toRadians(((minutesInYear + .0) / totalMinutesInYear) * 360)) * distanceFromSun;
            coordinates.y = (float) (((Math.sin(Math.toRadians(((minutesInYear + .0) / totalMinutesInYear) * 360)) * distanceFromSun) / 1.4) * orbitType);
        } else {
            coordinates.x = distanceFromSun + .0f;
            coordinates.y = 0.0f;
        }
    }

    public void displayMessageCheck(Console console, Player player) {
        if(player.location.planet == location.planet || (player.location.spaceshipObject != null && player.location.spaceshipObject.location.planet == location.planet)) {
            float dayPercent = (float) (minutesInDay / (totalMinutesInDay + .0));

            if(dayPercent >= dawnPercent && !dawnMessage) {
                dawnMessage = true;
                console.write(new ColorString("The sky begins to lighten.", "4w1dc2ddc11w1dw6ddw1y"), true);
            } else if(dayPercent >= sunrisePercent && !sunriseMessage) {
                sunriseMessage = true;
                console.write(new ColorString("The sun rises over the horizon.", "4w1dy2ddy16w1dw6ddw1y"), true);
            } else if(dayPercent >= noonPercent && !noonMessage) {
                noonMessage = true;
                console.write(new ColorString("It's noon.", "2w1y6w1y"), true);
            } else if(dayPercent >= duskPercent && !duskMessage) {
                duskMessage = true;
                console.write(new ColorString("The sun begins to set.", "4w1dy2ddy11w1dw2ddw1y"), true);
            } else if(dayPercent >= sunsetPercent && !sunsetMessage) {
                sunsetMessage = true;
                console.write(new ColorString("The sun sinks beyond the horizon.", "4w1dy2ddy18w1dw6ddw1y"), true);
            }
        }
    }

    public void updateNightDayTimers() {
        float yearRatio = 0.0f;
        if(minutesInYear > 0) {
            yearRatio = (float) (Math.cos(Math.toRadians((minutesInYear / (totalMinutesInYear + .0))) * 360));
        }
        float ratio = (float) (((axialTilt / 100.0) * yearRatio) * (totalMinutesInYear / 2.50));
        float nightMinutes = (float) (totalMinutesInYear - ((totalMinutesInYear / 1.9) - ratio));

        dawnPercent = (float) ((nightMinutes / 2.01) / totalMinutesInYear);
        sunrisePercent = (float) ((nightMinutes / 1.80) / totalMinutesInYear);
        noonPercent = .5f;
        duskPercent = (float) (((nightMinutes / 1.94) + (totalMinutesInYear - nightMinutes)) / totalMinutesInYear);
        sunsetPercent = (float) (((nightMinutes / 1.64) + (totalMinutesInYear - nightMinutes)) / totalMinutesInYear);

        dawnMessage = minutesInDay / (totalMinutesInDay + .0) >= dawnPercent;
        sunriseMessage = minutesInDay / (totalMinutesInDay + .0) >= sunrisePercent;
        noonMessage = minutesInDay / (totalMinutesInDay + .0) >= noonPercent;
        duskMessage = minutesInDay / (totalMinutesInDay + .0) >= duskPercent;
        sunsetMessage = minutesInDay / (totalMinutesInDay + .0) >= sunsetPercent;
    }

    // Getters & Setters //
    public static Planet getPlanet(ArrayList<Galaxy> galaxyList, Location location) {
        if(location.spaceshipObject == null) {
            return galaxyList.get(location.galaxy).systemList.get(location.system).planetList.get(location.planet);
        } else if(location.spaceshipObject.location.planet != -1) {
            return galaxyList.get(location.spaceshipObject.location.galaxy).systemList.get(location.spaceshipObject.location.system).planetList.get(location.spaceshipObject.location.planet);
        }
        return galaxyList.get(0).systemList.get(0).planetList.get(0);
    }

    public String getDayPeriod() {
        double dayPercent = minutesInDay / (totalMinutesInDay + .0);

        if(dayPercent < dawnPercent || dayPercent >= sunsetPercent) {
            return "Night";
        } else if(dayPercent < sunrisePercent) {
            return "Dawn";
        } else if(dayPercent < duskPercent) {
            return "Day";
        } else {
            return "Dusk";
        }
    }
}
