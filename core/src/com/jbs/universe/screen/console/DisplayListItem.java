package com.jbs.universe.screen.console;

import com.jbs.universe.gamedata.Mob;
import com.jbs.universe.gamedata.item.Item;

public class DisplayListItem {
    public Mob mob;
    public Item item;

    public boolean targetCheck;
    public boolean groupCheck;
    public boolean combatCheck;
    public int count;

    public DisplayListItem(Object object) {
        String targetClass = String.valueOf(object.getClass());
        targetClass = targetClass.substring(targetClass.lastIndexOf('.') + 1);
        if(targetClass.equals("Mob")) {
            mob = (Mob) object;

        } else if(targetClass.equals("Item")) {
            item = (Item) object;
        }
    }
}
