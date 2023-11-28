package com.jbs.universe.gamedata.world;

import com.jbs.universe.gamedata.world.Room.Room;
import com.jbs.universe.screen.console.ColorString;
import com.jbs.universe.screen.console.Console;

import java.util.Arrays;

public class Location {
    public int galaxy;
    public int system;
    public int planet;
    public int area;
    public int room;

    public Location(int galaxy, int system, int planet, int area, int room) {
        this.galaxy = galaxy;
        this.system = system;
        this.planet = planet;
        this.area = area;
        this.room = room;
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
}
