package com.ideaout.im.util;

public class UserUtil {
    public static final int multipleApplyUserCount = 13;

    public enum ThirdLoginWay {
        NotSupport(-1, "不支持的登录方式"), WeiXin(1, "微信"), qq(2, "qq"), Weibo(3, "微博");

        public int value;
        public String desc;

        ThirdLoginWay(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public static ThirdLoginWay getLoginTypeByCode(int value) {
            for (ThirdLoginWay type : ThirdLoginWay.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return NotSupport;
        }
    }


}
