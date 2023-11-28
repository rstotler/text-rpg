package com.jbs.universe.gamedata.world.Galaxy;

import com.jbs.universe.gamedata.world.SolarSystem.SolarSystem;
import com.jbs.universe.screen.console.ColorString;

import java.util.ArrayList;

public class Galaxy {
    public ArrayList<SolarSystem> systemList;

    ColorString name;

    public Galaxy(ColorString name) {
        systemList = new ArrayList<>();

        this.name = name;
    }
}
