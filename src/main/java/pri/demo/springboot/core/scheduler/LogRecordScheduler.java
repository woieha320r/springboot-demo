package pri.demo.springboot.core.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 日志记录定时任务
 *
 * @author woieha320r
 */
@Component
@Slf4j
public class LogRecordScheduler implements Job {

    private final RedisBatchToDb redisBatchToDb;

    @Autowired
    public LogRecordScheduler(RedisBatchToDb redisBatchToDb) {
        this.redisBatchToDb = redisBatchToDb;
    }

    @Override
    public void execute(JobExecutionContext context) {
        log.debug("定时任务-日志记录");
        try {
            redisBatchToDb.execute(RedisBatchToDb.Type.LOG);
        } catch (Exception exception) {
            //防止quartz失败重试
            log.error("", exception);
        }
    }

}
