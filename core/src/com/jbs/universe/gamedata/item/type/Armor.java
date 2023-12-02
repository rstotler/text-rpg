package com.jbs.universe.gamedata.item.type;

import com.jbs.universe.gamedata.item.Item;
import com.jbs.universe.screen.console.ColorString;

public class Armor extends Item {
    public Armor(int num) {
        super(num);
    }

    public static Armor load(int num) {
        Armor item = new Armor(num);

        if(num == 1) {
            item.name = new ColorString("Helmet", "6shim-w");
        }

        else if(num == 2) {
            item.name = new ColorString("Necklace", "8shim-w");
        }

        return item;
    }
}
