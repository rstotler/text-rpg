package com.jbs.universe.gamedata.world.Planet;

import com.jbs.universe.gamedata.world.Area.Area;
import com.jbs.universe.screen.console.ColorString;

import java.util.ArrayList;

public class Planet {
    public ArrayList<Area> areaList;

    public ColorString name;

    public Planet(ColorString name) {
        areaList = new ArrayList<>();

        this.name = name;
    }
}
