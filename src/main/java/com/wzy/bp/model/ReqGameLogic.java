package com.wzy.bp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReqGameLogic {
    private String gameId;
    private long userId;
    private int seat;
    private int level;
    private List<List<String>> history = new ArrayList<>();
    private List<String> allHands = new ArrayList<>();
    private Map<String,String> rule = new HashMap<>();

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<List<String>> getHistory() {
        return history;
    }

    public void setHistory(List<List<String>> history) {
        this.history = history;
    }

    public List<String> getAllHands() {
        return allHands;
    }

    public void setAllHands(List<String> allHands) {
        this.allHands = allHands;
    }

    public Map<String, String> getRule() {
        return rule;
    }

    public void setRule(Map<String, String> rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ReqGameLogic{");
        sb.append("gameId='").append(gameId).append('\'');
        sb.append(", userId=").append(userId);
        sb.append(", seat=").append(seat);
        sb.append(", level=").append(level);
        sb.append(", history=").append(history);
        sb.append(", allHands=").append(allHands);
        sb.append(", rule=").append(rule);
        sb.append('}');
        return sb.toString();
    }
}
