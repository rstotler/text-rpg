package com.jbs.universe.gamedata.item;

import com.jbs.universe.screen.console.ColorString;

import java.util.ArrayList;

public class Item {
    public int num;
    public String pocket;
    public ArrayList<String> flags;

    public String prefix;
    public ColorString name;
    public ColorString roomDescription;

    public int quantity;

    public Item() {
        flags = new ArrayList<String>();
    }

    public static int getSpecialItemNum(String targetItem) {
        if(targetItem.equals("Corpse")) {
            return 666;
        }
        return 0;
    }
}
