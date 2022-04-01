package com.ideaout.im.enumtype;

public enum ImUserType {
    Visitor(1, "游客"),
    Admin(2, "管理员"),
    LoginUser(3, "已登录用户"),
    ;


    public int value;
    public String desc;

    ImUserType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ImUserType getImUserType(int value) {
        for (ImUserType type : ImUserType.values()) {
            if (type.value ==value) {
                return type;
            }
        }
        return Visitor;
    }
}
