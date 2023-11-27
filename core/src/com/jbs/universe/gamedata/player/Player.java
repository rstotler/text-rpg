package com.jbs.universe.gamedata.player;

import com.jbs.universe.gamedata.GameObject;
import com.jbs.universe.gamedata.Mob;
import com.jbs.universe.gamedata.item.Item;
import com.jbs.universe.gamedata.world.Area;
import com.jbs.universe.gamedata.world.Galaxy;
import com.jbs.universe.gamedata.world.Room.MoveData;
import com.jbs.universe.gamedata.world.Room.Room;
import com.jbs.universe.screen.MiniMap;
import com.jbs.universe.screen.console.ColorString;
import com.jbs.universe.screen.console.Console;
import com.jbs.universe.screen.console.MessageData;

import java.util.*;

import static com.jbs.universe.components.Utility.*;

public class Player implements GameObject {
    public int galaxy;
    public int system;
    public int planet;
    public int area;
    public int room;
    public int spaceship;

    public ArrayList<Action> actionList;

    public int maxLookDistance;

    public ArrayList<Mob> targetList;
    public ArrayList<Mob> recruitList;
    public ArrayList<Player> combatList;

    public Map<String, ArrayList<Item>> itemList;
    public Map<String, Item[]> gear;

    public Player(int galaxy, int system, int planet, int area, int room, int spaceship) {
        this.galaxy = galaxy;
        this.system = system;
        this.planet = planet;
        this.area = area;
        this.room = room;
        this.spaceship = spaceship;

        actionList = new ArrayList<Action>();

        maxLookDistance = 5;

        targetList = new ArrayList<Mob>();
        recruitList = new ArrayList<Mob>();
        combatList = new ArrayList<Player>();

        itemList = new HashMap<String, ArrayList<Item>>();
        gear = new HashMap<String, Item[]>();
    }

    public ArrayList<MessageData> moveCheck(Console console, MiniMap miniMap, ArrayList<Galaxy> galaxyList, Player player, Room currentRoom, String targetDirKey) {
        ArrayList<MessageData> messageDataList = new ArrayList<MessageData>();
        String targetDir = getTargetDirectionString(targetDirKey);

        if(currentRoom.door.get(targetDir) != null && currentRoom.door.get(targetDir).status.equals("Locked") && !currentRoom.door.get(targetDir).password.isEmpty() && hasKey(currentRoom.door.get(targetDir).password)) {
            if(!isNPC()) {
                console.write(new ColorString("You lack the proper key.", "23w1y"), true);
            }
        } else if(currentRoom.exit.get(targetDir) == null || (currentRoom.door.get(targetDir) != null && currentRoom.door.get(targetDir).type.equals("Hidden") && Arrays.asList("Closed", "Locked").contains(currentRoom.door.get(targetDir).status))) {
            if(!isNPC()) {
                console.write(new ColorString("You can't go that way!", "7w1y13w1y"), true);
            }
        } else if(currentRoom.exit.get(targetDir).type().equals("Spaceship Exit") && currentRoom.spaceshipObject != null && currentRoom.spaceshipObject.landedLocation[0] == -1) {
            if(!isNPC()) {
                console.write(new ColorString("The hatch is sealed.", "19w1y"), true);
            }
        }

        else {
            ColorString mobColorString = getMobColorString("Name", true);
            if(!actionList.isEmpty() && !Arrays.asList("Combat Skill", "Reload").contains(actionList.get(0).actionType)) {
                Action targetAction = actionList.get(0);
                actionList.clear();
                if(!isNPC()) {
                    console.write(new ColorString("You stop " + targetAction.actionType.toLowerCase() + "ing and move.", "9w" + targetAction.actionType.length() + "w12w1y"), true);
                } else if(currentRoom.sameRoomCheck(player)) {
                    String messageDataString = mobColorString.label + " stops " + targetAction.actionType.toLowerCase() + "ing and moves " + targetDir + ".";
                    String messageDataColorCode = mobColorString.colorCode + "7w" + targetAction.actionType.length() + "w14w" + targetDir.length() + "w1y";
                    messageDataList.add(new MessageData(messageDataString, messageDataColorCode, true));
                }
            } else if(isNPC() && currentRoom.sameRoomCheck(player) && !(currentRoom.door.get(targetDir) != null && currentRoom.door.get(targetDir).type.equals("Automatic"))) {
                String messageDataString = mobColorString.label + " moves " + targetDir + ".";
                String messageDataColorCode = mobColorString.colorCode + "7w" + targetDir.length() + "w1y";
                messageDataList.add(new MessageData(messageDataString, messageDataColorCode, true));
            }

            Area currentArea = (Area) Room.getAreaAndRoom(galaxyList, this).get("Area");
            MoveData moveData = Room.getTargetRoomFromStartRoom(galaxyList, currentArea, currentRoom, targetDir, 1, true);

            if(!actionList.isEmpty() && actionList.get(0).actionType.equals("Combat Skill") && actionList.get(0).combatSkill.maxRange > 0) {
                MoveData attackData = Room.getTargetRange(galaxyList, moveData.targetRoom, actionList.get(0).targetRoom, actionList.get(0).combatSkill.maxRange);
                if(attackData.distance == -1) {
                    actionList.remove(0);
                    if(!isNPC()) {
                        console.write(new ColorString("You move out of your attack range.", "33w1y"), true);
                    } else {
                        String messageDataString = mobColorString.label + " releases their attack.";
                        String messageDataColorCode = mobColorString.colorCode + "22w1y";
                        messageDataList.add(new MessageData(messageDataString, messageDataColorCode, true));
                    }
                }
            }

            // Move //
            boolean automaticDoorCheck = false;
            if(moveData.targetRoom.spaceshipObject != null) {
                area = moveData.targetRoom.area;
                room = moveData.targetRoom.room;
            } else {
                int oldSpaceship = spaceship;
                int oldArea = area;
                galaxy = moveData.targetRoom.galaxy;
                system = moveData.targetRoom.system;
                planet = moveData.targetRoom.planet;
                area = moveData.targetRoom.area;
                room = moveData.targetRoom.room;
                if(spaceship != -1) {
                    spaceship = -1;
                }

                if(!isNPC()) {
                    if(spaceship != oldSpaceship || area != oldArea) {
                        miniMap.loadAreaMap(moveData.targetArea);
                        moveData.targetArea.flavorTextTimer = 0;
                    }
                }
            }

            boolean openCheck = false;
            boolean lockCheck = false;
            if(currentRoom.door.get(targetDir) != null) {
                if(currentRoom.door.get(targetDir).status.equals("Locked")) {
                    lockCheck = true;
                }
                if(currentRoom.door.get(targetDir).type.equals("Manual") && !currentRoom.door.get(targetDir).status.equals("Open")) {
                    currentRoom.openCloseDoor(galaxyList, "Open", targetDir);
                    openCheck = true;
                }
            }

            if(!isNPC() || moveData.targetRoom.sameRoomCheck(player) || currentRoom.sameRoomCheck(player)) {
                if(currentRoom.door.get(targetDir) != null && currentRoom.door.get(targetDir).type.equals("Manual") && openCheck) {
                    if(!isNPC()) {
                        String openString = "open";
                        if(lockCheck) {
                            openString = "unlock and open";
                        }
                        console.write(new ColorString("You " + openString + " the door and walk through.", "4w" + openString.length() + "w26w1y"), true);
                    } else if(currentRoom.sameRoomCheck(player)) {
                        String openString = "opens";
                        if(lockCheck) {
                            openString = "unlocks and opens";
                        }
                        String displayString = getMobColorString("Name", true).label + " " + openString + " the door to the " + targetDir + " and walks through.";
                        String displayColorCode = getMobColorString("Name", true).colorCode + "1w" + openString.length() + "w17w" + targetDir.length() + "w18w1y";
                        messageDataList.add(new MessageData(displayString, displayColorCode, true));
                    } else if(moveData.targetRoom.sameRoomCheck(player)) {
                        String displayString = "The door to the " + Room.getOppositeDirection(targetDir) + " opens as " + getMobColorString("Name").label + " walks through.";
                        String displayColorCode = "16w" + Room.getOppositeDirection(targetDir).length() + "w10w" + getMobColorString("Name").colorCode + "14w1y";
                        messageDataList.add(new MessageData(displayString, displayColorCode, true));
                    }
                }

                else if(currentRoom.door.get(targetDir) != null && currentRoom.door.get(targetDir).type.equals("Automatic")) {
                    String doorString = "door";
                    if(currentRoom.exit.get(targetDir).type().equals("Spaceship Exit")) {
                        doorString = "hatch";
                    }
                    if(!isNPC()) {
                        console.write(new ColorString("The " + doorString + " opens and closes as you walk through.", "4w" + doorString.length() + "w37w1y"), true);
                    } else {
                        String displayDirection = targetDir;
                        String leavesString = " leaves";
                        if(moveData.targetRoom.sameRoomCheck(player)) {
                            displayDirection = Room.getOppositeDirection(targetDir);
                            leavesString = " enters";
                        }
                        String displayString = "The door to the " + displayDirection + " opens and closes as " + mobColorString.label + leavesString + ".";
                        String displayColorCode = "16w" + displayDirection.length() + "w21w" + mobColorString.colorCode + leavesString.length() + "w1y";
                        messageDataList.add(new MessageData(displayString, displayColorCode, true));
                    }
                    automaticDoorCheck = true;
                }
            }

            if(!isNPC()) {
                for(Mob mob : moveData.targetRoom.mobList) {
                    mob.speechTimer = 0;
                    mob.speechIndex = 0;
                }
            } else {
                currentRoom.mobList.remove(this);
                createMob(-1, moveData.targetRoom, this);
                actionList.add(new Action("Buffer Action", {}, 3)); ////// needs a modification
            }

            ArrayList<Mob> delTargetList = new ArrayList<Mob>();
            Mob delMobData = null;
            boolean deleteMultiple = false;
            String loseSightDir = "";
            if(!isNPC()) {
                for(Mob targetMob : targetList) {
                    MoveData targetMoveData = Room.getTargetRange(galaxyList, moveData.targetRoom, targetMob, maxLookDistance + 1);
                    if(targetMoveData.distance == maxLookDistance + 1) {
                        delTargetList.add(targetMob);
                        loseSightDir = targetMoveData.direction;
                        if(delMobData == null) {
                            delMobData = targetMob;
                        } else if(!deleteMultiple && delMobData.num != targetMob.num) {
                            deleteMultiple = true;
                        }
                    }
                }
                for(Mob targetMob : delTargetList) {
                    targetList.remove(targetMob);
                }

                moveData.targetRoom.display(console, galaxyList, this);
            }

            if(isNPC() && player.targetList.contains(this)) {
                Room playerRoom = Room.exists(galaxyList, player.spaceship, player.galaxy, player.system, player.planet, player.area, player.room);
                if(playerRoom == null) {
                    playerRoom = galaxyList.get(0).systemList.get(0).planetList.get(0).areaList.get(0).roomList.get(0);
                }
                MoveData targetMoveData = Room.getTargetRange(galaxyList, moveData.targetRoom, playerRoom, player.maxLookDistance + 1);
                if(!player.targetList.isEmpty() && player.targetList.get(0) == this && !player.actionList.isEmpty() && player.actionList.get(0).actionType.equals("Combat Skill") && player.actionList.get(0).combatSkill.onTarget && targetMoveData.distance > player.actionList.get(0).combatSkill.maxRange) {
                    player.actionList.remove(0);
                    String displayString = "Your target moved out of range for your attack.";
                    String displayColorCode = "46w1y";
                    messageDataList.add(new MessageData(displayString, displayColorCode, true));
                } else if(player.targetList.contains(this) && targetMoveData.distance > player.maxLookDistance) {
                    String displayString = "You lose sight of " + getMobColorString("Name", false).label + " as they move " + Room.getOppositeDirection(targetMoveData.direction) + ".";
                    String displayColorCode = "18w" + getMobColorString("Name", false).colorCode + "14w" + Room.getOppositeDirection(targetMoveData.direction) + "w1y";
                    messageDataList.add(new MessageData(displayString, displayColorCode, true));
                    if(player.targetList.get(0) == this && !player.actionList.isEmpty() && player.actionList.get(0).actionType.equals("Combat Skill") && player.actionList.get(0).combatSkill.onTarget) {
                        player.actionList.remove(0);
                        messageDataList.add(new MessageData("Your target moved out of range for your attack.", "46w1y", false));
                    }
                    player.targetList.remove(this);
                }
            }

            if(!isNPC() && !delTargetList.isEmpty()) {
                ColorString countColorString = getCountString(delTargetList.size(), true);
                if(!deleteMultiple) {
                    String displayString = "You lose sight of " + getMobColorString("Name", false).label + countColorString.label + " to the " + loseSightDir + ".";
                    String displayColorCode = "18w" + getMobColorString("Name", false).colorCode + countColorString.colorCode + "8w" + loseSightDir.length() + "w1y";
                    console.write(new ColorString(displayString, displayColorCode), true);
                } else {
                    String someString = "some";
                    if(targetList.isEmpty()) {
                        someString = "your";
                    }
                    String displayString = "You lose sight of " + someString + " targets" + countColorString.label + " to the " + loseSightDir + ".";
                    String displayColorCode = "18w" + someString.length() + "w8w" + countColorString.colorCode + "8w" + loseSightDir.length() + "w1y";
                    console.write(new ColorString(displayString, displayColorCode), true);
                }
            }

            else if(isNPC() && moveData.targetRoom.sameRoomCheck(player) && !automaticDoorCheck) {
                String displayString = getMobColorString("Name", true).label + " enters from the " + Room.getOppositeDirection(targetDir) + ".";
                String displayColorCode = getMobColorString("Name", true).colorCode + "17w" + Room.getOppositeDirection(targetDir).length() + "w1y";
                messageDataList.add(new MessageData(displayString, displayColorCode, true));
            }
        }

        return messageDataList;
    }

    // Utility Functions //
    public boolean isNPC() {
        return false;
    }

    public ColorString getMobColorString(String targetString, boolean uppercaseCheck) {
        return new ColorString("", "");
    }

    public CombatSkill getCombatSkill() {
        return null;
    }

    public boolean hasKey(String password) {
        for(String pocket : itemList.keySet()) {
            for(Item item : itemList.get(pocket)) {
                if(item.passwordList.contains(password)) {
                    return true;
                }
            }
        }
        return false;
    }
}
