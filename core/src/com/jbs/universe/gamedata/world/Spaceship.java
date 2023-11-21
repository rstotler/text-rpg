package com.jbs.universe.gamedata.world;

import com.jbs.universe.gamedata.world.Room.Exit;
import com.jbs.universe.gamedata.world.Room.Room;
import com.jbs.universe.screen.console.ColorString;

import java.util.ArrayList;

public class Spaceship {
    public int galaxy;
    public int system;
    public int planet;
    public int num;

    public ColorString name;
    public ArrayList<Area> areaList;

    public int[] landedLocation; // 0 - Area, 1 - Room

    public Spaceship(ColorString name, Exit landedLocation) {
        galaxy = landedLocation.galaxy;
        system = landedLocation.system;
        planet = landedLocation.planet;

        this.name = name;
        areaList = new ArrayList<Area>();

        this.landedLocation = new int[] {landedLocation.area, landedLocation.room};
    }

    public Room getRoom(int area, int room) {
        if(area <= areaList.size()) {
            if(room <= areaList.get(area).roomList.size()) {
                return areaList.get(area).roomList.get(room);
            }
        }
        return null;
    }
}
