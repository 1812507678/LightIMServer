package com.ideaout.im.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

//金额转换工具类
public class MoneyUtils {

    /**
     * 格式化金额为特定长度
     */
    public static String formatMoney(Double s, int length) {
        if (s == null) {
            return "";
        }
        NumberFormat formater = null;

        if (length == 0) {
            formater = new DecimalFormat("###,###");
        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("###,###.");
            for (int i = 0; i < length; i++) {
                buff.append("#");
            }
            formater = new DecimalFormat(buff.toString());
        }
        String result = formater.format(s);
        if (result.indexOf(".") == -1) {

            if (length == 1)
                result = "￥" + result + ".0";
            else
                result = "￥" + result + ".00";

        } else {
            if (result.substring(result.indexOf('.') + 1).length() == 1) {
                result = "￥" + result + "0";
            } else {
                result = "￥" + result;
            }
        }
        return result;
    }

    public static final String MONEY_REGULAR = "\\d+(\\.\\d+)?";
    public static final String MONEY_FLOAT_REGULAR = "(\\.\\d+)?";

    /**
     * 格式化金额为两位小数点
     */
    public static String formatMoney(String val) {
        if (!TextUtil.isEmpty(val)) {
            try {
                return formatMoney(Double.parseDouble(val), 2);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return "￥0.00";
    }

    /**
     * 格式化金额为两位小数点
     */
    public static String formatMoney1(String val) {
        if (!TextUtil.isEmpty(val) && val.matches(MONEY_REGULAR)) {
            return formatMoney(Double.parseDouble(val), 1);
        }
        return "￥0.00";
    }

    /**
     * 格式化金额为两位小数点
     */
    public static String formatMoney(double value) {
        return formatMoney(value, 2);
    }

    /**
     * 格式化金额为两位小数点
     */
    public static String formatMoneyWithoutSymbol(double value) {
        return formatMoney(value, 2).replace("￥","");
    }

    /**
     * 格式化金额为两位小数点
     */
    public static String formatMoneyWithoutSymbol(double value,int lngth) {
        return formatMoney(value, lngth).replace("￥","");
    }

    /**
     * 格式化金额为两位小数点
     */
    public static String formatMoneyWithoutSymbol(String value) {
        return formatMoney(value).replace("￥","");
    }

    public static double doubelMoney(double value) {
        BigDecimal b = new BigDecimal(value);
        value = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return value;
    }
}
