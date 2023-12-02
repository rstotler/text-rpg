package com.jbs.universe.gamedata.mob;

import com.jbs.universe.screen.console.ColorString;

public class Mob {
    public int num;

    public String prefix;
    public ColorString name;
    public ColorString description;

    public Mob(int num) {
        this.num = num;

        prefix = "A ";
        description = new ColorString("is standing here.", "16w1y");
    }

    public static Mob load(int num) {
        Mob mob = new Mob(num);

        if(num == 1) {
            mob.name = new ColorString("Man", "3shim-w");
        }

        return mob;
    }

    public ColorString getFullName() {
        String fullNameString = prefix + name.label + " " + description.label;
        String fullNameColorCode = prefix.length() + "w" + name.colorCode + "1w" + description.colorCode;
        return new ColorString(fullNameString, fullNameColorCode);
    }
}
