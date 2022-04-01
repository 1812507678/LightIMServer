package com.ideaout.im.util.tencent_gzh;

public class GzhUserInfoProxy {
    private static GzhUserInfoProxy gzhUserInfoProxy;

    public static GzhUserInfoProxy getInstance() {
        if (gzhUserInfoProxy == null) {
            gzhUserInfoProxy = new GzhUserInfoProxy();
        }
        return gzhUserInfoProxy;
    }



}
