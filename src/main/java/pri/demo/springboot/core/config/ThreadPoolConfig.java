package pri.demo.springboot.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 线程池
 *
 * @author woieha320r6
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * 线程池任务调度类
     */
    @Bean
    public ThreadPoolTaskScheduler globalThreadPool() {
        ThreadPoolTaskScheduler schedulerPool = new ThreadPoolTaskScheduler();
        schedulerPool.setPoolSize(3);
        schedulerPool.setWaitForTasksToCompleteOnShutdown(true);
        schedulerPool.setAwaitTerminationSeconds(60);
        schedulerPool.setThreadNamePrefix("thread-pool-task-scheduler-");
        return schedulerPool;
    }

}
