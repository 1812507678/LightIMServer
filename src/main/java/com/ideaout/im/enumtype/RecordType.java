package com.ideaout.im.enumtype;

public enum RecordType {
    All(0, "公共类型"),
    BuyGoods(1, "购买商品"),
    EditPassword(2, "修改密码"),
    BindThirdPlatform(3, "绑定三方平台"),
    Refund(4, "退款"),
    ;

    public int value;
    public String desc;

    RecordType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static RecordType getRecordType(int value) {
        for (RecordType type : RecordType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return All;
    }
}