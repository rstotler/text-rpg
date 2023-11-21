package com.jbs.universe.gamedata.world;

import com.jbs.universe.gamedata.world.Room.Room;
import com.jbs.universe.screen.console.ColorString;

import java.util.ArrayList;

public class Area {
    public int num;
    public ArrayList<Room> roomList;

    public ColorString name;

    public Area(int num, ColorString name) {
        this.num = num;
        roomList = new ArrayList<Room>();

        this.name = name;
    }
}
