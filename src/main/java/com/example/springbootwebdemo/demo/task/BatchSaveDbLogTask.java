package com.example.springbootwebdemo.demo.task;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.example.springbootwebdemo.core.log.GlobalLogService;
import com.example.springbootwebdemo.demo.entity.GlobalLogEntity;
import com.example.springbootwebdemo.demo.property.BatchSaveCacheLogProperty;
import io.lettuce.core.RedisCommandExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 定时从缓存中取出对象批量保存入库
 * 缓存设计：一边只管放，一边只管取，不要依赖是否已取出的状态或是否成功，这样要加锁
 *
  * @date 2022-02-23
 */
@Component
@Slf4j
@Configuration
public class BatchSaveDbLogTask {

    @Autowired
    private RedisTemplate<String, Object> strObjRedisTemplate;

    @Autowired
    private BatchSaveCacheLogProperty batchSaveCacheLogProperty;

    @Autowired
    private GlobalLogService globalLogService;

    private boolean redisVersionLow = false;

    private final long handleSizeOnce = 50L;

    /**
     * 低版本redis不支持spop key number命令，只能一个个取。
     */
    private List<Object> getObjFromRedisOneByOne(long redisCacheObjSize, String key) {
        List<Object> objs = new ArrayList<>();
        for (int i = 0; i < redisCacheObjSize; i++) {
            objs.add(strObjRedisTemplate.opsForSet().pop(key));
        }
        return objs;
    }

    /**
     * 日志
     */
    //initialDelay：启动后多久执行一次，fixedRate：第一次执行后每隔多久执行一次。单位为毫秒
    //也可用cron表达式@Scheduled(cron = "0 0 0/2 * * ?")
    @Scheduled(initialDelay = 3000, fixedRate = 60000)
    //异步执行，新启线程，下次任务不会等待上次任务结束
    //@Async
    public void log() throws InterruptedException {
        log.debug("缓存中转批量存库 >>> 保存日志任务 >>> 开始执行");
        if (!batchSaveCacheLogProperty.getOpen()) {
            log.debug("缓存中转批量存库 >>> 保存日志任务 >>> 已在配置文件中配置为停止执行");
            return;
        }
        String key = batchSaveCacheLogProperty.getKey();
        long redisCacheObjSize = Optional.ofNullable(strObjRedisTemplate.opsForSet().size(key)).orElse(0L);
        if (redisCacheObjSize <= 0) {
            log.debug("缓存中转批量存库 >>> 保存日志任务 >>> 无数据");
            return;
        }
        log.debug("缓存中转批量存库 >>> 保存日志任务 >>> {}条数据", redisCacheObjSize);

        // 共循环 (定时任务执行时查询到的数量/50) 次
        List<Object> redisCaches;
        do {
            // 每次循环内，处理50个日志对象，redis弹出50条
            if (redisVersionLow) {
                redisCaches = getObjFromRedisOneByOne(redisCacheObjSize, key);
            } else {
                try {
                    redisCaches = Optional.ofNullable(strObjRedisTemplate.opsForSet().pop(key, handleSizeOnce)).orElse(new ArrayList<>());
                } catch (RedisSystemException | RedisCommandExecutionException e) {
                    // 因redis版本不同，可能不支持spop key number命令，只能一个个取。
                    redisCaches = getObjFromRedisOneByOne(redisCacheObjSize, key);
                    redisVersionLow = true;
                }
            }

            if (redisCaches.size() > 0) {
                try {
                    globalLogService.batchSaveLog(redisCaches.parallelStream().map(obj -> {
                        //避免null值批量增库导致数据库默认值失效
                    /*
                    if (Objects.isNull(log.getIsSuccess())) {
                        log.setIsSuccess(true);
                    }
                    */
                        return (GlobalLogEntity) obj;
                    }).collect(Collectors.toList()));
                } catch (Exception e) {
                    log.error("缓存中转批量存库 >>> 保存日志任务 >>> 异常 >>> {}", ExceptionUtil.stacktraceToString(e));
                    strObjRedisTemplate.opsForSet().add(key, redisCaches.toArray());
                    log.debug("缓存中转批量存库 >>> 保存日志任务 >>> 异常数据回写至缓存完成");
                }
            }

            redisCacheObjSize -= redisCaches.size();
            Thread.sleep(1000L);
        } while (redisCacheObjSize > 0);
        log.debug("缓存中转批量存库 >>> 保存日志任务 >>> 执行结束");
    }

}
