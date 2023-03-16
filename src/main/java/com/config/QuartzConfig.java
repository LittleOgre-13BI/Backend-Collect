package com.config;

import com.job.UpdateMissionStateJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    private static String JOB_GROUP_NAME = "COLLECT_JOBGROUP_NAME";
    private static String TRIGGER_GROUP_NAME = "COLLECT_TRIGGERGROUP_NAME";

    @Bean
    public JobDetail UpdateMissionStateJobDetail(){
        return JobBuilder.newJob(UpdateMissionStateJob.class)
                .withIdentity("UpdateMissionStateJobDetail", JOB_GROUP_NAME)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger UpdateMissionStateJobTrigger(){
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/30 * * * * ?");

        return TriggerBuilder.newTrigger()
                .forJob(UpdateMissionStateJobDetail())
                .withIdentity("UpdateMissionStateJobTrigger", TRIGGER_GROUP_NAME)
                .withSchedule(cronScheduleBuilder)
                .build();

    }
}
