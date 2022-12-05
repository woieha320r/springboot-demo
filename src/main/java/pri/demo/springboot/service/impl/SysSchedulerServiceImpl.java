package pri.demo.springboot.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pri.demo.springboot.entity.SysSchedulerEntity;
import pri.demo.springboot.mapper.SysSchedulerMapper;
import pri.demo.springboot.service.SysSchedulerService;

import java.util.List;

/**
 * <p>
 * 定时任务 服务实现类
 * </p>
 *
 * @author woieha320r
 */
@Service
@Slf4j
public class SysSchedulerServiceImpl extends ServiceImpl<SysSchedulerMapper, SysSchedulerEntity> implements SysSchedulerService {

    private final Scheduler scheduler;

    @Autowired
    public SysSchedulerServiceImpl(Scheduler scheduler, SysSchedulerMapper schedulerMapper) throws ClassNotFoundException, SchedulerException {
        this.scheduler = scheduler;
        List<SysSchedulerEntity> schedulers = schedulerMapper.selectList(
                Wrappers.lambdaQuery(SysSchedulerEntity.class)
                        .select(SysSchedulerEntity::getTaskKey, SysSchedulerEntity::getClassName, SysSchedulerEntity::getCron)
                        .eq(SysSchedulerEntity::getInitStart, true)
        );
        for (SysSchedulerEntity schedulerEntity : schedulers) {
            addScheduler(scheduler, schedulerEntity);
        }
    }

    /**
     * 暂停定时任务
     */
    public void pause(String taskKey) throws SchedulerException {
        log.debug("定时任务-暂停:{}", taskKey);
        scheduler.pauseJob(JobKey.jobKey(taskKey));
    }

    /**
     * 启动定时任务
     */
    public void resume(String taskKey) throws SchedulerException, ClassNotFoundException {
        if (scheduler.checkExists(JobKey.jobKey(taskKey))) {
            log.debug("定时任务-恢复:{}", taskKey);
            scheduler.resumeJob(JobKey.jobKey(taskKey));
            return;
        }
        addScheduler(scheduler, getOne(
                Wrappers.lambdaQuery(SysSchedulerEntity.class)
                        .eq(SysSchedulerEntity::getTaskKey, taskKey)
        ));
    }

    /**
     * 更新定时任务
     */
    public void update(String taskKey, String cron) throws SchedulerException {
        log.debug("定时任务-更新:{}", taskKey);
        scheduler.rescheduleJob(
                scheduler.getTriggersOfJob(JobKey.jobKey(taskKey)).get(0).getKey()
                , startNowTrigger(taskKey, cron)
        );
    }

    /**
     * 调用定时任务执行一次
     */
    public void execute(String taskKey) throws SchedulerException {
        log.debug("定时任务-调用:{}", taskKey);
        scheduler.triggerJob(JobKey.jobKey(taskKey));
    }

    private void addScheduler(Scheduler scheduler, SysSchedulerEntity schedulerEntity) throws ClassNotFoundException, SchedulerException {
        String taskKey = schedulerEntity.getTaskKey();
        log.debug("定时任务-启动:{}", taskKey);
        scheduler.scheduleJob(
                JobBuilder.newJob((Class<? extends Job>) Class.forName(schedulerEntity.getClassName()))
                        .withIdentity(taskKey)
                        .build(),
                startNowTrigger(taskKey, schedulerEntity.getCron())
        );
    }

    private Trigger startNowTrigger(String taskKey, String cron) {
        return TriggerBuilder.newTrigger()
                .forJob(taskKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .startNow()
                .build();
    }

}
