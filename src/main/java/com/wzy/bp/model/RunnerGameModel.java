package com.wzy.bp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunnerGameModel {
    private int                 code;
    private String              gameId;
    private List<String>        cards    = new ArrayList<>();
    private List<RobotInfo>     seatInfo = new ArrayList<>();
    private List<List<String>>  history  = new ArrayList<>();
    private List<String>        allHands = new ArrayList<>();
    private Map<String, String> rule     = new HashMap<>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public List<String> getCards() {
        return cards;
    }

    public void setCards(List<String> cards) {
        this.cards = cards;
    }

    public List<RobotInfo> getSeatInfo() {
        return seatInfo;
    }

    public void setSeatInfo(List<RobotInfo> seatInfo) {
        this.seatInfo = seatInfo;
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

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public int getInTurn() {
        return inTurn;
    }

    public void setInTurn(int inTurn) {
        this.inTurn = inTurn;
    }

    private String gameStatus;
    private int    inTurn;
}
