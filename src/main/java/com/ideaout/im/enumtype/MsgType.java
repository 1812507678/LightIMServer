package com.ideaout.im.enumtype;

public enum MsgType {
    Text(1, "文字"),
    Image(2, "图片"),
    Voice(3, "语音"),
    Video(4, "视频"),
    Other(5, "其他"),
    ;


    public int value;
    public String desc;

    MsgType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static MsgType getMsgType(int value) {
        MsgType[] orderTypes = MsgType.values();
        for (MsgType msgType : orderTypes) {
            if (msgType.value == value) {
                return msgType;
            }
        }
        return MsgType.Text;
    }
}
