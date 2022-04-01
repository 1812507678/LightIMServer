package com.ideaout.im.util;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {
    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }


    public static String getStringID() {
        return UUID.randomUUID().toString();
    }

    public static Long getLongID() {
        return System.currentTimeMillis();
    }


    //判断字符串是否为数字的方法:
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String getOrderSerialNumber() {
        return FormatUtil.getSpecialFormatTime("yyyyMMddHHmmssSSS", new Date());
    }

    /**
     * 生成订单号（25位）：时间（精确到毫秒）+5位随机数+5位用户id
     */
    public static String getOrderNum(int userId) {
        //时间（精确到毫秒）
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String localDate = LocalDateTime.now().format(ofPattern);
        //5位随机数
        String randomNumeric = RandomStringUtils.randomNumeric(5);
        //5位用户id
        int subStrLength = 5;
        String sUserId = String.valueOf(userId);
        int length = sUserId.length();
        String str;
        if (length >= subStrLength) {
            str = sUserId.substring(length - subStrLength, length);
        }
        else {
            str = String.format("%0" + subStrLength + "d", userId);
        }
        return localDate + randomNumeric + str;
    }

    /**
     * 过滤非数字
     * @param str
     * @return
     */
    public static String getNumeric(String str) {
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }



}
