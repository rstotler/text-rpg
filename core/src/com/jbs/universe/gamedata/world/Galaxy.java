package com.jbs.universe.gamedata.world;

import com.jbs.universe.screen.console.ColorString;

import java.util.ArrayList;

public class Galaxy {
    public ArrayList<SolarSystem> systemList;

    public ColorString name;

    public Galaxy(ColorString name) {
        systemList = new ArrayList<SolarSystem>();

        this.name = name;
    }
}
