package com.jbs.universe.gamedata.player;

import com.jbs.universe.gamedata.item.Item;
import com.jbs.universe.gamedata.mob.Mob;
import com.jbs.universe.gamedata.world.Location;
import com.jbs.universe.gamedata.world.galaxy.Galaxy;
import com.jbs.universe.gamedata.world.room.Room;
import com.jbs.universe.gamedata.world.spaceship.Spaceship;
import com.jbs.universe.screen.console.ColorString;
import com.jbs.universe.screen.console.Console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {
    public Location location;

    public ArrayList<Mob> targetList;
    public ArrayList<Mob> combatList;
    public ArrayList<Mob> groupList;

    public Map<String, ArrayList<Item>> itemList;

    public Player(Location location) {
        this.location = location;

        targetList = new ArrayList<>();
        combatList = new ArrayList<>();
        groupList = new ArrayList<>();

        itemList = new HashMap<String, ArrayList<Item>>() {{
           put("Misc.", new ArrayList<Item>());
        }};
    }

    // Action Functions //
    public void move(Console console, ArrayList<Galaxy> galaxyList, String inputDirection) {
        Room currentRoom = Room.getRoom(galaxyList, location);

        if(currentRoom.exit.get(inputDirection) == null) {
            console.write(new ColorString("You can't go that way!", "7w1y13w1y"), true);
        } else if(currentRoom.door.get(inputDirection) != null && !currentRoom.door.get(inputDirection).keyCheck(this)) {
            console.write(new ColorString("You lack the proper key.", "23w1y"), true);
        }

        else {
            location = currentRoom.exit.get(inputDirection);
            currentRoom = Room.getRoom(galaxyList, location);
            currentRoom.display(console, galaxyList, this);
        }
    }

    public void enterShip(Console console, ArrayList<Galaxy> galaxyList, String targetSpaceshipString) {
        Room currentRoom = Room.getRoom(galaxyList, location);

        Spaceship targetSpaceship = null;
        for(Spaceship spaceship : currentRoom.spaceshipList) {
            if(spaceship.keyList.contains(targetSpaceshipString)) {
                targetSpaceship = spaceship;
                break;
            }
        }

        if(targetSpaceship == null) {
            console.write(new ColorString("You don't see it.", "7w1y8w1y"), true);
        } else if(!targetSpaceship.password.isEmpty() && !hasKey(targetSpaceship.password)) {
            console.write(new ColorString("You lack the proper key.", "23w1y"), true);
        } else if(!targetSpaceship.outsideEntranceLocationList.contains(currentRoom.location)) {
            console.write(new ColorString("You don't see an entrance here.", "7w1y22w1y"), true);
        }

//        else if(targetSpaceship.status.equals("Launching")) {
//            console.write(new ColorString("You can't do that while the ship is launching!", "7w1y37w1y"), true);
//        }

        else {
            for(int i = 0; i < targetSpaceship.outsideEntranceLocationList.size(); i++) {
                Location entranceLocation = targetSpaceship.outsideEntranceLocationList.get(i);
                if(currentRoom.location.equals(entranceLocation)) {
                    entranceLocation = targetSpaceship.innerEntranceLocationList.get(i);
                    location = entranceLocation;

                    console.write(new ColorString("The hatch opens and closes as you step inside.", "45w1y"), true);

                    Room entranceRoom = Room.getRoom(galaxyList, entranceLocation);
                    entranceRoom.display(console, galaxyList, this);
                    break;
                }
            }
        }
    }

    // Utility Functions //
    public boolean hasKey(String targetPassword) {
        for(String pocketString : itemList.keySet()) {
            for(Item item : itemList.get(pocketString)) {
                if(item.passwordList.contains(targetPassword)) {
                    return true;
                }
            }
        }
        return false;
    }
}
