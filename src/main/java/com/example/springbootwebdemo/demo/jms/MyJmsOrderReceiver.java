package com.example.springbootwebdemo.demo.jms;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * JMS接收方
 *
  * @date 2021-12-09
 */
@Component
@Slf4j
public class MyJmsOrderReceiver {

    @Autowired
    private JavaMailSender javaMailSender;

    @JmsListener(destination = "sbwd-mail")
    public void receiveSendLoginMailMsg(String msg) {
        log.info("jms推模式接收到发送登录提醒邮件信息 >>> " + msg);
        SimpleMailMessage simpleMailMessage = JSONUtil.toBean(msg, SimpleMailMessage.class);
        simpleMailMessage.setTo(JSONUtil.parseObj(msg).getJSONArray("to").toArray(new String[0]));
        javaMailSender.send(simpleMailMessage);
    }

}
