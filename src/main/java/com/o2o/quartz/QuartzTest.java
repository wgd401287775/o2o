package com.o2o.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzTest {

    public static void main(String[] args) {
        try {
            //创建一个jobDetail实例。将该实例与JobTest绑定 JobTest为具体处理逻辑
            JobDetail jobDetail = JobBuilder.newJob(JobTest.class)
                    .withIdentity("job","jobGroup").build();

            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("*/5 * * * * ?");
            //创建一个Trigger实例。定义该job立即执行。并且每隔两秒钟执行一次
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger", "triggerGroup")
                    .withSchedule(cronScheduleBuilder).build();

            //创建scheduler实例
            SchedulerFactory sfact = new StdSchedulerFactory();
            Scheduler scheduler = sfact.getScheduler();
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
