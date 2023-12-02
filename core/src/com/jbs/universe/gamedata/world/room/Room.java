package com.jbs.universe.gamedata.world.room;

import com.jbs.universe.gamedata.item.Item;
import com.jbs.universe.gamedata.mob.Mob;
import com.jbs.universe.gamedata.player.Player;
import com.jbs.universe.gamedata.world.Location;
import com.jbs.universe.gamedata.world.galaxy.Galaxy;
import com.jbs.universe.gamedata.world.planet.Planet;
import com.jbs.universe.gamedata.world.spaceship.Spaceship;
import com.jbs.universe.screen.console.ColorString;
import com.jbs.universe.screen.console.Console;
import com.jbs.universe.screen.console.DisplayLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.jbs.universe.components.Utility.getOppositeDirection;

public class Room {
    public Location location;
    public Map<String, Location> exit;

    public ArrayList<Item> itemList;
    public ArrayList<Mob> mobList;
    public ArrayList<Spaceship> spaceshipList;

    public ColorString name;
    public ColorString description;

    public Room(ColorString name, ColorString description, Location location) {
        this.location = location;

        exit = new HashMap<>();
        for(String exitString : Arrays.asList("North", "East", "South", "West", "Up", "Down")) {
            exit.put(exitString, null);
        }

        itemList = new ArrayList<>();
        mobList = new ArrayList<>();
        spaceshipList = new ArrayList<>();

        this.name = name;
        this.description = description;
    }

    public void display(Console console, ArrayList<Galaxy> galaxyList, Player player) {
        if(isLit(galaxyList)) {
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

            for(DisplayLine displayLine : DisplayLine.quantifyItemList(itemList)) {
                displayLine.display(console);
            }

            for(DisplayLine displayLine : DisplayLine.quantifyMobList(mobList, player)) {
                displayLine.display(console);
            }

            for(Spaceship spaceship : spaceshipList) {
                spaceship.display(console);
            }
        }

        else {
            displayDark(console, galaxyList);
        }
    }

    public void displayDark(Console console, ArrayList<Galaxy> galaxyList) {

    }

    // Utility Functions //
    public static Room getRoom(ArrayList<Galaxy> galaxyList, Location location) {
        if(location.spaceshipObject == null) {
            if(galaxyList.size() > location.galaxy
            && galaxyList.get(location.galaxy).systemList.size() > location.system
            && galaxyList.get(location.galaxy).systemList.get(location.system).planetList.size() > location.planet
            && galaxyList.get(location.galaxy).systemList.get(location.system).planetList.get(location.planet).areaList.size() > location.area
            && galaxyList.get(location.galaxy).systemList.get(location.system).planetList.get(location.planet).areaList.get(location.area).roomList.size() > location.room) {
                return galaxyList.get(location.galaxy).systemList.get(location.system).planetList.get(location.planet).areaList.get(location.area).roomList.get(location.room);
            }
        } else {
            if(location.spaceshipObject.areaList.size() > location.area && location.spaceshipObject.areaList.get(location.area).roomList.size() > location.room) {
                return location.spaceshipObject.areaList.get(location.area).roomList.get(location.room);
            }
        }

        return galaxyList.get(0).systemList.get(0).planetList.get(0).areaList.get(0).roomList.get(0);
    }

    public boolean isInside() {
        return false;
    }

    public boolean isLit(ArrayList<Galaxy> galaxyList) {
        Room currentRoom = Room.getRoom(galaxyList, location);

        if(location.spaceshipObject == null) {
            if(!currentRoom.isInside()) {
                Planet targetPlanet = Planet.getPlanet(galaxyList, location);
                return !targetPlanet.getDayPeriod().equals("Night");
            }
        }

        return false;
    }

    public void makeExit(String targetDirection, Room targetRoom, boolean makeOppositeExit) {
        exit.put(targetDirection, targetRoom.location);
        if(makeOppositeExit) {
            String oppositeDirection = getOppositeDirection(targetDirection);
            targetRoom.exit.put(oppositeDirection, location);
        }
    }
}
