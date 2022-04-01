package com.ideaout.im.http.entity;

public class AppStaResult {
    public String appKey;
    public String appSecret;
    public Integer state;
    public String name;
    public Long timestamp;

    public int allUserCount;
    public int monthUserCount;

    public int monthMsgCount;
    public int dayMsgCount;

    public AppStaResult(String appKey, String appSecret, Integer state, String name, Long timestamp) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.state = state;
        this.name = name;
        this.timestamp = timestamp;
    }
}
