package com.wzy.bp.model;

import java.util.ArrayList;
import java.util.List;

public class AckInitCard {
    private int code;
    private String gameId;
    private List<String> cards = new ArrayList<>();


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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AckInitCard{");
        sb.append("code=").append(code);
        sb.append(", gameId='").append(gameId).append('\'');
        sb.append(", cards=").append(cards);
        sb.append('}');
        return sb.toString();
    }
}
