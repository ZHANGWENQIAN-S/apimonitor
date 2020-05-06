package com.wzy.bp.model;

import java.util.Date;

public class VsGame {

    private int id;

    private String guid;

    private String group;//所属组

    private String name;

    private MonitorFrequency frequency = MonitorFrequency.THIRTY;//监控频率

    private String AIInfo;//AI配置信心，JSON对象

    private boolean enable;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private String remark;

//    public static VsGame getVsGame(VsGameForm form) {
//        VsGame vsGame = new VsGame();
//        return vsGame;
//    }

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getAIInfo() {
        return AIInfo;
    }

    public void setAIInfo(String AIInfo) {
        this.AIInfo = AIInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MonitorFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(MonitorFrequency frequency) {
        this.frequency = frequency;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnabled(boolean enable) {
        this.enable = enable;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private int responseTime;


    private String jobName;//quartz调度的job名称

    private boolean enabled;//是否启动

    private boolean archived;//是否删除（0-有效，1-删除）

    private Date createTime;
}
