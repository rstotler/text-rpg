package com.jbs.universe.gamedata;

import com.jbs.universe.gamedata.player.Player;
import com.jbs.universe.screen.console.ColorString;

import java.util.Locale;

public class Mob extends Player {
    public int num;

    public String prefix;
    public ColorString name;
    public ColorString roomDescription;

    public int[] displayOffset;

    public int speechTimerMax;
    public int speechTimer;
    public int speechIndex;

    public Mob(int galaxy, int system, int planet, int area, int room, int spaceship, int num) {
        super(galaxy, system, planet, area, room, spaceship);
        this.num = num;

        speechTimerMax = 8;
        speechTimer = 0;
        speechIndex = 0;
    }

    public boolean isNPC() {
        return true;
    }

    public ColorString getMobColorString(String targetString, boolean uppercaseCheck) {
        if(targetString.equals("Name")) {
            String prefixString = prefix;
            if(!uppercaseCheck) {
                prefixString = prefix.toLowerCase();
            }
            return new ColorString(prefixString + " " + name.label, prefix.length() + "w1w" + name.colorCode);
        }

        return new ColorString("", "");
    }
}
