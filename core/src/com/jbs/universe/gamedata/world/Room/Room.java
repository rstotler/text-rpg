package com.jbs.universe.gamedata.world.Room;

import com.jbs.universe.gamedata.item.Item;
import com.jbs.universe.gamedata.player.Player;
import com.jbs.universe.gamedata.world.Galaxy;
import com.jbs.universe.gamedata.world.Planet;
import com.jbs.universe.gamedata.world.Spaceship;
import com.jbs.universe.screen.console.ColorString;
import com.jbs.universe.screen.console.Console;

import java.util.*;

import static com.jbs.universe.components.Utility.createUnderlineColorString;
import static com.jbs.universe.components.Utility.getCountString;

public class Room {
    public int galaxy;
    public int system;
    public int planet;
    public int area;
    public int room;
    public Spaceship spaceshipObject;

    ColorString name;
    public ColorString description;

    public Map<String, Exit> exit;
    public Map<String, Door> door;

    public ArrayList<Player> mobList;
    public ArrayList<Item> itemList;
    public ArrayList<Spaceship> spaceshipList;

    public boolean inside;

    public Room(int galaxy, int system, int planet, int area, int room, ColorString name) {
        this.galaxy = galaxy;
        this.system = system;
        this.planet = planet;
        this.area = area;
        this.room = room;
        spaceshipObject = null;

        this.name = name;
        this.description = new ColorString("", "");

        exit = new HashMap<String, Exit>() {{
            put("North", null);
            put("East", null);
            put("South", null);
            put("West", null);
            put("Up", null);
            put("Down", null);
        }};
        door = new HashMap<String, Door>() {{
            put("North", null);
            put("East", null);
            put("South", null);
            put("West", null);
            put("Up", null);
            put("Down", null);
        }};

        mobList = new ArrayList<Player>();
        itemList = new ArrayList<Item>();
        spaceshipList = new ArrayList<Spaceship>();

        inside = false;
    }

    public void display(Console console, ArrayList<Galaxy> galaxyList, Player player) {
        boolean roomIsLit = isLit(galaxyList, player, player);

        // Room Name //
        ColorString colorStringName = new ColorString(name.label, name.colorCode);
        if(!roomIsLit) {
            colorStringName = new ColorString("Darkness", "1ddda1dda1da1da1da1a1da1dda");
        }
        if(inside) {
            colorStringName.label = "(Inside) " + colorStringName.label;
            colorStringName.colorCode = "1r6w2r" + colorStringName.colorCode;
        }
        console.write(colorStringName, true);
        console.write(createUnderlineColorString(colorStringName.label), false);

        // Description //
        if(!roomIsLit) {
            console.write(new ColorString("It's too dark to see..", "2w1y17w2y"), false);
        } else {
            console.write(new ColorString(description.label, description.colorCode), false);
        }

        // Exits //
        for(String exitDir : new String[] {"North", "East", "South", "West", "Up", "Down"}) {
            if(!((exitDir.equals("Up") || exitDir.equals("Down"))
            && exit.get(exitDir) == null)) {
                String spaceString = "";
                if(Arrays.binarySearch(new String[] {"East", "West", "Down"}, exitDir) >= 0) {
                    spaceString = " ";
                } else if(exitDir.equals("Up")) {
                    spaceString = "   ";
                }

                if(exit.get(exitDir) != null && !(door.get(exitDir) != null && door.get(exitDir).type.equals("Hidden") && Arrays.binarySearch(new String[] {"Locked", "Closed"}, door.get(exitDir).status) >= 0)) {
                    Room exitRoom = null;
                    if(exit.get(exitDir).type().equals("Room")) {
                        exitRoom = Room.exists(galaxyList, -1, exit.get(exitDir).galaxy, exit.get(exitDir).system, exit.get(exitDir).planet, exit.get(exitDir).area, exit.get(exitDir).room);
                    } else if(exit.get(exitDir).type().equals("Spaceship Room")) {
                        exitRoom = spaceshipObject.getRoom(exit.get(exitDir).area, exit.get(exitDir).room);
                    } else if(exit.get(exitDir).type().equals("Spaceship Exit")) {
                        exitRoom = Room.exists(galaxyList, -1, spaceshipObject.galaxy, spaceshipObject.system, spaceshipObject.planet, spaceshipObject.landedLocation[0], spaceshipObject.landedLocation[1]);
                    }
                    if(exitRoom == null) {
                        exitRoom = galaxyList.get(0).systemList.get(0).planetList.get(0).areaList.get(0).roomList.get(0);
                    }

                    String exitRoomString = "";
                    String exitRoomColorCode = "";
                    if(door.get(exitDir) != null && Arrays.binarySearch(new String[] {"Closed", "Locked"}, door.get(exitDir).status) >= 0) {
                        exitRoomString = "( " + spaceString + exitDir + " ) - [Closed]";
                        exitRoomColorCode = "2r1w4ddw2r3y1r6w1r";
                    } else if(!roomIsLit && !exitRoom.isLit(galaxyList, player, player)) {
                        exitRoomString = "( " + spaceString + exitDir + " ) - ( Black )";
                        exitRoomColorCode = "2r1w4ddw2r3y2r1dw1a2da1dda2r";
                    } else {
                        exitRoomString = "( " + spaceString + exitDir + " ) - " + exitRoom.name.label;
                    }

                    String exitRoomNameCode = "";
                    if(!roomIsLit && !exitRoom.isLit(galaxyList, player, player)) {
                        exitRoomNameCode = "2r1dw1a2da1dda2r";
                    } else {
                        exitRoomNameCode = exitRoom.name.colorCode;
                    }

                    if(door.get(exitDir) == null || door.get(exitDir).status.equals("Open")) {
                        if(Arrays.binarySearch(new String[] {"East", "West", "Down"}, exitDir) >= 0) {
                            exitRoomColorCode = "3r1w" + String.valueOf(exitDir.length() - 1) + "ddw2r3y" + exitRoomNameCode;
                        } else if(exitDir.equals("Up")) {
                            exitRoomColorCode = "5r1w" + String.valueOf(exitDir.length() - 1) + "ddw2r3y" + exitRoomNameCode;
                        } else {
                            exitRoomColorCode = "2r1w" + String.valueOf(exitDir.length() - 1) + "ddw2r3y" + exitRoomNameCode;
                        }
                    }

                    console.write(new ColorString(exitRoomString, exitRoomColorCode), false);
                }

                else {
                    String exitRoomCode = "";
                    if(Arrays.binarySearch(new String[] {"East", "West", "Down"}, exitDir) >= 0) {
                        exitRoomCode = "3r1w" + String.valueOf(exitDir.length() - 1) + "ddw2r3y2r1w6ddw2r";
                    } else if(exitDir.equals("Up")) {
                        exitRoomCode = "5r1w" + String.valueOf(exitDir.length() - 1) + "ddw2r3y2r1w6ddw2r";
                    } else {
                        exitRoomCode = "2r1w" + String.valueOf(exitDir.length() - 1) + "ddw2r3y2r1w6ddw2r";
                    }

                    if(!roomIsLit) {
                        exitRoomCode = exitRoomCode.substring(0, exitRoomCode.length() - 8) + "1dw1a2da1dda2r";
                        console.write(new ColorString("( " + spaceString + exitDir + " ) - ( Black )", exitRoomCode), false);
                    } else {
                        console.write(new ColorString("( " + spaceString + exitDir + " ) - ( Nothing )", exitRoomCode), false);
                    }
                }
            }
        }

        // Spaceships //
        if(!spaceshipList.isEmpty()) {
            if(!roomIsLit) {
                String displayString = "A spaceship is sitting on the launch pad.";
                String displayColorCode = "40w1y";
                ColorString countString = getCountString(spaceshipList.size(), true);
                console.write(new ColorString(displayString + countString.label, displayColorCode + countString.colorCode), false);
            } else {
                for(Spaceship spaceship : spaceshipList) {
                    String displayString = "A " + spaceship.name.label + " is sitting on the launch pad.";
                    String displayColorCode = "2w" + spaceship.name.colorCode + "29w1y";
                    console.write(new ColorString(displayString, displayColorCode), false);
                }
            }
        }

        // Mobs //

        // Items //
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
