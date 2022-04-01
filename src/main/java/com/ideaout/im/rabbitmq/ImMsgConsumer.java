package com.ideaout.im.rabbitmq;

import com.ideaout.im.http.param.SendMsgParam;
import com.ideaout.im.service.MsgService;
import com.ideaout.im.util.GsonUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@RabbitListener(queues = "ImMsgQueue")
public class ImMsgConsumer {

    @Autowired
    MsgService msgService;

    /**
     * 消息消费
     * @RabbitHandler 代表此方法为接受到消息后的处理方法
     */
    @RabbitHandler
    public void recieved(SendMsgParam sendMsgParam) {
        System.out.println("[ImMsgQueue] recieved " + GsonUtil.toJson(sendMsgParam));
        msgService.realSendMsg(sendMsgParam);
    }

}