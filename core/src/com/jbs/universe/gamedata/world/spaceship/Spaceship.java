package com.jbs.universe.gamedata.world.spaceship;

import com.jbs.universe.gamedata.world.Location;
import com.jbs.universe.gamedata.world.area.Area;
import com.jbs.universe.gamedata.world.galaxy.Galaxy;
import com.jbs.universe.gamedata.world.room.Room;
import com.jbs.universe.gamedata.world.solarsystem.SolarSystem;
import com.jbs.universe.screen.components.Plane;
import com.jbs.universe.screen.components.Point;
import com.jbs.universe.screen.console.ColorString;
import com.jbs.universe.screen.console.Console;

import java.util.ArrayList;

import static com.jbs.universe.components.Utility.createKeyList;

public class Spaceship {
    static int numCount = 0;

    public int num;
    public ArrayList<Area> areaList;

    public Location location;

    public String password;

    public Plane landedSize;
    public ArrayList<Point> entranceCoordinateList;
    public ArrayList<Location> outsideEntranceLocationList;
    public ArrayList<Location> innerEntranceLocationList; // entranceCoordinateList [Parallel Array]

    public ColorString name;
    public ArrayList<String> keyList;

    public Spaceship(ColorString name, String password) {
        this.num = numCount++;
        areaList = new ArrayList<>();

        location = new Location(-1, -1, -1, -1, -1);

        this.password = password;

        landedSize = new Plane(1, 2);
        entranceCoordinateList = new ArrayList<>();
        entranceCoordinateList.add(new Point(0, 1));

        outsideEntranceLocationList = new ArrayList<>();
        innerEntranceLocationList = new ArrayList<>();

        this.name = name;
        keyList = createKeyList(name.label);
    }

    public static Spaceship create(ColorString name, String password) {
        Spaceship spaceship = new Spaceship(name, password);

        ColorString areaSpaceshipName = new ColorString("Main Area", "5shim-w4shim-w");
        Area areaMainArea = new Area(areaSpaceshipName);
        spaceship.areaList.add(areaMainArea);

        ColorString room00Name = new ColorString("A Ship Cabin", "2w5shim-w5shim-w");
        Location location00 = new Location(0, 0, spaceship);
        Room room00 = new Room(room00Name, null, location00);
        areaMainArea.roomList.add(room00);

        ColorString room01Name = new ColorString("A Cockpit", "2w7shim-w");
        Location location01 = new Location(0, 1, spaceship);
        Room room01 = new Room(room01Name, null, location01);
        areaMainArea.roomList.add(room01);
        room01.makeExit("South", room00, true);

        spaceship.innerEntranceLocationList.add(location00);

        return spaceship;
    }

    public void display(Console console) {
        String displayString = "A " + name.label + " is sitting on the launch pad.";
        String displayColorCode = "2w" + name.colorCode + "29w1y";

        console.write(new ColorString(displayString, displayColorCode), false);
    }

    public void park(ArrayList<Galaxy> galaxyList, Room targetRoom) {
        // targetRoom - Represents Top-Left Corner Of Parking Spot //

        // Temp? - Update Galaxy & System Location //
        location = new Location(targetRoom.location.galaxy, targetRoom.location.system, targetRoom.location.planet, location);

        Room loopRoom = targetRoom;
        Room yRoom = targetRoom;

        // Set Outside Entrance Coordinates //
        outsideEntranceLocationList.clear();
        for(int y = 0; y < landedSize.height; y++) {
            for(int x = 0; x < landedSize.width; x++) {
                if(!loopRoom.spaceshipList.contains(this)) {
                    loopRoom.spaceshipList.add(this);

                    for(Point entranceCoordinate : entranceCoordinateList) {
                        if(entranceCoordinate.x == x && entranceCoordinate.y == y) {
                            outsideEntranceLocationList.add(loopRoom.location);
                            break;
                        }
                    }
                }
                if(loopRoom.exit.get("East") != null) {
                    loopRoom = Room.getRoom(galaxyList, loopRoom.exit.get("East"));
                }
            }
            if(yRoom.exit.get("South") != null) {
                loopRoom = Room.getRoom(galaxyList, yRoom.exit.get("South"));
            } else {
                break;
            }
        }

        // Set Inner Exits //
        for(int i = 0; i < innerEntranceLocationList.size(); i++) {
            Location innerLocation = innerEntranceLocationList.get(i);
            Room innerRoom = Room.getRoom(galaxyList, innerLocation);
            Location outsideLocation = outsideEntranceLocationList.get(i);
            innerRoom.exit.put("West", outsideLocation);
        }
    }
}
