package com.jbs.universe.gamedata.world;

import com.jbs.universe.gamedata.world.room.Room;
import com.jbs.universe.gamedata.world.spaceship.Spaceship;
import com.jbs.universe.screen.console.ColorString;
import com.jbs.universe.screen.console.Console;

public class Location {
    public final int galaxy;
    public final int system;
    public final int planet;
    public final int area;
    public final int room;

    public final Spaceship spaceshipObject;

    public Location(int galaxy, int system, int planet, int area, int room) {
        this.galaxy = galaxy;
        this.system = system;
        this.planet = planet;
        this.area = area;
        this.room = room;

        spaceshipObject = null;
    }

    public Location(int area, int room, Spaceship spaceship) {
        galaxy = -1;
        system = -1;
        planet = -1;
        this.area = area;
        this.room = room;

        spaceshipObject = spaceship;
    }

    public static void display(Console console, Room targetRoom, String directionString) {
        String spaceString = "";
        String spaceColorCode = "";
        if(directionString.length() < 5) {
            for(int i = 0; i < 5 - directionString.length(); i++) {
                spaceString += " ";
            }
            spaceColorCode = spaceString.length() + "w";
        }

        String displayStringPrefix = "( " + spaceString + directionString + " ) - ";
        String displayColorCodePrefix = "2r" + spaceColorCode + String.valueOf(directionString.length()) + "shim-w" + "2r3dy";

        String displayStringRoomName = "";
        String displayColorCodeRoomName = "";

        if(targetRoom == null) {
            displayStringRoomName = "( Nothing )";
            displayColorCodeRoomName = "2r7shim-w2r";
        } else {
            displayStringRoomName = targetRoom.name.label;
            displayColorCodeRoomName = targetRoom.name.colorCode;
        }

        console.write(new ColorString(displayStringPrefix + displayStringRoomName, displayColorCodePrefix + displayColorCodeRoomName), false);
    }

    public boolean equals(Location location) {
        return this.galaxy == location.galaxy
            && this.system == location.system
            && this.planet == location.planet
            && this.area == location.area
            && this.room == location.room
            && this.spaceshipObject == location.spaceshipObject;
    }

    public String toString() {
        String spaceshipNum = "None";
        if(spaceshipObject != null) {
            spaceshipNum = String.valueOf(spaceshipObject.num);
        }
        return "[" + galaxy + ", " + system + ", " + planet + ", " + area + ", " + room + ", " + spaceshipNum + "]";
    }
}
