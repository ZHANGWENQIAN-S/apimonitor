package com.wzy.bp.model;

import java.util.Date;

public class VsGameRequestHistory {

    private int id;

    private String URI;

    private int status;
    private long costTime;

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public long getCostTime() {
        return costTime;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    private String log;
    private Date   createTime;
    private String gameId;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
