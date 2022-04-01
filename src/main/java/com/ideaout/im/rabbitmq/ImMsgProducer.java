package com.ideaout.im.rabbitmq;

import com.ideaout.im.http.param.SendMsgParam;
import com.ideaout.im.util.GsonUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImMsgProducer {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void sendToQueue(SendMsgParam sendMsgParam) {
        System.out.println("sendDemoQueue:"+ GsonUtil.toJson(sendMsgParam));
        // 第一个参数为刚刚定义的队列名称
        this.rabbitTemplate.convertAndSend("ImMsgQueue", sendMsgParam);
    }
}
