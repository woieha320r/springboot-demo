package pri.demo.springboot.core.log;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import pri.demo.springboot.entity.SysLogEntity;
import pri.demo.springboot.core.config.Constants;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * controller包日志切面
 *
 * @author woieha320r6
 */
@Aspect
@Component
@Slf4j
public class ControllerLogAop {

    private SysLogEntity logEntity;
    private LocalDateTime beforeTime;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public ControllerLogAop(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Pointcut(value = "execution(* pri.demo.springboot.controller.*.*(..))")
    public void pointCut() {
    }

    @Before(value = "pointCut()")
    public void before(JoinPoint joinPoint) {
        log.debug("切面-开始:controller日志");
        beforeTime = LocalDateTime.now();
        logEntity = new SysLogEntity().setMethodSignature(
                joinPoint.getTarget().getClass().getSimpleName() + "#" + joinPoint.getSignature().getName() + "("
                        + Arrays.stream(joinPoint.getArgs())
                        .map(obj -> obj.getClass().getSimpleName())
                        .collect(Collectors.joining(",")) + ")"
        );
    }

    @AfterReturning(value = "pointCut()")
    public void success() {
        toRedis(logEntity.setSuccess(true));
    }

    @AfterThrowing(value = "pointCut()")
    public void exception() {
        toRedis(logEntity.setSuccess(false));
    }

    private void toRedis(SysLogEntity logEntity) {
        log.debug("切面-结束:controller日志");
        LocalDateTime now = LocalDateTime.now();
        logEntity.setCreateTime(now)
                .setUpdateTime(now)
                .setElapsedTime((short) LocalDateTimeUtil.between(beforeTime, now).getSeconds());
        redisTemplate.opsForSet().add(Constants.REDIS_LOGS, logEntity);
    }

}
