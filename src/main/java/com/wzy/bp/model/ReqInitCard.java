package com.wzy.bp.model;

public class ReqInitCard {
    private String gameId;  //当前游戏唯一局号
    private int bombNumber; //炸弹个数,
    private int betterSeat;  //优势座位号：0,1,2 随机：3
    private int tidiness;   //手牌的整齐度 0:不调整 1:调整
    private int firstSeat;  //当局先出牌的座位号：0,1,2，随机：3

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getBombNumber() {
        return bombNumber;
    }

    public void setBombNumber(int bombNumber) {
        this.bombNumber = bombNumber;
    }

    public int getBetterSeat() {
        return betterSeat;
    }

    public void setBetterSeat(int betterSeat) {
        this.betterSeat = betterSeat;
    }

    public int getTidiness() {
        return tidiness;
    }

    public void setTidiness(int tidiness) {
        this.tidiness = tidiness;
    }

    public int getFirstSeat() {
        return firstSeat;
    }

    public void setFirstSeat(int firstSeat) {
        this.firstSeat = firstSeat;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ReqInitCard{");
        sb.append("gameId='").append(gameId).append('\'');
        sb.append(", bombNumber=").append(bombNumber);
        sb.append(", betterSeat=").append(betterSeat);
        sb.append(", tidiness=").append(tidiness);
        sb.append(", firstSeat=").append(firstSeat);
        sb.append('}');
        return sb.toString();
    }
}
