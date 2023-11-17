package com.jbs.universe.gamedata.world;

import java.util.ArrayList;

public class SolarSystem {
    public ArrayList<Planet> planetList;
    public ArrayList<Spaceship> spaceshipList;

    public SolarSystem() {
        planetList = new ArrayList<Planet>();
        spaceshipList = new ArrayList<Spaceship>();
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
