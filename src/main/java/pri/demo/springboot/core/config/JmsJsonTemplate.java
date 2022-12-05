package pri.demo.springboot.core.config;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * jms发送json类型消息
 *
 * @author woieha320r6
 */
@Component
public class JmsJsonTemplate {

    private final JmsTemplate jmsTemplate;

    @Autowired
    public JmsJsonTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(String destination, Object obj) {
        jmsTemplate.send(destination, session -> session.createTextMessage(
                JSONUtil.toJsonStr(obj, new JSONConfig().setIgnoreNullValue(true))
        ));
    }

}
