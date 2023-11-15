package com.jbs.universe.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jbs.universe.screen.console.Line;

import java.util.HashMap;
import java.util.Map;

public class Utility {
    static Map<String, Color> colorMap = new HashMap<String, Color>() {{
        put("lr", new Color(255/255f, 80/255f, 80/255f, 1));
        put("r", new Color(255/255f, 0/255f, 0/255f, 1));
        put("dr", new Color(145/255f, 0/255f, 0/255f, 1));
        put("ddr", new Color(80/255f, 0/255f, 0/255f, 1));
        put("dddr", new Color(40/255f, 0/255f, 0/255f, 1));
        put("lo", new Color(255/255f, 150/255f, 75/255f, 1));
        put("o", new Color(255/255f, 100/255f, 0/255f, 1));
        put("do", new Color(170/255f, 95/255f, 0/255f, 1));
        put("ddo", new Color(80/255f, 40/255f, 0/255f, 1));
        put("dddo", new Color(40/255f, 20/255f, 0/255f, 1));
        put("ly", new Color(255/255f, 255/255f, 80/255f, 1));
        put("y", new Color(255/255f, 255/255f, 0/255f, 1));
        put("dy", new Color(145/255f, 145/255f, 0/255f, 1));
        put("ddy", new Color(80/255f, 80/255f, 0/255f, 1));
        put("dddy", new Color(40/255f, 40/255f, 0/255f, 1));
        put("lg", new Color(80/255f, 255/255f, 80/255f, 1));
        put("g", new Color(0/255f, 255/255f, 0/255f, 1));
        put("dg", new Color(0/255f, 145/255f, 0/255f, 1));
        put("ddg", new Color(0/255f, 80/255f, 0/255f, 1));
        put("dddg", new Color(0/255f, 40/255f, 0/255f, 1));
        put("lc", new Color(80/255f, 255/255f, 255/255f, 1));
        put("c", new Color(0/255f, 255/255f, 255/255f, 1));
        put("dc", new Color(0/255f, 145/255f, 145/255f, 1));
        put("ddc", new Color(0/255f, 80/255f, 80/255f, 1));
        put("dddc", new Color(0/255f, 40/255f, 40/255f, 1));
        put("lb", new Color(80/255f, 80/255f, 255/255f, 1));
        put("b", new Color(0/255f, 0/255f, 255/255f, 1));
        put("db", new Color(0/255f, 0/255f, 145/255f, 1));
        put("ddb", new Color(0/255f, 0/255f, 80/255f, 1));
        put("dddb", new Color(0/255f, 0/255f, 40/255f, 1));
        put("lv", new Color(255/255f, 80/255f, 255/255f, 1));
        put("v", new Color(255/255f, 0/255f, 255/255f, 1));
        put("dv", new Color(145/255f, 0/255f, 145/255f, 1));
        put("ddv", new Color(80/255f, 0/255f, 80/255f, 1));
        put("dddv", new Color(40/255f, 0/255f, 40/255f, 1));
        put("lm", new Color(175/255f, 80/255f, 255/255f, 1));
        put("m", new Color(175/255f, 0/255f, 255/255f, 1));
        put("dm", new Color(95/255f, 0/255f, 145/255f, 1));
        put("ddm", new Color(75/255f, 0/255f, 80/255f, 1));
        put("dddm", new Color(37/255f, 0/255f, 40/255f, 1));
        put("lw", new Color(255/255f, 255/255f, 255/255f, 1));
        put("w", new Color(255/255f, 255/255f, 255/255f, 1));
        put("dw", new Color(220/255f, 220/255f, 220/255f, 1));
        put("ddw", new Color(150/255f, 150/255f, 150/255f, 1));
        put("dddw", new Color(70/255f, 70/255f, 70/255f, 1));
        put("la", new Color(150/255f, 150/255f, 150/255f, 1));
        put("a", new Color(150/255f, 150/255f, 150/255f, 1));
        put("da", new Color(120/255f, 120/255f, 120/255f, 1));
        put("dda", new Color(70/255f, 70/255f, 70/255f, 1));
        put("ddda", new Color(35/255f, 35/255f, 35/255f, 1));
        put("x", new Color(0/255f, 0/255f, 0/255f, 1));
    }};

    public static void writeColor(Line line, int[] location, BitmapFont font, int[] fontSize, SpriteBatch spriteBatch) {
        String targetColor = "";
        int colorCount = 0;
        int printIndex = 0;
        int displayX = location[0];
        boolean writeCheck = false;
        location[1] += fontSize[1];

        for(int i = 0; i < line.colorCode.length(); i++) {
            String letter = String.valueOf(line.colorCode.charAt(i));

            // Sort //
            if(stringIsNumber(letter)) {
                if(colorCount != 0) {
                    colorCount *= 10;
                }
                colorCount += Integer.parseInt(letter);
            }
            else {
                targetColor = targetColor.concat(letter);
                if(line.colorCode.length() > i + 1 && stringIsNumber(String.valueOf(line.colorCode.charAt(i + 1)))) {
                    writeCheck = true;
                }
            }

            // Write Check //
            if(i + 1 == line.colorCode.length()) {
                writeCheck = true;
            }

            // Write //
            if(writeCheck) {
                if(colorMap.containsKey(targetColor)) {
                    font.setColor(colorMap.get(targetColor));
                }
                else {
                    font.setColor(Color.WHITE);
                }

                int endIndex = printIndex + colorCount;
                if(endIndex > line.label.length()) {
                    endIndex = line.label.length();
                }
                String textString = line.label.substring(printIndex, endIndex);
                font.draw(spriteBatch, textString, displayX, location[1]);

                printIndex += colorCount;
                if(printIndex == line.label.length()) {
                    return;
                }

                displayX += fontSize[0] * textString.length();
                colorCount = 0;
                targetColor = "";
                writeCheck = false;
            }
        }
    }

    public static boolean stringIsNumber(String strNum) {
        try {
            int num = Integer.parseInt(strNum);
        }
        catch(Exception e) {
            return false;
        }
        return true;
    }
}
