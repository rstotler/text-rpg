package com.jbs.universe.gamedata.world;

import com.jbs.universe.gamedata.GameObject;
import com.jbs.universe.gamedata.world.Room.Room;
import com.jbs.universe.screen.console.ColorString;

import java.util.ArrayList;

public class Area implements GameObject {
    public int num;
    public ArrayList<Room> roomList;

    public ColorString name;

    public int flavorTextTimerMax;
    public int flavorTextTimer;

    public Area(int num, ColorString name) {
        this.num = num;
        roomList = new ArrayList<Room>();

        this.name = name;

        flavorTextTimerMax = 400;
        flavorTextTimer = 0;
    }
}
