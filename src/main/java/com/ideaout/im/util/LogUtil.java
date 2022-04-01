package com.ideaout.im.util;

public class LogUtil {

    private static boolean isDebug = true;

    public static void i(String info) {
        if (isDebug) {
            System.out.println(info);
        }
    }
}
