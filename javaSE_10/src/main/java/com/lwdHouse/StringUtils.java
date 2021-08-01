package com.lwdHouse;

public class StringUtils {
    public static String captialize(String str){
        if (str.length() == 0) return str;
//        return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
        return String.valueOf(str.charAt(0)).toUpperCase() + str.substring(1).toLowerCase();
    }
}
