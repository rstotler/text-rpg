package com.jbs.universe.gamedata.world.Room;

import com.jbs.universe.gamedata.world.Galaxy.Galaxy;
import com.jbs.universe.gamedata.world.Location;
import com.jbs.universe.screen.console.ColorString;
import com.jbs.universe.screen.console.Console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Room {
    public Map<String, Location> exit;

    public ColorString name;
    public ColorString description;

    public Room(ColorString name, ColorString description) {
        exit = new HashMap<>();
        for(String exitString : Arrays.asList("North", "East", "South", "West", "Up", "Down")) {
            exit.put(exitString, null);
        }

        this.name = name;
        this.description = description;
    }

    public void display(Console console, ArrayList<Galaxy> galaxyList) {
        console.write(name, true);
        console.write(console.getUnderlineColorString(name.label), false);
        if(description != null) {
            console.write(description, false);
        }

        for(String exitString : Arrays.asList("North", "East", "South", "West", "Up", "Down")) {
            if(!((exitString.equals("Up") || exitString.equals("Down")) && exit.get(exitString) == null)) {
                Room exitRoom = null;
                if(exit.get(exitString) != null) {
                    exitRoom = Room.getRoom(galaxyList, exit.get(exitString));
                }
                Location.display(console, exitRoom, exitString);
            }
        }
    }

    // Utility Functions //
    public static Room getRoom(ArrayList<Galaxy> galaxyList, Location location) {
        if(galaxyList.size() > location.galaxy
        && galaxyList.get(location.galaxy).systemList.size() > location.system
        && galaxyList.get(location.galaxy).systemList.get(location.system).planetList.size() > location.planet
        && galaxyList.get(location.galaxy).systemList.get(location.system).planetList.get(location.planet).areaList.size() > location.area
        && galaxyList.get(location.galaxy).systemList.get(location.system).planetList.get(location.planet).areaList.get(location.area).roomList.size() > location.room) {
            return galaxyList.get(location.galaxy).systemList.get(location.system).planetList.get(location.planet).areaList.get(location.area).roomList.get(location.room);
        }

        return galaxyList.get(0).systemList.get(0).planetList.get(0).areaList.get(0).roomList.get(0);
    }
}
