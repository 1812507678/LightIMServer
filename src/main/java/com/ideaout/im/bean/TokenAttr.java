package com.ideaout.im.bean;

public class TokenAttr {
    public Integer userRole;  //用户角色: 游客/管理员/im用户
    public Integer userId;  //用户id
    public Integer clientType; //终端设备类型
    public Integer userType; //用户类型
    public String deviceUniqueId; //设备id

    public TokenAttr(Integer userId, Integer appType, Integer userType) {
        this.userId = userId;
        this.clientType = appType;
        this.userType = userType;
    }

    public TokenAttr(Integer userRole, Integer userId, Integer clientType, Integer userType, String deviceUniqueId) {
        this.userRole = userRole;
        this.userId = userId;
        this.clientType = clientType;
        this.userType = userType;
        this.deviceUniqueId = deviceUniqueId;
    }
}
