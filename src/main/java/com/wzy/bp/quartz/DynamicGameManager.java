package com.wzy.bp.quartz;


import com.wzy.bp.context.BeanProvider;
import com.wzy.bp.job.HttpMonitoringJob;
import com.wzy.bp.model.HttpSequence;
import com.wzy.bp.model.VsGame;
import com.wzy.bp.service.HttpGameService;
import com.wzy.bp.service.HttpRequestService;
import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shengzhao Li
 */
public class DynamicGameManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicGameManager.class);

    private static final String MONITORING_INSTANCE_JOB_NAME_PREFIX = "gameJunit-instance-";


    public static String generateMonitoringInstanceJobName(String key) {
        return MONITORING_INSTANCE_JOB_NAME_PREFIX + key;
    }



    private transient HttpGameService httpGameService = BeanProvider.getBean(HttpGameService.class);


    private VsGame instance;

    public DynamicGameManager(VsGame instance) {
        this.instance = instance;
    }

    public boolean enable() {
        //final ApplicationInstance instance = instanceRepository.findByGuid(guid, ApplicationInstance.class);
        if (instance.isEnable()) {
            LOGGER.debug("<{}> Instance[guid={}] already enabled, ignore it", username(), instance.getGuid());
            return false;
        }

        final boolean addSuccessful = startupMonitoringJob(instance);
        if (!addSuccessful) {
            LOGGER.debug("<{}> NOTE: Add MonitoringJob[jobName={}] failed", username(), instance.getJobName());
            return false;
        }

        //update
        instance.setEnabled(true);
        httpGameService.updateEnabled(instance.getEnable(),instance.getGuid());
        LOGGER.debug("<{}> Update ApplicationInstance[guid={}] enabled=true,jobName={}", username(), instance.getGuid(), instance.getJobName());

        return true;
    }

    private boolean startupMonitoringJob(VsGame instance) {
        final String jobName = getAndSetJobName(instance);

        DynamicJob job = new DynamicJob(jobName)
                .cronExpression(instance.getFrequency().getCronExpression())
                .target(HttpMonitoringJob.class)
                .addJobData(HttpMonitoringJob.APPLICATION_INSTANCE_GUID, instance.getGuid());

        return executeStartup(instance, job);
    }

    private boolean executeStartup(VsGame instance, DynamicJob job) {
        boolean result = false;
        try {
            if (DynamicSchedulerFactory.existJob(job)) {
                result = DynamicSchedulerFactory.resumeJob(job);
                LOGGER.debug("<{}> Resume  [{}] by ApplicationInstance[guid={},instanceName={}] result: {}", username(), job, instance.getGuid(), instance.getName(), result);
            } else {
                result = DynamicSchedulerFactory.registerJob(job);
                LOGGER.debug("<{}> Register  [{}] by ApplicationInstance[guid={},instanceName={}] result: {}", username(), job, instance.getGuid(), instance.getName(), result);
            }
        } catch (SchedulerException e) {
            LOGGER.error("<{}> Register [" + job + "] failed", username(), e);
        }
        return result;
    }

    private String getAndSetJobName(VsGame instance) {
        String jobName = instance.getJobName();
        if (StringUtils.isEmpty(jobName)) {
            jobName = generateMonitoringInstanceJobName(instance.getGuid());
            instance.setJobName(jobName);
        }
        return jobName;
    }

    private String username() {
    	return null;
        //return SecurityUtils.currentUsername();
    }
    

    public boolean delete() {
        if (instance.isEnable()) {
            LOGGER.debug("<{}> Forbid delete enabled ApplicationInstance[guid={}]", username(), instance.getGuid());
            return false;
        }

        httpGameService.deleteHttpLog(instance.getGuid());

        checkAndRemoveJob(instance);

        //logic delete
        instance.setArchived(true);
        httpGameService.archivedHttpData(instance.getGuid());
        LOGGER.debug("<{}> Archive ApplicationInstance[guid={}] and FrequencyMonitorLogs,MonitoringReminderLogs", username(), instance.getGuid());
        return true;
    }

    private void checkAndRemoveJob(VsGame instance) {
        DynamicJob job = new DynamicJob(getAndSetJobName(instance));
        try {
            if (DynamicSchedulerFactory.existJob(job)) {
                final boolean result = DynamicSchedulerFactory.removeJob(job);
                LOGGER.debug("<{}> Remove DynamicJob[{}] result [{}]", username(), job, result);
            }
        } catch (SchedulerException e) {
            LOGGER.error("<{}> Remove [" + job + "] failed", username(), e);
        }
    }

    
    /* * 1. Remove the job
     * 2. update instance to enabled=false
     **/
     
    public boolean kill() {
        if (!instance.isEnable()) {
            LOGGER.debug("<{}> Expect ApplicationInstance[guid={}] enabled=true,but it is false, illegal status",
            		username(), instance.getGuid());
            return false;
        }

        if (!pauseJob(instance)) {
            LOGGER.debug("<{}> Pause Job[name={}] failed", username(), instance.getJobName());
            return false;
        }

        //update
        instance.setEnabled(false);
        httpGameService.updateEnabled(instance.getEnable(),instance.getGuid());
        LOGGER.debug("<{}> Update ApplicationInstance[guid={}] enabled=false", username(), instance.getGuid());
        return true;
    }

    private boolean pauseJob(VsGame instance) {
        DynamicJob job = new DynamicJob(getAndSetJobName(instance));
        try {
            return DynamicSchedulerFactory.pauseJob(job);
        } catch (SchedulerException e) {
            LOGGER.error("<{}> Pause [" + job + "] failed", username(), e);
            return false;
        }
    }
}