package com.jbs.universe.gamedata.world.area;

import com.jbs.universe.gamedata.world.room.Room;
import com.jbs.universe.screen.console.ColorString;

import java.util.ArrayList;

public class Area {
    public ArrayList<Room> roomList;

    public ColorString name;

    public Area(ColorString name) {
        roomList = new ArrayList<>();

        this.name = name;
    }
}
