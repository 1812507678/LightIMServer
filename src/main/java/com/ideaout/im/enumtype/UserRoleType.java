package com.ideaout.im.enumtype;

public enum UserRoleType {
    Visitor(1, "游客"),
    Admin(2, "管理员"),
    IMUser(3, "IM用户"),
    ;


    public int value;
    public String desc;

    UserRoleType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static UserRoleType getUserRoleType(int value) {
        UserRoleType[] orderTypes = UserRoleType.values();
        for (UserRoleType msgType : orderTypes) {
            if (msgType.value == value) {
                return msgType;
            }
        }
        return Visitor;
    }
}
