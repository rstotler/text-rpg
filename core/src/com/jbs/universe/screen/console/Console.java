package com.jbs.universe.screen.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.jbs.universe.components.Keyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.jbs.universe.components.Utility.stringIsNumber;

public class Console {
    static Map<String, Color> colorMap;

    FrameBuffer frameBuffer;

    static int consoleLines;
    static int lineCharacterCount;
    public static int displayLine;
    static ArrayList<ColorString> colorStringList;

    static boolean drawPatternCheck;
    static String patternColor;

    static boolean redrawCheck;

    public Console() {
        colorMap = new HashMap<String, Color>() {{
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
            put("w", new Color(220/255f, 220/255f, 220/255f, 1));
            put("dw", new Color(150/255f, 150/255f, 150/255f, 1));
            put("ddw", new Color(120/255f, 120/255f, 120/255f, 1));
            put("dddw", new Color(70/255f, 70/255f, 70/255f, 1));
            put("la", new Color(150/255f, 150/255f, 150/255f, 1));
            put("a", new Color(150/255f, 150/255f, 150/255f, 1));
            put("da", new Color(120/255f, 120/255f, 120/255f, 1));
            put("dda", new Color(70/255f, 70/255f, 70/255f, 1));
            put("ddda", new Color(35/255f, 35/255f, 35/255f, 1));
            put("x", new Color(0/255f, 0/255f, 0/255f, 1));
        }};

        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 800, 600, false);

        consoleLines = 18;
        lineCharacterCount = 57;
        displayLine = 0;
        colorStringList = new ArrayList<ColorString>();

        redrawCheck = true;
    }

    public void draw(SpriteBatch spriteBatch, BitmapFont font) {
        if(redrawCheck) {
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
                    writeColor(colorString, new int[]{5, 18 * i}, font, new int[]{10, 18}, spriteBatch);
                }
                i++;
            }

            spriteBatch.end();
            frameBuffer.end();

            spriteBatch.begin();
            spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, 22, 0, 0,580, (consoleLines * 18) + 6, 1, 1, 0, 0, 0, 580, (consoleLines * 18) + 6, false, true);
            spriteBatch.end();

            redrawCheck = false;
        }

        spriteBatch.begin();
        spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, 22, 0, 0,580, (consoleLines * 18) + 6, 1, 1, 0, 0, 0, 580, (consoleLines * 18) + 6, false, true);
        spriteBatch.end();
    }

    public void write(ColorString colorString, boolean blankCheck) {
        if(blankCheck && !(!colorStringList.isEmpty() && colorStringList.get(0) == null)) {
            colorStringList.add(0, null);
        }

        if(colorString.label.length() <= lineCharacterCount) {
            colorStringList.add(0, colorString);
        } else {
            for(ColorString splitColorString : wordWrap(colorString, lineCharacterCount)) {
                colorStringList.add(0, splitColorString);
            }
        }

        displayLine = 0;
        redrawCheck = true;
    }

    // Utility Functions //
    public static void writeColor(ColorString colorString, int[] location, BitmapFont font, int[] fontSize, SpriteBatch spriteBatch) {
        String targetColor = "";
        int colorCount = 0;
        int printIndex = 0;
        int displayX = location[0];
        boolean writeCheck = false;
        location[1] += fontSize[1];

        for(int i = 0; i < colorString.colorCode.length(); i++) {
            String letter = String.valueOf(colorString.colorCode.charAt(i));

            // Sort //
            if(stringIsNumber(letter)) {
                if(colorCount != 0) {
                    colorCount *= 10;
                }
                colorCount += Integer.parseInt(letter);
            }
            else {
                targetColor = targetColor.concat(letter);
                if(colorString.colorCode.length() > i + 1 && stringIsNumber(String.valueOf(colorString.colorCode.charAt(i + 1)))) {
                    writeCheck = true;
                }
            }

            // Write Check //
            if(i + 1 == colorString.colorCode.length()) {
                writeCheck = true;
            }

            // Write //
            if(writeCheck) {
                int loopCount = 1;
                drawPatternCheck = false;

                if(colorMap.containsKey(targetColor)) {
                    font.setColor(colorMap.get(targetColor));
                }
                else if(targetColor.contains("-")) {
                    drawPatternCheck = true;

                    // Pattern Setup //
                    if(targetColor.substring(0, targetColor.indexOf('-')).equals("shim")) {
                        loopCount = 4;
                    } else if(targetColor.substring(0, targetColor.indexOf('-')).equals("alt")) {
                        loopCount = 2;
                    }
                } else {
                    font.setColor(Color.WHITE);
                }

                int endIndex = printIndex + colorCount;
                if(endIndex > colorString.label.length()) {
                    endIndex = colorString.label.length();
                }
                String textString = colorString.label.substring(printIndex, endIndex);

                for(int patternIndex = 0; patternIndex < loopCount; patternIndex++) {
                    String patternString = textString;

                    // Update Pattern //
                    if(drawPatternCheck) {
                        if(targetColor.substring(0, targetColor.indexOf('-')).equals("shim")) {
                            if(patternIndex == 0) {
                                patternColor = targetColor.substring(targetColor.indexOf('-') + 1);
                                if(colorMap.containsKey("l" + patternColor)) {
                                    font.setColor(colorMap.get("l" + patternColor));
                                }
                                patternString = patternString.substring(0, 1);
                            }

                            else {
                                patternString = "";
                                for(int blankIndex = 0; blankIndex < patternIndex; blankIndex++) {
                                    patternString += " ";
                                }

                                int charCount = (int) Math.ceil((textString.length() - patternIndex) / 3.0);
                                for(int charIndex = 0; charIndex < charCount; charIndex++) {
                                    int startIndex = patternIndex + (charIndex * (loopCount - 1));
                                    String targetChar = textString.substring(startIndex, startIndex + 1);
                                    patternString += targetChar;
                                    for(int blankIndex = 0; blankIndex < (loopCount - 2); blankIndex++) {
                                        patternString += " ";
                                    }
                                }

                                if(patternIndex == 1) {
                                    font.setColor(colorMap.get("d" + patternColor));
                                } else if(patternIndex == 2) {
                                    font.setColor(colorMap.get("dd" + patternColor));
                                } else if(patternIndex == 3) {
                                    font.setColor(colorMap.get("ddd" + patternColor));
                                }
                            }
                        }

                        else if(targetColor.substring(0, targetColor.indexOf('-')).equals("alt")) {
                            patternColor = targetColor.substring(targetColor.indexOf('-') + 1, targetColor.indexOf('-') + 2);
                            if(patternIndex == 0) {
                                patternString = "";
                                if(colorMap.containsKey("d" + patternColor)) {
                                    font.setColor(colorMap.get("d" + patternColor));
                                }
                            } else {
                                patternString = " ";
                                if(colorMap.containsKey(patternColor)) {
                                    font.setColor(colorMap.get(patternColor));
                                }
                            }

                            for(int c = 0; c < Math.ceil(textString.length() / 2.0); c++) {
                                int charIndex = (c * 2) + patternIndex;
                                if(charIndex < textString.length()) {
                                    patternString += textString.charAt(charIndex) + " ";
                                }
                            }
                        }
                    }
                    font.draw(spriteBatch, patternString, displayX, location[1]);
                }
                printIndex += colorCount;
                if(printIndex == colorString.label.length()) {
                    return;
                }

                displayX += fontSize[0] * textString.length();
                colorCount = 0;
                targetColor = "";
                writeCheck = false;
            }
        }
    }

    public static ArrayList<ColorString> wordWrap(ColorString colorString, int maxWidth) {
        class WordWrapUtility {
            public int getNextColorLength(String colorCode) {
                String nextColorLengthString = "";
                boolean start = false;
                for(char c : colorCode.toCharArray()) {
                    if(stringIsNumber(String.valueOf(c))) {
                        start = true;
                        nextColorLengthString += c;
                    } else if(start) {
                        return Integer.parseInt(nextColorLengthString);
                    }
                }
                return -1;
            }

            public String[] getNextColor(String colorCode) {
                String nextColorString = "";
                boolean start = false;
                for(int i = 0; i < colorCode.length(); i++) {
                    char c = colorCode.charAt(i);
                    if(!stringIsNumber(String.valueOf(c))) {
                        start = true;
                        nextColorString += c;
                    } else if(start) {
                        String[] nextColorData = new String[2];
                        nextColorData[0] = colorCode.substring(i);
                        nextColorData[1] = nextColorString;
                        return nextColorData;
                    }
                    if(start && i == colorCode.length() - 1) {
                        String[] nextColorData = new String[2];
                        nextColorData[0] = colorCode.substring(i - 1);
                        nextColorData[1] = nextColorString;
                        return nextColorData;
                    }
                }
                return new String[] {"", ""};
            }
        }

        ArrayList<ColorString> colorStringList = new ArrayList<>();
        colorStringList.add(new ColorString("", ""));
        for(String word : colorString.label.split(" ")) {
            if(word.length() + colorStringList.get(colorStringList.size() - 1).label.length() > maxWidth) {
                colorStringList.add(new ColorString("", ""));
            }
            colorStringList.get(colorStringList.size() - 1).label += word + " ";
        }

        WordWrapUtility wordWrapUtility = new WordWrapUtility();
        int currentColorLength = wordWrapUtility.getNextColorLength(colorString.colorCode);
        String[] nextColorData = wordWrapUtility.getNextColor(colorString.colorCode);
        for(ColorString splitColorString : colorStringList) {
            int currentCount = 0;
            for(int i = 0; i < splitColorString.label.length(); i++) {
                currentCount++;
                if(currentCount == currentColorLength) {
                    splitColorString.colorCode += currentCount + nextColorData[1];
                    currentColorLength = wordWrapUtility.getNextColorLength(nextColorData[0]);
                    nextColorData = wordWrapUtility.getNextColor(nextColorData[0]);
                    currentCount = 0;
                } else if(i == splitColorString.label.length() - 1) {
                    splitColorString.colorCode += currentCount + nextColorData[1];
                    currentColorLength -= currentCount;
                    currentCount = 0;
                }
            }
        }
        return colorStringList;
    }

    public ColorString getUnderlineColorString(String label) {
        float endPercent = .20f;
        int dashCount = (int) (label.length() * endPercent);
        String underlineString = "";
        for(int i = 0; i < label.length(); i++) {
            if(i < dashCount || i >= label.length() - dashCount) {
                underlineString += "-";
            } else {
                underlineString += "=";
            }
        }
        String underlineCode = underlineString.length() + "alt-y";

        return new ColorString(underlineString, underlineCode);
    }

    public static void scroll(int yMod) {
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
