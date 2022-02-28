package com.example.springbootwebdemo.core.jms;

import cn.hutool.core.map.MapUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

import java.util.HashMap;


/**
 * spring-jms异步消息配置类
 *
  * @date 2021-11-25
 */
@Configuration
public class JmsConfig {

    /**
     * 消息转换成json格式
     */
    @Bean
    public MappingJackson2MessageConverter messageConverter() {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        //设置是必须的，否则接收时会抛空指针，疑似类型不匹配。这里只有String，收发其他类型也需要在这里设置进去
        messageConverter.setTypeIdPropertyName("_typeId");
        messageConverter.setTypeIdMappings(MapUtil.builder(new HashMap<String, Class<?>>())
                .put("jsonStr", String.class)
                .build()
        );
        return messageConverter;
    }

    /*
     * 自定义监听器容器工厂，开启消息客户端调用acknowledge手动确认，须同时关闭事务。防止日志一直打印 Transaction Commit :null
     * JmsListener需要指定此工厂bean名称
     * 失败
     */
    /*
    @Bean(value = "myJmsListenerContainerFactory")
    public SimpleJmsListenerContainerFactory simpleJmsListenerContainerFactory(@Qualifier("jmsConnectionFactory") ConnectionFactory jmsConnectionFactory) {
        SimpleJmsListenerContainerFactory jmsTemplate = new SimpleJmsListenerContainerFactory();
        jmsTemplate.setSessionTransacted(false);
        jmsTemplate.setSessionAcknowledgeMode(JmsProperties.AcknowledgeMode.CLIENT.getMode());
        jmsTemplate.setConnectionFactory(jmsConnectionFactory);
        return jmsTemplate;
    }
    */

}
