package com.wzy.bp.config;

import javax.sql.DataSource;

import com.wzy.bp.quartz.DynamicSchedulerFactory;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class ScheduleFactoryConfig {

    @Value("${scheduler.auto.startup}")
    private boolean schedulerAutoStartup;


    @Bean
//    @Lazy
    public SchedulerFactoryBean scheduler(DataSource dataSource) throws Exception {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        factoryBean.setDataSource(dataSource);
        factoryBean.setAutoStartup(schedulerAutoStartup);

        return factoryBean;
    }


    /**
     * dynamic scheduler factory
     * 动态 定时任务 配置
     */
    @Bean
    public DynamicSchedulerFactory dynamicSchedulerFactory(Scheduler scheduler) {
        DynamicSchedulerFactory schedulerFactory = new DynamicSchedulerFactory();
        schedulerFactory.setScheduler(scheduler);
        return schedulerFactory;
    }


}
