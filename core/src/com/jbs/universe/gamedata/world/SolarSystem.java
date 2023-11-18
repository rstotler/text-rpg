package com.jbs.universe.gamedata.world;

import com.jbs.universe.screen.console.ColorString;

import java.util.ArrayList;

public class SolarSystem {
    public ArrayList<Planet> planetList;
    public ArrayList<Spaceship> spaceshipList;

    public ColorString name;

    public SolarSystem(ColorString name) {
        planetList = new ArrayList<Planet>();
        spaceshipList = new ArrayList<Spaceship>();

        this.name = name;
    }

    public Spaceship getSpaceship(int targetSpaceshipNum) {
        for(Spaceship spaceship : spaceshipList) {
            if(spaceship.num == targetSpaceshipNum) {
                return spaceship;
            }
        }
        return null;
    }
}
