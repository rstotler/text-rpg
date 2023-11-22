package com.jbs.universe.gamedata;

import com.jbs.universe.gamedata.player.Player;
import com.jbs.universe.screen.console.ColorString;

public class Mob extends Player {
    public int num;

    public String prefix;
    public ColorString name;
    public ColorString roomDescription;

    public int[] displayOffset;

    public Mob(int galaxy, int system, int planet, int area, int room, int spaceship, int num) {
        super(galaxy, system, planet, area, room, spaceship);
        this.num = num;
    }

    public boolean isNPC() {
        return true;
    }
}
