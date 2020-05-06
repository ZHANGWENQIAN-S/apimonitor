package com.wzy.bp.notice;


import com.wzy.bp.config.DruidDBConfig;
import com.wzy.bp.dao.HttpSequenceLogMapper;
import com.wzy.bp.dao.HttpSequenceMapper;
import com.wzy.bp.model.AlertModel;
import com.wzy.bp.model.HttpSequence;
import com.wzy.bp.model.HttpSequenceLog;
import com.wzy.bp.util.CommonAttribute;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 日志邮件提醒任务
 *
 * @author 01383518
 * @date: 2019年4月3日 下午7:39:22
 */
@Component
public class FailReminderTask {

    @Autowired
    private HttpSequenceMapper    httpSequenceMapper;
    @Autowired
    private HttpSequenceLogMapper httpSequenceLogMapper;
    @Autowired
    private MailSendService       mailSendService;

    public  Map<String, Integer> failId = Maps.newConcurrentMap();
    private Logger              logger = LoggerFactory.getLogger(DruidDBConfig.class);


    /**
     * 异常日志邮件提醒，每30秒一次
     */
    @Scheduled(cron = "3/30 * * * * ?")
    public void mailReminder() {
        List<HttpSequence> list = httpSequenceMapper.selectEnabledMonitorList();
        list.forEach(m -> {
            /**
             * 根据每个任务的时间，计算定时区间内任务次数
             * 如何避免报警风暴
             * 报警级别:严重 4、警告 3、一般 2、提示告警 1
             */


            Integer num = 30 / m.getFrequency().getSeconds();
            if(m.getFrequency().getSeconds()>30){
                num =1;
            }
            List<HttpSequenceLog> logsList = httpSequenceLogMapper.selectLastFewLogByPguid(m.getGuid(), num);
            if (logsList == null || logsList.size() == 0) {
                return;
            }
            String AlertLevel = null;
            List<HttpSequenceLog> delayList = new ArrayList<>();
            List<HttpSequenceLog> errList = new ArrayList<>();
            logsList.forEach(l -> {
                if (l.isStatus()) {
                    if (l.getCostTime() > m.getResponseTime()) {
                        delayList.add(l);
                    }
                } else {
                    errList.add(l);
                }
            });
            if (errList.size() == 0 && delayList.size() == 0) {
                if (failId.get(m.getGuid()) != null) {
                    //logsList首个
                    sendMessage(m, logsList.get(0), true);
                    failId.remove(m.getGuid());
                }
                return;
            } else {
                //区分同级别告警不覆盖，不同级别告警覆盖且发送邮件
                if (errList.size() == num ) {
                    if(failId.get(m.getGuid()) != null && failId.get(m.getGuid()) == CommonAttribute.ALERT_CRITICAL){
                        return;
                    }
                    failId.put(m.getGuid(), 4);
                    sendMessage(m, errList, 4);
                } else if (errList.size() < num && errList.size() > 0 ) {
                    if(failId.get(m.getGuid()) != null && failId.get(m.getGuid()) ==3){
                        return;
                    }
                    failId.put(m.getGuid(), 3);
                    sendMessage(m, errList, 3);
                } else {
                    if (delayList.size() > 1 && num != 1 ) {
                        if(failId.get(m.getGuid())!=null && failId.get(m.getGuid()) ==2){
                            return;
                        }
                        failId.put(m.getGuid(), 2);
                        sendMessage(m, delayList, 2);
                    } else if (failId.get(m.getGuid()) != null && failId.get(m.getGuid()) != 1) {
                        if(failId.get(m.getGuid())!=null && failId.get(m.getGuid()) ==1){
                            return;
                        }
                        failId.put(m.getGuid(), 1);
                        sendMessage(m, delayList, 1);
                    }
                }
            }
//			HttpSequenceLog logs = httpSequenceLogMapper.selectLastLogByPguid(m.getGuid());
//			if(logs==null){
//				return ;
//			}
//			//成功
//			if(logs.isStatus()){
//				if(failId.get(m.getGuid())!=null){
//				    sendMessage(m,logs,true);
//				}
//				failId.remove(m.getGuid());
//				return;
//			}
//			//失败
//			if(failId.get(m.getGuid())==null){
//				failId.put(m.getGuid(), logs.getLog());
//				sendMessage(m,logs,false);
//			}


        });
    }


    private void sendMessage(HttpSequence m, HttpSequenceLog logs, boolean success) {
        String msg = builderMsg(m, logs, success);
        String remark = m.getRemark();
        if (StringUtils.isNotEmpty(remark)) {
            String[] mails = remark.split(",");
            logger.debug(msg);
            mailSendService.sendMail(mails, "服务可用性监控", msg,"");
        }
    }

    /**
     * 查询到错误发送消息
     *
     * @param m
     * @param logs
     * @param level
     */

    private void sendMessage(HttpSequence m, List<HttpSequenceLog> logs, Integer level) {
        String msg = builderMsg(m, logs, level);
        String remark = m.getRemark();
        if (StringUtils.isNotEmpty(remark)) {
            String[] mails = remark.split(",");
            mailSendService.sendMail(mails, "服务可用性监控", msg, (String) CommonAttribute.alertLevel.get(level));
        }
    }


    private String builderMsg(HttpSequence m, HttpSequenceLog logs, boolean success) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(logs.getCreateTime());
        if (success) {
            return date + "  服务【" + m.getName() + "】已恢复正常";
        }
        return date + "  服务【" + m.getName() + "】异常,详细信息：" + logs.getLog();
    }

    /**
     * 错误告警渲染
     * @param m
     * @param logs
     * @param level
     * @return
     */
    private String builderMsg(HttpSequence m, List<HttpSequenceLog> logs, Integer level) {
        AlertModel alertMsg = new AlertModel();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String date = simpleDateFormat.format(logs.get(0).getCreateTime());
        int num = 30/m.getFrequency().getSeconds();
        if (m.getFrequency().getSeconds()>30){
            num = 1;
        }
        String percentage = numberFormat.format((float) logs.size() / (float) (num) * 100) + "%";
        alertMsg.setHappenTime(date);
        alertMsg.setPercentage(percentage);
        alertMsg.setServiceName(m.getName());
        alertMsg.setLogContent(logs.get(0).getLog());
//        alertMsg.setUrl(m.);
        switch (level){
            case 4:
                alertMsg.setLevel("严重");
                break;
            case 3:
                alertMsg.setLevel("警告");
                break;
            case 2:
                alertMsg.setLevel("一般");
                break;
            case 1:
                alertMsg.setLevel("提醒");
                break;
            default: break;
        }
        String msg = "[报警级别]:" +alertMsg.getLevel()+";";
         msg += "[报警内容]:" +alertMsg.getLogContent()+";";
         msg += "[异常占比]:" +alertMsg.getPercentage()+";";
         msg += "[监控内容]:" +alertMsg.getServiceName()+";";
         msg += "[报警时间]:" +alertMsg.getHappenTime()+";";
        return msg;
    }


}
