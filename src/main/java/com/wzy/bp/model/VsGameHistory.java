package com.wzy.bp.model;

import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.util.Date;

public class VsGameHistory {

    private int id;

    private String guid;

    private int status;
    private long costTime;

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    private String log;
    private Date   createTime;
    private int    winner;

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

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }
}
