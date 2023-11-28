package com.jbs.universe.gamedata.world.SolarSystem;

import com.jbs.universe.gamedata.world.Planet.Planet;
import com.jbs.universe.gamedata.world.Star.Star;
import com.jbs.universe.screen.console.ColorString;

import java.util.ArrayList;

public class SolarSystem {
    public Star star;
    public ArrayList<Planet> planetList;

    public ColorString name;

    public SolarSystem(ColorString name, Star star) {
        this.star = star;
        planetList = new ArrayList<>();

        this.name = name;
    }
}
