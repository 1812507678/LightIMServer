package com.ideaout.im.util;

public interface Constant {
    long oneHourTimestamp = 60*60*1000;
    long oneDayTimestamp = oneHourTimestamp*24;
    long oneMonthTimestamp = oneDayTimestamp*30;

    String redisKey_TaskRecPrice = "redisKey_TaskRecPrice";
    String redisKey_Admin_Token_ = "redisKey_Admin_Token_";
    String redisKey_User_Token_ = "redisKey_User_Token_";

}
