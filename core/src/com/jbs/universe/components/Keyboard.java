package com.jbs.universe.components;

public class Keyboard {
    public static String inputCharacterList;
    public static String upperCharacterList;
    public static boolean shift;
    public static boolean control;
    public static boolean backspace;

    public static int backspaceTimer;

    public Keyboard() {
        inputCharacterList = "1234567890-=[];',./ABCDEFGHIJKLMNOPQRSTUVWXYZSpace";
        upperCharacterList = "!@#$%^&*()_+{}:\"<>?";
        shift = false;
        control = false;
        backspace = false;

        backspaceTimer = -1;
    }

    public void update() {
        if(backspace) {
            backspaceTimer++;
            if(backspaceTimer >= 4) {
                backspaceTimer = -1;
            }
        }
    }

    public static String getArrowDirectionString(String targetKey) {
        targetKey = targetKey.toLowerCase();
        if(targetKey.charAt(0) == 'u') {
            return "North";
        } else if(targetKey.charAt(0) == 'r') {
            return "East";
        } else if(targetKey.charAt(0) == 'd') {
            return "South";
        } else {
            return "West";
        }
    }
}
