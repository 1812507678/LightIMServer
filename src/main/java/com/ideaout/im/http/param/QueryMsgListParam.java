package com.ideaout.im.http.param;

public class QueryMsgListParam extends ListParam {
    public int conversationId;  //会话id
    public String setUnreadType;  //1.fromUser 2.toUser

    public int otherUserId;
    public int conversationFromUserId;  //会话的fromUserId，根据这个id来判断会话未读消息数量
}
