package com.ideaout.im.dao;

import com.ideaout.im.domain.Conversation;
import com.ideaout.im.domain.ConversationExample;
import java.util.List;

import com.ideaout.im.http.param.ListParam;
import org.apache.ibatis.annotations.Param;

public interface ConversationMapper {
    int countByExample(ConversationExample example);

    int deleteByExample(ConversationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Conversation record);

    int insertSelective(Conversation record);

    List<Conversation> selectByExample(ConversationExample example);

    Conversation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Conversation record, @Param("example") ConversationExample example);

    int updateByExample(@Param("record") Conversation record, @Param("example") ConversationExample example);

    int updateByPrimaryKeySelective(Conversation record);

    int updateByPrimaryKey(Conversation record);

    List<Conversation> queryConversationList(@Param("param") ListParam param);
}