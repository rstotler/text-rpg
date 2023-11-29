package com.jbs.universe.components;

import java.util.ArrayList;
import java.util.Arrays;

public class Utility {
    public static boolean stringIsNumber(String strNum) {
        try {
            Integer.parseInt(strNum);
        }
        catch(Exception e) {
            return false;
        }
        return true;
    }

    public static String getDirectionString(String targetDirection) {
        targetDirection = targetDirection.toLowerCase();
        if(targetDirection.charAt(0) == 'n') {
            return "North";
        } else if(targetDirection.charAt(0) == 'e') {
            return "East";
        } else if(targetDirection.charAt(0) == 's') {
            return "South";
        } else if(targetDirection.charAt(0) == 'w') {
            return "West";
        } else if(targetDirection.charAt(0) == 'u') {
            return "Up";
        } else {
            return "Down";
        }
    }

    public static String getOppositeDirection(String targetDirection) {
        targetDirection = targetDirection.toLowerCase();
        if(targetDirection.charAt(0) == 'n') {
            return "South";
        } else if(targetDirection.charAt(0) == 'e') {
            return "West";
        } else if(targetDirection.charAt(0) == 's') {
            return "North";
        } else if(targetDirection.charAt(0) == 'w') {
            return "East";
        } else if(targetDirection.charAt(0) == 'u') {
            return "Down";
        } else {
            return "Up";
        }
    }

    public static String combineStringArray(String[] stringArray) {
        String returnString = "";
        for(int i = 0; i < stringArray.length; i++) {
            returnString += stringArray[i];
            if(i < stringArray.length - 1) {
                returnString += " ";
            }
        }
        return returnString;
    }

    public static ArrayList<String> createKeyList(String targetString) {
        ArrayList<String> keyList = new ArrayList<>();

        targetString = targetString.toLowerCase();
        for(int i = 0; i < targetString.split(" ").length; i++) {
            String[] splitString = targetString.split(" ");
            String keyString = combineStringArray(Arrays.copyOfRange(splitString, targetString.split(" ").length - i - 1, targetString.split(" ").length));

            if(!keyString.isEmpty()) {
                int keyStringCount = keyString.length() - 1;
                for (int ii = 0; ii < keyStringCount; ii++) {
                    String keySubString = keyString.substring(0, 1 + ii);
                    if(!keyList.contains(keySubString)) {
                        keyList.add(keySubString);
                    }
                }
            }
        }

        return keyList;
    }
}
