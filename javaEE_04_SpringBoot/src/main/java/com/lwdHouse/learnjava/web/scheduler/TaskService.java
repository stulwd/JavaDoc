package com.lwdHouse.learnjava.web.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 */
@Component
public class TaskService {
    final Logger logger = LoggerFactory.getLogger(getClass());

    // 启动延迟60秒，并以60秒的间隔执行任务
    // 可以指定 fixedRate 和 fixedDelay
    // @Scheduled(initialDelay = 60_000, fixedRate = 60_000 /*, fixedDelay = 60_000*/ )
    // 使用占位符
    @Scheduled(initialDelay = 60_000, fixedDelayString = "${task.checkDiskSpace:PT2M30S}")
    // 使用Duration 表示2分30秒
//     @Scheduled(initialDelay = 60_000, fixedDelayString = "PT2M30S")
    public void checkSystemStatusEveryMinute() {
        logger.info("Start check system status");
    }

    // 使用cron
    // 0 0 12 * * MON-FRI表示工作日12点
    // 0 0 12 1-3,10 * * 每个月1，2，3，10号12点
    // 0 */10 * * * * 每隔10分钟执行 可以取代fixedRate
    @Scheduled(cron = "${task.report:*/5 * * * * *}")
    public void cronDailyReport() {
        logger.info("Start daily report task...");
    }
}
