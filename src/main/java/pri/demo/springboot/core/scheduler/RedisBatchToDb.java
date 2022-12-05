package pri.demo.springboot.core.scheduler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import pri.demo.springboot.entity.SysBusinessTrackEntity;
import pri.demo.springboot.entity.SysLogEntity;
import pri.demo.springboot.core.config.Constants;
import pri.demo.springboot.mapper.SysBusinessTrackMapper;
import pri.demo.springboot.mapper.SysLogMapper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * redis批量存库模版
 *
 * @author woieha320r
 */
@Component
@Slf4j
public class RedisBatchToDb {

    private final RedisTemplate<String, Object> redisTemplate;
    private final SysLogMapper logMapper;
    private final SysBusinessTrackMapper businessTrackMapper;

    @Autowired
    public RedisBatchToDb(RedisTemplate<String, Object> redisTemplate, SysLogMapper logMapper, SysBusinessTrackMapper businessTrackMapper) {
        this.redisTemplate = redisTemplate;
        this.logMapper = logMapper;
        this.businessTrackMapper = businessTrackMapper;
    }

    @AllArgsConstructor
    @Getter
    public enum Type {
        /**
         * 日志
         */
        LOG(Constants.REDIS_LOGS),
        /**
         * 业务节点
         */
        BUSINESS_NODE(Constants.REDIS_BUSINESS_NODES);

        private final String redisKey;
    }

    public void execute(Type type) throws InterruptedException {
        long redisSize = Optional
                .ofNullable(redisTemplate.opsForSet().size(type.redisKey))
                .orElse(0L);
        if (redisSize <= 0) {
            return;
        }

        // 共循环 (定时任务执行时的数量/50) 次
        List<Object> poppedObjs;
        do {
            long popSize = redisSize > 50 ? 50 : redisSize;
            poppedObjs = Optional.ofNullable(redisTemplate.opsForSet().pop(type.redisKey, popSize))
                    .orElse(new ArrayList<>());
            if (poppedObjs.size() > 0) {
                try {
                    switch (type) {
                        case LOG:
                            logMapper.batchSave(poppedObjs);
                            break;
                        case BUSINESS_NODE:
                            businessTrackMapper.batchSave(poppedObjs);
                            break;
                        default:
                    }
                } catch (Exception exception) {
                    saveOneByOne(type, poppedObjs);
                }
            }
            redisSize -= popSize;
            Thread.sleep(1000L);
        } while (redisSize > 0);
    }

    private void saveOneByOne(Type type, List<Object> poppedObjs) {
        List<Object> errorObjs = new ArrayList<>();
        for (Object poppedObj : poppedObjs) {
            try {
                switch (type) {
                    case LOG:
                        logMapper.insert((SysLogEntity) poppedObj);
                        break;
                    case BUSINESS_NODE:
                        businessTrackMapper.insert((SysBusinessTrackEntity) poppedObj);
                        break;
                    default:
                }
            } catch (Exception exception) {
                errorObjs.add(poppedObj);
            }
        }
        String errorKey = Constants.REDIS_ERROR_PREFIX + type.redisKey;
        redisTemplate.opsForSet().add(errorKey, errorObjs);
        int errorCount = errorObjs.size();
        log.warn("Redis批量入库失败，回写{}条至{}，错误率{}", errorCount, errorKey
                , new DecimalFormat("##.00%").format(errorCount / poppedObjs.size()));
    }

}
