package com.jbs.universe.gamedata.world.Room;

public class Exit {
    public int galaxy;
    public int system;
    public int planet;
    public int area;
    public int room;
    public int spaceship;

    public boolean spaceshipExit;

    public Exit(int galaxy, int system, int planet, int area, int room, int spaceship) {
        this.galaxy = galaxy;
        this.system = system;
        this.planet = planet;
        this.area = area;
        this.room = room;
        this.spaceship = spaceship;

        if(galaxy == -1 && spaceship == -1) {
            spaceshipExit = true;
        }
    }

    public String type() {
        if(spaceshipExit) {
            return "Spaceship Exit";
        } else if(spaceship == -1) {
            return "Room";
        } else {
            return "Spaceship Room";
        }
    }
}
