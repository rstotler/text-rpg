package com.jbs.universe.gamedata.world.Room;

import com.jbs.universe.gamedata.GameObject;
import com.jbs.universe.gamedata.Mob;
import com.jbs.universe.gamedata.item.Item;
import com.jbs.universe.gamedata.player.Player;
import com.jbs.universe.gamedata.world.*;
import com.jbs.universe.screen.console.*;

import java.util.*;

import static com.jbs.universe.components.Utility.createUnderlineColorString;
import static com.jbs.universe.components.Utility.getCountString;

public class Room implements GameObject {
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

    public ArrayList<Mob> mobList;
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

        mobList = new ArrayList<Mob>();
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
        ArrayList<DisplayListItem> mobDisplayDataList = new ArrayList<DisplayListItem>();
        int totalMobCount = 0;
        for(Mob mob : mobList) {
            DisplayListItem targetMobDisplayData = null;
            for(DisplayListItem mobDisplayData : mobDisplayDataList) {
                boolean groupCheck = player.recruitList.contains(mob);
                boolean targetCheck = false;
                if(!groupCheck) {
                    targetCheck = player.targetList.contains(mob);
                }
                boolean combatCheck = player.combatList.contains(mob);
                if(mob.num == mobDisplayData.mob.num && targetCheck == mobDisplayData.targetCheck && groupCheck == mobDisplayData.groupCheck && combatCheck == mobDisplayData.combatCheck) {
                    targetMobDisplayData = mobDisplayData;
                    break;
                }
            }

            if(targetMobDisplayData == null) {
                boolean groupCheck = player.recruitList.contains(mob);
                boolean targetCheck = false;
                if(!groupCheck) {
                    targetCheck = player.targetList.contains(mob);
                }
                boolean combatCheck = player.combatList.contains(mob);

                DisplayListItem mobDisplayListItem = new DisplayListItem(mob);
                mobDisplayListItem.targetCheck = targetCheck;
                mobDisplayListItem.groupCheck = groupCheck;
                mobDisplayListItem.combatCheck = combatCheck;
                mobDisplayDataList.add(mobDisplayListItem);
            } else {
                targetMobDisplayData.count++;
            }
            totalMobCount++;
        }

        if(!roomIsLit && totalMobCount > 0) {
            ColorString countColorString = getCountString(totalMobCount, true);
            countColorString.label = "There is someone here." + countColorString.label;
            countColorString.colorCode = "21w1y" + countColorString.colorCode;
            console.write(countColorString, false);
        }

        else {
            for(DisplayListItem mobDisplayData : mobDisplayDataList) {
                String targetString = "";
                String targetColorCode = "";
                if(mobDisplayData.groupCheck) {
                    targetString = "[+]";
                    targetColorCode = "3g";
                } else if(mobDisplayData.targetCheck) {
                    targetString = "[+]";
                    targetColorCode = "3m";
                }
                ColorString countColorString = getCountString(mobDisplayData.count, true);
                String mobDisplayString = "";
                String mobDisplayColorCode = "";
                if(mobDisplayData.combatCheck) {
                    mobDisplayString = targetString + mobDisplayData.mob.prefix + mobDisplayData.mob.name.label + " is here, fighting you!" + countColorString.label;
                    mobDisplayColorCode = targetColorCode + mobDisplayData.mob.prefix.length() + "w1w" + mobDisplayData.mob.name.colorCode + "8w1y13w1y" + countColorString.colorCode;
                } else {
                    mobDisplayString = targetString + mobDisplayData.mob.prefix + mobDisplayData.mob.name.label + " " + mobDisplayData.mob.roomDescription.label + countColorString.label;
                    mobDisplayColorCode = targetColorCode + mobDisplayData.mob.prefix.length() + "w1w" + mobDisplayData.mob.name.colorCode + "1w" + mobDisplayData.mob.roomDescription.colorCode + countColorString.colorCode;
                }
                console.write(new ColorString(mobDisplayString, mobDisplayColorCode), false);
            }
        }

        // Items //
        ArrayList<DisplayListItem> itemDisplayDataList = new ArrayList<DisplayListItem>();
        int totalItemCount = 0;
        for(Item item : itemList) {
            DisplayListItem targetItemDisplayData = null;
            for(DisplayListItem itemDisplayData : itemDisplayDataList) {
                if(item.num == itemDisplayData.item.num && item.pocket.equals(itemDisplayData.item.pocket)) {
                    if(item.num != Item.getSpecialItemNum("Corpse") || item.name.label.equals(itemDisplayData.item.name.label)) {
                        targetItemDisplayData = itemDisplayData;
                        break;
                    }
                }
            }
            if(targetItemDisplayData == null) {
                int itemCount = 1;
                if(item.quantity != -1) {
                    itemCount = item.quantity;
                }
                DisplayListItem itemDisplayListItem = new DisplayListItem(item);
                itemDisplayListItem.count = itemCount;
                itemDisplayDataList.add(itemDisplayListItem);
                totalItemCount += itemCount;
            } else {
                targetItemDisplayData.count++;
                totalItemCount++;
            }
        }

        if(!roomIsLit && totalItemCount > 0) {
            ColorString countColorString = getCountString(totalItemCount, true);
            countColorString.label = "There is something on the ground." + countColorString.label;
            countColorString.colorCode = "32w1y" + countColorString.colorCode;
            console.write(countColorString, false);
        }

        else {
            for(DisplayListItem itemDisplayData : itemDisplayDataList) {
                String modString = "";
                String modColorCode = "";
                if(itemDisplayData.item.flags.contains("Glowing")) {
                    modString = " (Glowing)";
                    modColorCode = "2y1w1dw1ddw1w2dw1ddw1y";
                }

                ColorString countColorString = getCountString(itemDisplayData.count, true);
                countColorString.label = itemDisplayData.item.prefix + " " + itemDisplayData.item.name.label + " " + itemDisplayData.item.roomDescription.label + modString + countColorString.label;
                countColorString.colorCode = itemDisplayData.item.prefix.length() + "w1w" + itemDisplayData.item.name.colorCode + "1w" + itemDisplayData.item.roomDescription.colorCode + modColorCode + countColorString.colorCode;
                console.write(countColorString, false);
            }
        }
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

    public static TargetDistanceObject getTargetDistance(ArrayList<Galaxy> galaxyList, Room startRoom, GameObject targetObject, int maxRange) {
        boolean targetFoundCheck = false;
        int targetRange = 0;
        String searchDir = "";
        String messageType = "";

        String targetClass = String.valueOf(targetObject.getClass());
        targetClass = targetClass.substring(targetClass.lastIndexOf('.') + 1);

        if(targetClass.equals("Player") || targetClass.equals("Mob")) {
            if( (((Player) targetObject).isNPC() && startRoom.mobList.contains(targetObject))
            || ( !(((Player) targetObject).isNPC()) && startRoom.sameRoomCheck((Player) targetObject))) {
                return new TargetDistanceObject(0, "", "");
            }
        } else {
            if(startRoom.sameRoomCheck())
        }
    }

    public static Map<String, GameObject> getAreaAndRoom(ArrayList<Galaxy> galaxyList, GameObject target) {
        String targetClass = String.valueOf(target.getClass());
        targetClass = targetClass.substring(targetClass.lastIndexOf('.') + 1);
        Room targetRoom = null;
        Area targetArea = null;

        if(targetClass.equals("Room")) {
            if(((Room) target).spaceshipObject != null) {
                targetRoom = Room.exists(galaxyList, ((Room) target).spaceshipObject.num, ((Room) target).spaceshipObject.galaxy, ((Room) target).spaceshipObject.system, ((Room) target).spaceshipObject.planet, ((Room) target).area, ((Room) target).room);
                if(targetRoom != null) {
                    targetArea = targetRoom.spaceshipObject.areaList.get(((Room) target).area);
                }
            } else {
                targetRoom = Room.exists(galaxyList, -1, ((Room) target).galaxy, ((Room) target).system, ((Room) target).planet, ((Room) target).area, ((Room) target).room);
                if(targetRoom != null) {
                    targetArea = galaxyList.get(((Room) target).galaxy).systemList.get(((Room) target).system).planetList.get(((Room) target).planet).areaList.get(((Room) target).area);
                }
            }
        }
        else if(targetClass.equals("Player") || targetClass.equals("Mob")) {
            targetRoom = Room.exists(galaxyList, -1, ((Player) target).galaxy, ((Player) target).system, ((Player) target).planet, ((Player) target).area, ((Player) target).room);
            if(targetRoom != null) {
                if(targetRoom.spaceshipObject != null) {
                    targetArea = targetRoom.spaceshipObject.areaList.get(((Player) target).area);
                } else {
                    targetArea = galaxyList.get(targetRoom.galaxy).systemList.get(targetRoom.system).planetList.get(targetRoom.planet).areaList.get(targetRoom.area);
                }
            }
        }
        if(targetRoom == null) {
            targetRoom = galaxyList.get(0).systemList.get(0).planetList.get(0).areaList.get(0).roomList.get(0);
            targetArea = galaxyList.get(0).systemList.get(0).planetList.get(0).areaList.get(0);
        }

        Map<String, GameObject> returnData = new HashMap<String, GameObject>();
        returnData.put("Area", targetArea);
        returnData.put("Room", targetRoom);
        return returnData;
    }

    public static String getOppositeDirection(String targetDirection) {
        targetDirection = targetDirection.toLowerCase();
        if(targetDirection.charAt(0) == 'n') {
            return "South";
        } else if(targetDirection.charAt(0) == 'e') {
            return "West";
        } else if(targetDirection.charAt(0) == 's') {
            return "North";
        } else if(targetDirection.charAt(0) == 'w') {
            return "East";
        } else if(targetDirection.charAt(0) == 'u') {
            return "Down";
        } else {
            return "Up";
        }
    }
}
