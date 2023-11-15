package com.jbs.universe.screen.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.jbs.universe.components.Utility;

import java.util.ArrayList;

public class Console {
    int consoleLines;
    int lineCharacterCount;
    int displayLine;
    ArrayList<Line> lineList;

    public Console() {
        consoleLines = 18;
        lineCharacterCount = 57;
        displayLine = 0;
        lineList = new ArrayList<Line>();
    }

    public void draw(FrameBuffer frameBuffer, SpriteBatch spriteBatch, BitmapFont font) {
        frameBuffer.begin();
        spriteBatch.begin();

        Gdx.gl.glClearColor(15/255f, 15/255f, 15/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int startIndex = displayLine;
        int endIndex = startIndex + consoleLines;
        if(endIndex > lineList.size()) {
            endIndex = lineList.size();
        }

        int i = 0;
        for(Line line : lineList.subList(startIndex, endIndex)) {
            if(line != null) {
                Utility.writeColor(line, new int[]{5, 18 * i}, font, new int[]{10, 18}, spriteBatch);
            }
            i++;
        }

        spriteBatch.end();
        frameBuffer.end();

        spriteBatch.begin();
        spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, 22, 0, 0,580, (consoleLines * 18) + 6, 1, 1, 0, 0, 0, 580, (consoleLines * 18) + 6, false, true);
        spriteBatch.end();
    }

    public void write(Line line, boolean blankCheck) {
        if(blankCheck && !(lineList.size() > 0 && lineList.get(0) == null)) {
            lineList.add(0, null);
        }

        if(line.label.length() <= lineCharacterCount) {
            lineList.add(0, line);
        } else {
            // Word-Wrap
        }
    }
}
