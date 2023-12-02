package com.jbs.universe.gamedata.world.room;

import com.jbs.universe.gamedata.player.Player;

public class Door {
    public String type;
    public String password;
    public String status;

    public Door(String type, String password) {
        this.type = type;
        this.password = password;
        status = "Closed";
    }

    public boolean keyCheck(Player targetPlayer) {
        if(password.equals("") || targetPlayer.hasKey(password)) {
            return true;
        }
        return false;
    }
}
