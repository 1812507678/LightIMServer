package com.ideaout.im.util;

public class CacheUtil {
    public static String getAdminTokenRedisKey(int adminId,int clientType){
        return Constant.redisKey_Admin_Token_ + adminId+clientType;
    }

    public static String getImUserTokenRedisKey(int userId, int clientType){
        return Constant.redisKey_User_Token_ + userId+clientType;
    }
}
