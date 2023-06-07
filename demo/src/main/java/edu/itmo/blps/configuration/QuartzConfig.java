package edu.itmo.blps.configuration;

import edu.itmo.blps.service.MyScheduledJob;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail jobDetail() {
        JobDetail jobDetail = JobBuilder.newJob(MyScheduledJob.class)
                .withIdentity("start_of_day", "start_of_day")
                .storeDurably()
                .build();
        return jobDetail;
    }

    @Bean
    public Trigger trigger() {
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withIdentity("start_of_day", "start_of_day")
                .startNow()
                // 每天0点执行
                // factory.setCronExpression("0 0 0 * * ?");
                .withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?"))
                .build();
        return trigger;
    }

}
