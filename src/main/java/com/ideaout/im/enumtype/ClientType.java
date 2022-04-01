package com.ideaout.im.enumtype;

public enum ClientType {
    PC(1, "PC"),
    H5(2, "h5"),
    ANDROID(3, "android"),
    IOS(4, "ios"),
    ;


    public int value;
    public String desc;

    ClientType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ClientType getAppType(int value) {
        ClientType[] orderTypes = ClientType.values();
        for (ClientType msgType : orderTypes) {
            if (msgType.value == value) {
                return msgType;
            }
        }
        return PC;
    }
}
