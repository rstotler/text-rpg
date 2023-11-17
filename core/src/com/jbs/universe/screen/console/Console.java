package com.jbs.universe.screen.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.jbs.universe.components.Keyboard;
import com.jbs.universe.components.Utility;

import java.util.ArrayList;

import static com.jbs.universe.components.Utility.wordWrap;

public class Console {
    int consoleLines;
    int lineCharacterCount;
    public int displayLine;
    ArrayList<ColorString> colorStringList;

    public Console() {
        consoleLines = 18;
        lineCharacterCount = 57;
        displayLine = 0;
        colorStringList = new ArrayList<ColorString>();
    }

    public void draw(FrameBuffer frameBuffer, SpriteBatch spriteBatch, BitmapFont font) {
        frameBuffer.begin();
        spriteBatch.begin();

        Gdx.gl.glClearColor(15/255f, 15/255f, 15/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int startIndex = displayLine;
        int endIndex = startIndex + consoleLines;
        if(endIndex > colorStringList.size()) {
            endIndex = colorStringList.size();
        }

        int i = 0;
        for(ColorString colorString : colorStringList.subList(startIndex, endIndex)) {
            if(colorString != null) {
                Utility.writeColor(colorString, new int[]{5, 18 * i}, font, new int[]{10, 18}, spriteBatch);
            }
            i++;
        }

        spriteBatch.end();
        frameBuffer.end();

        spriteBatch.begin();
        spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, 22, 0, 0,580, (consoleLines * 18) + 6, 1, 1, 0, 0, 0, 580, (consoleLines * 18) + 6, false, true);
        spriteBatch.end();
    }

    public void write(ColorString colorString, boolean blankCheck) {
        if(blankCheck && !(colorStringList.size() > 0 && colorStringList.get(0) == null)) {
            colorStringList.add(0, null);
        }

        if(colorString.label.length() <= lineCharacterCount) {
            colorStringList.add(0, colorString);
        } else {
            for(ColorString splitColorString : wordWrap(colorString, lineCharacterCount)) {
                colorStringList.add(0, splitColorString);
            }
        }
    }

    public void scroll(int yMod) {
        if(colorStringList.size() > consoleLines) {
            int scrollMod = 1;
            if(Keyboard.control) {
                scrollMod = 6;
            }
            displayLine += yMod * scrollMod;
            if(displayLine < 0) {
                displayLine = 0;
            } else if(displayLine > colorStringList.size() - consoleLines) {
                displayLine = colorStringList.size() - consoleLines;
            }
        }
    }
}
