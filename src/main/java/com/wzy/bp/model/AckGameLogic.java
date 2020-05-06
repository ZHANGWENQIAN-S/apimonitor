package com.wzy.bp.model;

public class AckGameLogic {
    private int code;
    private String gameId;
    private long userId;
    private int seat;
    private String cards;
    private int delay;

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

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AckGameLogic{");
        sb.append("code=").append(code);
        sb.append(", gameId='").append(gameId).append('\'');
        sb.append(", userId=").append(userId);
        sb.append(", seat=").append(seat);
        sb.append(", cards=").append(cards);
        sb.append('}');
        return sb.toString();
    }
}
