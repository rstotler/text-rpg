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
    public Map<String, Door> door;

    public ArrayList<Mob> mobList;
    public ArrayList<Item> itemList;
    public ArrayList<Spaceship> spaceshipList;

    public ColorString name;
    public ColorString description;

    public Room(ColorString name, ColorString description, Location location) {
        this.location = location;

        exit = new HashMap<>();
        door = new HashMap<>();
        for(String exitString : Arrays.asList("North", "East", "South", "West", "Up", "Down")) {
            exit.put(exitString, null);
            door.put(exitString, null);
        }

        mobList = new ArrayList<>();
        itemList = new ArrayList<>();
        spaceshipList = new ArrayList<>();

        this.name = name;
        this.description = description;
    }

    public void display(Console console, ArrayList<Galaxy> galaxyList, Player player) {
        if(isLit(galaxyList)) {
            ColorString nameColorString = new ColorString(name.label, name.colorCode);
            if(isInside()) {
                nameColorString.label = "(Inside) " + nameColorString.label;
                nameColorString.colorCode = "1r1w5ddw2r" + nameColorString.colorCode;
            }
            console.write(nameColorString, false);
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
                    if(exitRoom == null || exitRoom.isLit(galaxyList)) {
                        Location.display(console, exitRoom, exitString);
                    } else {
                        Location.displayDark(console, exitRoom, exitString);
                    }
                }
            }

            for(DisplayLine displayLine : DisplayLine.quantifyMobList(mobList, player)) {
                displayLine.display(console);
            }

            for(DisplayLine displayLine : DisplayLine.quantifyItemList(itemList)) {
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
        ColorString nameColorString = new ColorString("Darkness", "8grad-a");
        if(isInside()) {
            nameColorString.label = "(Inside) " + nameColorString.label;
            nameColorString.colorCode = "1r1w5ddw2r" + nameColorString.colorCode;
        }
        console.write(new ColorString(nameColorString.label, nameColorString.colorCode), false);
        console.write(console.getUnderlineColorString(nameColorString.label), false);

        for(String exitString : Arrays.asList("North", "East", "South", "West", "Up", "Down")) {
            if(!((exitString.equals("Up") || exitString.equals("Down")) && exit.get(exitString) == null)) {
                Room exitRoom = null;
                if(exit.get(exitString) != null) {
                    exitRoom = Room.getRoom(galaxyList, exit.get(exitString));
                }
                if(exitRoom != null && exitRoom.isLit(galaxyList)) {
                    Location.display(console, exitRoom, exitString);
                } else {
                    Location.displayDark(console, exitRoom, exitString);
                }
            }
        }

        if(!mobList.isEmpty()) {
            ColorString countColorString = new ColorString("", "");
            if(mobList.size() > 1) {
                countColorString.label = " (" + mobList.size() + ")";
                countColorString.colorCode = "2r" + String.valueOf(mobList.size()).length() + "w1r";
            }
            console.write(new ColorString("Someone is here." + countColorString.label, "8shim-w7w1y" + countColorString.colorCode), false);
        }
        if(!itemList.isEmpty()) {
            ColorString countColorString = new ColorString("", "");
            if(itemList.size() > 1) {
                countColorString.label = " (" + itemList.size() + ")";
                countColorString.colorCode = "2r" + String.valueOf(itemList.size()).length() + "w1r";
            }
            console.write(new ColorString("Something is on the ground." + countColorString.label, "10shim-w16w1y" + countColorString.colorCode), false);
        }
        if(!spaceshipList.isEmpty()) {
            ColorString countColorString = new ColorString("", "");
            if(spaceshipList.size() > 1) {
                countColorString.label = " (" + spaceshipList.size() + ")";
                countColorString.colorCode = "2r" + String.valueOf(spaceshipList.size()).length() + "w1r";
            }
            console.write(new ColorString("A Spaceship is sitting on the launch pad." + countColorString.label, "2w10shim-w28w1y" + countColorString.colorCode), false);
        }
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
        return location.spaceshipObject != null;
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

    public void makeDoor(String targetDirection, Room targetRoom, String type, String password) {
        Door newDoor = new Door(type, password);
        door.put(targetDirection, newDoor);
        if(targetRoom != null) {
            String oppositeDirection = getOppositeDirection(targetDirection);
            targetRoom.door.put(oppositeDirection, newDoor);
        }
    }
}
