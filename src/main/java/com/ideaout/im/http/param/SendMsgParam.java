package com.ideaout.im.http.param;

public class SendMsgParam {
    public int fromUserId;
    public int toUserId;
    public int type;
    public String text;
    public String url;
    public String extra;
    public String userInfoExtra;
    public int conversationId;
    public String setUnreadType;
    public int conversationFromUserId;  //会话的fromUserId，根据这个id来判断会话未读消息数量
    public String fileUrl;
    public String fileSmallUrl;
}
