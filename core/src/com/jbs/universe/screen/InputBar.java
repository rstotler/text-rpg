package com.jbs.universe.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.jbs.universe.components.Keyboard;
import com.jbs.universe.components.Utility;
import com.jbs.universe.screen.console.Line;

import java.util.ArrayList;

public class InputBar {
    String inputString;
    int cursorTimer;

    int previousInputIndex;
    ArrayList<String> previousInputList;

    public InputBar() {
        inputString = "";
        cursorTimer = 0;

        previousInputIndex = -1;
        previousInputList = new ArrayList<String>();
    }

    public void update() {
        if(!inputString.isEmpty() && Keyboard.backspaceTimer == 0) {
            inputString = inputString.substring(0, inputString.length() - 1);
        }
    }

    public void draw(FrameBuffer frameBuffer, SpriteBatch spriteBatch, BitmapFont font) {
        frameBuffer.begin();
        spriteBatch.begin();

        Gdx.gl.glClearColor(10/255f, 25/255f, 50/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        String displayString = inputString;
        if(displayString.length() > 54) {
            displayString = displayString.substring(displayString.length() - 54);
        }
        displayString = "> ".concat(displayString);
        if(cursorTimer >= 30) {
            displayString = displayString.concat("_");
        }
        String displayColorCode = "2y".concat(String.valueOf(displayString.length() + 1)).concat("w");
        Utility.writeColor(new Line(displayString, displayColorCode), new int[]{5, 1}, font, new int[]{10, 18}, spriteBatch);

        spriteBatch.end();
        frameBuffer.end();

        spriteBatch.begin();
        spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, 0, 0, 0,580, 22, 1, 1, 0, 0, 0, 580, 22, false, true);
        spriteBatch.end();

        // Update Cursor Timer //
        cursorTimer++;
        if(cursorTimer >= 60) {
            cursorTimer = 0;
        }
    }

    public void inputKey(String key, boolean shift) {
        if("1234567890-=[];',./".contains(key) && shift) {
            int keyIndex = Keyboard.inputCharacterList.indexOf(key);
            key = Keyboard.upperCharacterList.substring(keyIndex, keyIndex + 1);
        } else if(key.equals("Space")) {
            key = " ";
        } else if(!shift) {
            key = key.toLowerCase();
        }

        inputString = inputString.concat(key);
    }

    public void scrollInput(String key) {
        if(key.equals("Up")) {
            if(!Keyboard.control && previousInputIndex < previousInputList.size() - 1) {
                previousInputIndex++;
                inputString = previousInputList.get(previousInputIndex);
                cursorTimer = 0;
            }
        }

        else if(key.equals("Down")) {
            if(!Keyboard.control && previousInputIndex > -1) {
                previousInputIndex--;
                cursorTimer = 0;
                if(previousInputIndex > -1) {
                    inputString = previousInputList.get(previousInputIndex);
                } else {
                    inputString = "";
                }
            }
        }
    }

    public String enterInput() {
        String returnString = "";

        if(!inputString.isEmpty()) {
            inputString = inputString.trim();
            if(!inputString.isEmpty()) {
                returnString = inputString;
                if(previousInputList.isEmpty() || !inputString.equals(previousInputList.get(0))) {
                    previousInputList.add(0, inputString);
                    if(previousInputList.size() > 20) {
                        previousInputList.remove(previousInputList.size() - 1);
                    }
                }
            }

            inputString = "";
            previousInputIndex = -1;
        }
        return returnString;
    }
}
