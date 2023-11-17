package com.jbs.universe.gamedata.player;

import com.jbs.universe.gamedata.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {
    public int galaxy;
    public int system;
    public int planet;
    public int area;
    public int room;
    public int spaceship;
    public int num;

    public ArrayList<Action> actionList;

    public Map<String, Item[]> gear;

    public Player(int galaxy, int system, int planet, int area, int room, int spaceship, int num) {
        this.galaxy = galaxy;
        this.system = system;
        this.planet = planet;
        this.area = area;
        this.room = room;
        this.spaceship = spaceship;
        this.num = num;

        actionList = new ArrayList<Action>();

        gear = new HashMap<String, Item[]>();
    }

    public boolean isNPC() {
        return num != -1;
    }

    public CombatSkill getCombatSkill() {
        return null;
    }
}
