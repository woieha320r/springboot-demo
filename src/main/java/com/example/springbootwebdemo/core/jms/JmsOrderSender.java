package com.example.springbootwebdemo.core.jms;

import cn.hutool.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * JMS发送方
 *
  * @date 2021-12-09
 * */
@Service
public class JmsOrderSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 发送json字符串
     */
    public void sendOrder(String destinationName, String msg) {
        jmsTemplate.convertAndSend(destinationName, msg, message -> {
            message.setStringProperty("ContentType", ContentType.JSON.getValue());
            return message;
        });
    }

}
