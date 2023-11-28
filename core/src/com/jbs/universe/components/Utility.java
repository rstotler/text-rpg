package com.jbs.universe.components;

import java.util.*;

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
}
