package com.jbs.universe.gamedata.world.solarsystem;

import com.jbs.universe.gamedata.player.Player;
import com.jbs.universe.gamedata.world.Location;
import com.jbs.universe.gamedata.world.galaxy.Galaxy;
import com.jbs.universe.gamedata.world.planet.Planet;
import com.jbs.universe.gamedata.world.spaceship.Spaceship;
import com.jbs.universe.gamedata.world.star.Star;
import com.jbs.universe.screen.console.ColorString;
import com.jbs.universe.screen.console.Console;

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

    public void update(Console console, Player player) {
        for(Planet planet : planetList) {
            planet.update(console, player);
        }
    }

    public static SolarSystem getSystem(ArrayList<Galaxy> galaxyList, Location location) {
        if(location.spaceshipObject == null) {
            return galaxyList.get(location.galaxy).systemList.get(location.system);
        } else {
            Location spaceshipLocation = location.spaceshipObject.location;
            return galaxyList.get(spaceshipLocation.galaxy).systemList.get(spaceshipLocation.system);
        }
    }
}
