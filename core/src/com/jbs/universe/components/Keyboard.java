package com.jbs.universe.components;

public class Keyboard {
    public static String inputCharacterList;
    public static String upperCharacterList;
    public static boolean backspace;
    public static boolean shift;
    public static boolean control;

    public static int backspaceTimer;

    public Keyboard() {
        inputCharacterList = "1234567890-=[];',./ABCDEFGHIJKLMNOPQRSTUVWXYZSpace";
        upperCharacterList = "!@#$%^&*()_+{}:\"<>?";
        backspace = false;
        shift = false;
        control = false;

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
}
