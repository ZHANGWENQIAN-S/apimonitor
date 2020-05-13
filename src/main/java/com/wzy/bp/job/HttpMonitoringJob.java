package com.wzy.bp.job;

import com.wzy.bp.context.BeanProvider;
import com.wzy.bp.service.HttpGameService;
import com.wzy.bp.service.HttpRequestService;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@DisallowConcurrentExecution
public class HttpMonitoringJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpMonitoringJob.class);
    public static final String APPLICATION_INSTANCE_GUID = "instanceGuid";

    private transient HttpRequestService instanceService = BeanProvider.getBean(HttpRequestService.class);
    private transient HttpGameService    gameService = BeanProvider.getBean(HttpGameService.class);

    public HttpMonitoringJob() {
    	
    }

    /*
    * 每次的监控会将 以下代码执行一次
    * */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        final JobKey key = context.getJobDetail().getKey();
        LOGGER.debug("*****  Start execute Job [{}]", key);


        final String guid = context.getMergedJobDataMap().getString(APPLICATION_INSTANCE_GUID);
        if(StringUtils.startsWith(key.getName(),"gameJunit")){
            gameService.excuteVs(guid);
        }
        instanceService.executeRequest(guid);

        LOGGER.debug("&&&&&  End execute Job [{}]", key);
    }
}