package com.ideaout.im.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * @anthor haijun
 * @project name: Shop
 * @class name：com.haijun.shop.util
 * @time 2018-02-28 2:01 PM
 * @describe
 */
public class FormatUtil {

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


    //判断字符串是否为金钱格式的方法:
    public static boolean isMoney(String str) {
        try {
            Float.parseFloat(str);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public static String getSpecialFormatTime(String stringFormat, Date date) {
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        SimpleDateFormat format = new SimpleDateFormat(stringFormat, Locale.CHINA);  //07-12 15:10
        return format.format(date);
    }

    public static String getFormatTime(Date date) {
        return getSpecialFormatTime("yyyy/MM/dd HH:mm:ss", date);
    }

    public static String getFormatTime() {
        return getFormatTime(new Date());
    }


    public static java.sql.Date getSqlDate(){
        Date date = new Date();
        return new java.sql.Date(date.getTime());
    }

    //获取当前时间的前days天
    public static Date getPreDayDate(int days){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -days);
        return c.getTime();
    }


    /**
     * 判断时间是否在时间段内
     *
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime,
                                         Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean judgeIsBelongTime(String startTimeStr,String endTimeStr) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
        Date now = null;
        Date beginTime = null;
        Date endTime = null;
        try {
            now = df.parse(df.format(new Date()));
            beginTime = df.parse(startTimeStr);
            endTime = df.parse(endTimeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return belongCalendar(now, beginTime, endTime);
    }
}
