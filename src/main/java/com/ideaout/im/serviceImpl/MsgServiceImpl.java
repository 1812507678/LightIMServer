package com.ideaout.im.serviceImpl;

import com.ideaout.im.dao.ConversationMapper;
import com.ideaout.im.dao.MessageMapper;
import com.ideaout.im.dao.UserMapper;
import com.ideaout.im.domain.Conversation;
import com.ideaout.im.domain.ConversationExample;
import com.ideaout.im.domain.Message;
import com.ideaout.im.enumtype.MsgType;
import com.ideaout.im.http.ResponseDataBase;
import com.ideaout.im.http.param.IdParam;
import com.ideaout.im.http.param.ListParam;
import com.ideaout.im.http.param.QueryMsgListParam;
import com.ideaout.im.http.param.SendMsgParam;
import com.ideaout.im.service.MsgService;
import com.ideaout.im.util.HttpUtil;
import com.ideaout.im.util.TextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class MsgServiceImpl implements MsgService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    ConversationMapper conversationMapper;


    @Override
    public void sendMsg(SendMsgParam param, ResponseDataBase responseDataBase) {
        String lastText = param.text;
        if (TextUtil.isEmpty(lastText)){
            lastText = "["+MsgType.getMsgType(param.type).desc+"]";
        }
        else {
            if (lastText.length()>8){
                lastText = lastText.substring(0,8);
                lastText += "...";
            }
        }

        boolean isFirstCreateConversation = false;
        if (param.conversationId<=0){
            //会话id为空，则有可能是第一次聊天
            //1.查询是否以前有聊天会话
            ConversationExample conversationExample = new ConversationExample();
            ConversationExample.Criteria criteria1 = conversationExample.createCriteria();
            criteria1.andFromUserIdEqualTo(param.fromUserId);
            criteria1.andToUserIdEqualTo(param.toUserId);

            ConversationExample.Criteria criteria2 = conversationExample.createCriteria();
            criteria2.andFromUserIdEqualTo(param.toUserId);
            criteria2.andToUserIdEqualTo(param.fromUserId);

            conversationExample.or(criteria2);
            List<Conversation> conversations = conversationMapper.selectByExample(conversationExample);
            if (!CollectionUtils.isEmpty(conversations)){
                param.conversationId = conversations.get(0).getId();
            }
            else {
                //第一次会话，建立新的会话
                Conversation conversation = new Conversation();
                conversation.setFromUserId(param.fromUserId);
                conversation.setToUserId(param.toUserId);
                conversation.setTimestamp(System.currentTimeMillis());
                conversation.setState(1);
                conversation.setToUnreadCount(1);
                conversation.setFromUnreadCount(0);
                conversation.setLastText(lastText);
                conversation.setExtra(param.userInfoExtra);
                conversationMapper.insert(conversation);
                param.conversationId = conversation.getId();
                isFirstCreateConversation = true;
            }
        }

        Message message = new Message();
        message.setType(param.type);
        message.setFromUserId(param.fromUserId);
        message.setToUserId(param.toUserId);
        message.setText(param.text);
        message.setExtra(param.extra);
        message.setConversationId(param.conversationId);
        message.setState(0);
        message.setTimestamp(System.currentTimeMillis());
        message.setDatetime(new Date());

        if (!TextUtil.isEmpty(param.fileUrl)){
            message.setFileUrl(param.fileUrl);
            message.setFileSmallUrl(param.fileSmallUrl);
        }

        int insert = messageMapper.insert(message);
        if (insert>0){
            //成功
            if(!isFirstCreateConversation){
                //更新会话
                Conversation c = getConversationByFromUserId(param.conversationFromUserId,param.conversationId);;
                c.setLastText(lastText);
                c.setTimestamp(System.currentTimeMillis());
                //c.setState(1);
                c.setExtra(param.userInfoExtra);
                if(param.conversationFromUserId<=0){
                    param.conversationFromUserId = c.getFromUserId();
                }

                //设置未读消息数量
                if (param.fromUserId == param.conversationFromUserId){
                    c.setToUnreadAddCount(1);  //未读消息数量+1
                }
                else {
                    c.setFromUnreadAddCount(1);  //未读消息数量+1
                }
                conversationMapper.updateByPrimaryKeySelective(c);
            }
            responseDataBase.data = message;
        }
        else {
            responseDataBase.code = HttpUtil.ErrorDec.RequestError.value;
        }
    }

    @Override
    public int deleteMsg(IdParam param) {
        Message conversation = new Message();
        conversation.setId(param.id);
        conversation.setState(2); //已删除
        return messageMapper.updateByPrimaryKeySelective(conversation);
    }

    @Override
    public List<Message> queryMsgList(QueryMsgListParam param) {
        if (param.conversationId>0){
            Conversation conversation = getConversationByFromUserId(param.conversationFromUserId,param.conversationId);
            if(param.conversationFromUserId<=0){
                param.conversationFromUserId = conversation.getFromUserId();
            }
            //把当前查询者的未读消息设置为0
            if (param.userId == param.conversationFromUserId){
                conversation.setFromUnreadCount(0);
            }
            else {
                conversation.setToUnreadCount(0);
            }
            conversationMapper.updateByPrimaryKeySelective(conversation);
        }
        return messageMapper.queryMsgList(param);
    }

    @Override
    public List<Conversation> queryConversationList(ListParam param) {
        return conversationMapper.queryConversationList(param);
    }

    @Override
    public int deleteConversation(IdParam param) {
        Conversation conversation = new Conversation();
        conversation.setId(param.id);
        conversation.setState(2); //已删除
        return conversationMapper.updateByPrimaryKeySelective(conversation);
    }

    @Override
    public void realSendMsg(SendMsgParam sendMsgParam) {

    }

    //更加FromUserId返回会话对象
    private Conversation getConversationByFromUserId(int conversationFromUserId,int conversationId){
        Conversation conversation;
        if (conversationFromUserId>0){
            conversation = new Conversation();
            conversation.setId(conversationId);
        }
        else {
            conversation = conversationMapper.selectByPrimaryKey(conversationId);
        }
        return conversation;
    }


}
