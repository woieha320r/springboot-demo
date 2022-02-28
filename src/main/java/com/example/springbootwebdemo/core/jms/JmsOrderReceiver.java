package com.example.springbootwebdemo.core.jms;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * JMS接收方
 *
  * @date 2021-12-09
 */
@Component
@Slf4j
public class JmsOrderReceiver {

    @Autowired
    private JmsTemplate jms;

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 拉模式，阻塞等待消息产生。需要在消息发送前就已经等待
     */
    public String receivePull() {
        String msg = (String) jms.receiveAndConvert("xxx.destination.name");
        log.info("拉模式接收到消息：" + JSONUtil.toJsonStr(msg));
        return msg;
    }

    /**
     * 推模式，监听器监听消息产生
     */
    @JmsListener(destination = "xxx.destination.name")
    public void receivePush(String msg) {
        log.info("jms推模式接收到消息：" + msg);
    }

    /**
     * 手动确认消息接收，失败
     */
    /*
    @JmsListener(destination = "xxx.destination.name", containerFactory = "myJmsListenerContainerFactory")
    public void receivePush(ActiveMQMessage msg, Session session) {
        try {
            System.out.println("手动确认：" + session.getAcknowledgeMode());
            System.out.println("事务：" + session.getTransacted());
            System.out.println("推模式接收到消息：" + JSONUtil.toJsonStr(msg));
            msg.acknowledge();
            System.out.println("推模式接收到消息：" + JSONUtil.toJsonStr(msg));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    */

}
