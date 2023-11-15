package com.jbs.universe.screen.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.jbs.universe.components.Utility;

import java.util.ArrayList;

public class Console {
    public BitmapFont font;

    int consoleLines;
    int displayLine;
    ArrayList<Line> lineList;

    public Console() {
        font = new BitmapFont(Gdx.files.internal("Fonts/Consolas_18.fnt"), Gdx.files.internal("Fonts/Consolas_18.png"), false);

        consoleLines = 18;
        displayLine = 0;
        lineList = new ArrayList<Line>();
    }

    public void draw(FrameBuffer frameBuffer, SpriteBatch spriteBatch, Camera camera) {
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
            Utility.writeColor(line.label, line.colorCode, new int[]{3, 18 * i++}, font, new int[]{10, 18}, spriteBatch);
        }

        spriteBatch.end();
        frameBuffer.end();

        spriteBatch.begin();
        font.setColor(Color.WHITE);
        spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, 22, 0, 0,580, (consoleLines * 18) + 6, 1, 1, 0, 0, 0, 580, (consoleLines * 18) + 6, false, true);
        spriteBatch.end();
    }

    public void dispose() {
        font.dispose();
    }
}
