package com.ideaout.im.service;

import com.ideaout.im.domain.Conversation;
import com.ideaout.im.domain.Message;
import com.ideaout.im.http.ResponseDataBase;
import com.ideaout.im.http.param.IdParam;
import com.ideaout.im.http.param.ListParam;
import com.ideaout.im.http.param.QueryMsgListParam;
import com.ideaout.im.http.param.SendMsgParam;

import java.util.List;

public interface MsgService {

    void sendMsg(SendMsgParam param, ResponseDataBase responseDataBase);
    int deleteMsg(IdParam param);
    List<Message> queryMsgList(QueryMsgListParam param);
    List<Conversation> queryConversationList(ListParam param);
    int deleteConversation(IdParam param);

    void realSendMsg(SendMsgParam sendMsgParam);
}
