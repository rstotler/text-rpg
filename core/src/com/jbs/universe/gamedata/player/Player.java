package com.jbs.universe.gamedata.player;

import com.jbs.universe.gamedata.Mob;
import com.jbs.universe.gamedata.item.Item;
import com.jbs.universe.screen.console.ColorString;

import java.util.*;

public class Player {
    public int galaxy;
    public int system;
    public int planet;
    public int area;
    public int room;
    public int spaceship;

    public ArrayList<Action> actionList;

    public ArrayList<Mob> targetList;
    public ArrayList<Mob> recruitList;
    public ArrayList<Player> combatList;

    public Map<String, Item[]> gear;

    public Player(int galaxy, int system, int planet, int area, int room, int spaceship) {
        this.galaxy = galaxy;
        this.system = system;
        this.planet = planet;
        this.area = area;
        this.room = room;
        this.spaceship = spaceship;

        actionList = new ArrayList<Action>();

        targetList = new ArrayList<Mob>();
        recruitList = new ArrayList<Mob>();
        combatList = new ArrayList<Player>();

        gear = new HashMap<String, Item[]>();
    }

    public boolean isNPC() {
        return false;
    }

    public CombatSkill getCombatSkill() {
        return null;
    }
}
