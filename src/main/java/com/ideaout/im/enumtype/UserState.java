package com.ideaout.im.enumtype;

public enum  UserState {
    Normal(1, "默认状态"),
    Canceled(3, "已注销"),
    ;

    public int value;
    public String desc;

    UserState(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static UserState getUserState(int value) {
        for (UserState type : UserState.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return Normal;
    }
}
