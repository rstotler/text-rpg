package com.jbs.universe.gamedata.item;

import com.jbs.universe.screen.console.ColorString;

import java.util.ArrayList;

public class Item {
    public int num;
    public int quantity;

    public String prefix;
    public ColorString name;
    public ColorString description;

    public ArrayList<String> passwordList;

    public Item(int num) {
        this.num = num;

        prefix = "A ";
        name = new ColorString("", "");
        description = new ColorString("has been dropped on the ground.", "30w1y");

        passwordList = new ArrayList<>();
    }

    public ColorString getFullName() {
        String fullNameString = prefix + name.label + " " + description.label;
        String fullNameColorCode = prefix.length() + "w" + name.colorCode + "1w" + description.colorCode;
        return new ColorString(fullNameString, fullNameColorCode);
    }

    public int getCount() {
        if(quantity == -1) {
            return 1;
        } else {
            return quantity;
        }
    }
}
