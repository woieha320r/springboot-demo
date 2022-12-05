package pri.demo.springboot.core.businessnode;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pri.demo.springboot.entity.SysBusinessTrackEntity;
import pri.demo.springboot.core.config.Constants;

/**
 * 接收业务节点并记录到业务轨迹表
 *
 * @author woieha320r6
 */
@Component
@Slf4j
public class BusinessNodeRecord {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public BusinessNodeRecord(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 可通过concurrency指定并发，注意线程安全
     */
    @JmsListener(destination = Constants.JMS_BUSINESS_NODE)
    public void businessNodeRecord(String msg) {
        log.debug("消息队列-接收:业务节点");
        redisTemplate.opsForSet().add(Constants.REDIS_BUSINESS_NODES, JSONUtil.toBean(msg, SysBusinessTrackEntity.class));
    }

}
