package com.jbs.universe.gamedata.world.solarsystem;

import com.jbs.universe.gamedata.world.planet.Planet;
import com.jbs.universe.gamedata.world.spaceship.Spaceship;
import com.jbs.universe.gamedata.world.star.Star;
import com.jbs.universe.screen.console.ColorString;

import java.util.ArrayList;

public class SolarSystem {
    public Star star;
    public ArrayList<Planet> planetList;
    public ArrayList<Spaceship> spaceshipList;

    public ColorString name;

    public SolarSystem(ColorString name, Star star) {
        this.star = star;
        planetList = new ArrayList<>();
        spaceshipList = new ArrayList<>();

        this.name = name;
    }

    public void update() {
        for(Planet planet : planetList) {
            planet.update();
        }
    }
}
