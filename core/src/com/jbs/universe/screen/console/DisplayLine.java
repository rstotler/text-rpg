package com.jbs.universe.screen.console;

import com.jbs.universe.gamedata.item.Item;
import com.jbs.universe.gamedata.mob.Mob;
import com.jbs.universe.gamedata.player.Player;

import java.util.ArrayList;

public class DisplayLine {
    public int num;

    public ColorString colorString;
    public int count;

    public boolean groupTargetCheck;
    public boolean groupCheck;
    public boolean combatCheck;
    public boolean targetCheck;

    public DisplayLine(int num, ColorString colorString, int count) {
        this.num = num;
        this.colorString = colorString;
        this.count = count;
    }

    public void display(Console console) {
        String displayString = colorString.label;
        String displayColorCode = colorString.colorCode;

        if(count > 1) {
            displayString += " (" + count + ")";
            displayColorCode += "2r" + String.valueOf(count).length() + "w1r";
        }

        console.write(new ColorString(displayString, displayColorCode), false);
    }

    public static ArrayList<DisplayLine> quantifyMobList(ArrayList<Mob> mobList, Player player) {
        ArrayList<DisplayLine> displayLineList = new ArrayList<>();

        for(Mob mob : mobList) {
            boolean containsCheck = false;
            for(DisplayLine displayLine : displayLineList) {
                if(mob.num == displayLine.num) {
                    if((displayLine.groupTargetCheck && player.targetList.contains(mob) && player.groupList.contains(mob)) ||
                            (displayLine.groupCheck && player.groupList.contains(mob)) ||
                            (displayLine.combatCheck && player.combatList.contains(mob)) ||
                            (displayLine.targetCheck && !player.groupList.contains(mob) && player.targetList.contains(mob)) ||
                            (!displayLine.groupTargetCheck && !displayLine.groupCheck && !displayLine.combatCheck && !displayLine.targetCheck
                                    && !player.targetList.contains(mob) && !player.groupList.contains(mob)) && !player.combatList.contains(mob)) {
                        displayLine.count++;
                        containsCheck = true;
                        break;
                    }
                }
            }

            if(!containsCheck) {
                DisplayLine newDisplayLine = new DisplayLine(mob.num, mob.getFullName(), 1);

                if(player.targetList.contains(mob) && player.groupList.contains(mob)) {
                    newDisplayLine.groupTargetCheck = true;
                    newDisplayLine.colorString.label = "[+]" + newDisplayLine.colorString.label;
                    newDisplayLine.colorString.colorCode = "3c" + newDisplayLine.colorString.colorCode;
                } else if(player.groupList.contains(mob)) {
                    newDisplayLine.groupCheck = true;
                    newDisplayLine.colorString.label = "[+]" + newDisplayLine.colorString.label;
                    newDisplayLine.colorString.colorCode = "3g" + newDisplayLine.colorString.colorCode;
                } else if(player.combatList.contains(mob)) {
                    newDisplayLine.combatCheck = true;
                    newDisplayLine.colorString.label = "[+]" + newDisplayLine.colorString.label;
                    newDisplayLine.colorString.colorCode = "3r" + newDisplayLine.colorString.colorCode;
                } else if(player.targetList.contains(mob)) {
                    newDisplayLine.targetCheck = true;
                    newDisplayLine.colorString.label = "[+]" + newDisplayLine.colorString.label;
                    newDisplayLine.colorString.colorCode = "3m" + newDisplayLine.colorString.colorCode;
                }

                displayLineList.add(newDisplayLine);
            }
        }

        return displayLineList;
    }

    public static ArrayList<DisplayLine> quantifyItemList(ArrayList<Item> itemList) {
        ArrayList<DisplayLine> displayLineList = new ArrayList<>();

        for(Item item : itemList) {
            boolean containsCheck = false;
            for(DisplayLine displayLine : displayLineList) {
                if(item.num == displayLine.num) {
                    displayLine.count += item.getCount();
                    containsCheck = true;
                    break;
                }
            }
            if(!containsCheck) {
                DisplayLine newDisplayLine = new DisplayLine(item.num, item.getFullName(), item.getCount());
                displayLineList.add(newDisplayLine);
            }
        }

        return displayLineList;
    }
}
