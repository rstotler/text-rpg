package com.jbs.universe.screen.inputbar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.jbs.universe.GameMain;
import com.jbs.universe.components.Keyboard;
import com.jbs.universe.gamedata.player.Player;
import com.jbs.universe.gamedata.world.galaxy.Galaxy;
import com.jbs.universe.screen.console.ColorString;

import java.util.ArrayList;

import static com.jbs.universe.screen.console.Console.writeColor;

public class InputBar {
    FrameBuffer frameBuffer;

    String inputString;
    int cursorTimer;

    ArrayList<String> previousInputList;
    int previousInputIndex;

    ArrayList<String> inputList;
    int inputListTimer;

    boolean redrawCheck = true;

    public InputBar() {
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 800, 600, false);

        inputString = "";
        cursorTimer = 0;

        previousInputList = new ArrayList<String>();
        previousInputIndex = -1;

        inputList = new ArrayList<String>();
        inputListTimer = 999;
    }

    public void update(GameMain game) {
        cursorTimer++;
        if(cursorTimer >= 60) {
            cursorTimer = 0;
            redrawCheck = true;
        } else if(cursorTimer == 30) {
            redrawCheck = true;
        }

        if(!inputList.isEmpty()) {
            inputListTimer++;
            if(inputListTimer >= 3) {
                inputListTimer = 0;
                game.processInput(inputList.get(0));
                game.console.displayLine = 0;
                inputList.remove(0);
            }
        }

        // Backspace //
        if(!inputString.isEmpty() && Keyboard.backspaceTimer == 0) {
            inputString = inputString.substring(0, inputString.length() - 1);
            redrawCheck = true;
        }
    }

    public void draw(SpriteBatch spriteBatch, BitmapFont font, ArrayList<Galaxy> galaxyList, Player player) {
        if(redrawCheck) {
            frameBuffer.begin();
            spriteBatch.begin();

            Gdx.gl.glClearColor(10/255f, 25/255f, 50/255f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            String carrotColor = "y";
            String displayString = inputString;
            if(displayString.length() > 54) {
                displayString = displayString.substring(displayString.length() - 54);
            }
            displayString = "> ".concat(displayString);
            if(cursorTimer < 30) {
                displayString = displayString.concat("_");
            }
            String displayColorCode = "2".concat(carrotColor).concat(String.valueOf(displayString.length() + 1)).concat("w");
            writeColor(new ColorString(displayString, displayColorCode), new int[]{5, 1}, font, new int[]{10, 18}, spriteBatch);

            spriteBatch.end();
            frameBuffer.end();

            redrawCheck = false;
        }

        spriteBatch.begin();
        spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, 0, 0, 0,580, 22, 1, 1, 0, 0, 0, 580, 22, false, true);
        spriteBatch.end();
    }

    // Utility Functions //
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
        redrawCheck = true;
    }

    public void handleInput(String key) {
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

        redrawCheck = true;
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
            redrawCheck = true;
        }

        return returnString;
    }
}
