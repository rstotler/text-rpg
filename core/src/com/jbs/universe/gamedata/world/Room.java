package com.jbs.universe.gamedata.world;

import com.jbs.universe.gamedata.item.Item;
import com.jbs.universe.gamedata.player.Player;
import com.jbs.universe.screen.console.Console;

import java.util.ArrayList;

public class Room {
    public int galaxy;
    public int system;
    public int planet;
    public int area;
    public int room;
    public Spaceship spaceshipObject;

    public ArrayList<Player> mobList;
    public ArrayList<Item> itemList;

    public boolean inside;

    public Room(int galaxy, int system, int planet, int area, int room) {
        this.galaxy = galaxy;
        this.system = system;
        this.planet = planet;
        this.area = area;
        this.room = room;
        spaceshipObject = null;

        mobList = new ArrayList<Player>();
        itemList = new ArrayList<Item>();

        inside = false;
    }

    public void display(Console console, ArrayList<Galaxy> galaxyList, Player player) {
        boolean roomIsLit = isLit(galaxyList, player, player);
    }

    public boolean sameRoomCheck(Player target) {
        if(target.spaceship == -1 && spaceshipObject == null) {
            if(target.galaxy == galaxy && target.system == system && target.planet == planet && target.area == area && target.room == room) {
                return true;
            }
        } else if(target.spaceship != -1 && spaceshipObject != null) {
            if(target.spaceship == spaceshipObject.num && target.area == area && target.room == room) {
                return true;
            }
        }
        return false;
    }

    public boolean isLit(ArrayList<Galaxy> galaxyList, Player player, Player targetPlayer) {
        Planet targetPlanet = null;
        if(galaxy != -1 && system != -1 && planet != -1) {
            if(galaxy <= galaxyList.size() - 1 && system <= galaxyList.get(galaxy).systemList.size() - 1 && planet <= galaxyList.get(galaxy).systemList.get(system).planetList.size() - 1) {
                targetPlanet = galaxyList.get(galaxy).systemList.get(system).planetList.get(planet);
            }
        }
        if(targetPlanet != null && !inside) {
            if(targetPlanet.dayCheck()) {
                return true;
            }
        }

        for(Item item : itemList) {
            if(item.flags.contains("Glowing")) {
                return true;
            }
        }

        ArrayList<Player> targetPlayerList = new ArrayList<Player>();
        if(!targetPlayer.isNPC() || sameRoomCheck(player)) {
            targetPlayerList.add(player);
        }
        targetPlayerList.addAll(mobList);

        for(Player target : targetPlayerList) {
            if(sameRoomCheck(target)) {
                for(String gearSlot : target.gear.keySet()) {
                    for(Item item : target.gear.get(gearSlot)) {
                        if(item != null && item.flags.contains("Glowing")) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static Room exists(ArrayList<Galaxy> galaxyList, int targetSpaceship, int targetGalaxy, int targetSystem, int targetPlanet, int targetArea, int targetRoom) {
        if(targetSpaceship == -1) {
            if(targetGalaxy <= galaxyList.size() - 1
            && targetSystem <= galaxyList.get(targetGalaxy).systemList.size() - 1
            && targetPlanet <= galaxyList.get(targetGalaxy).systemList.get(targetSystem).planetList.size() - 1
            && targetArea <= galaxyList.get(targetGalaxy).systemList.get(targetSystem).planetList.get(targetPlanet).areaList.size() - 1
            && targetRoom <= galaxyList.get(targetGalaxy).systemList.get(targetSystem).planetList.get(targetPlanet).areaList.get(targetArea).roomList.size() - 1) {
                return galaxyList.get(targetGalaxy).systemList.get(targetSystem).planetList.get(targetPlanet).areaList.get(targetArea).roomList.get(targetRoom);
            }
        }

        else {
            Spaceship spaceshipObject = galaxyList.get(targetGalaxy).systemList.get(targetSystem).getSpaceship(targetSpaceship);
            if(spaceshipObject != null
            && targetArea <= spaceshipObject.areaList.size() - 1
            && targetRoom <= spaceshipObject.areaList.get(targetArea).roomList.size() - 1) {
                return spaceshipObject.areaList.get(targetArea).roomList.get(targetRoom);
            }
        }

        return null;
    }
}
