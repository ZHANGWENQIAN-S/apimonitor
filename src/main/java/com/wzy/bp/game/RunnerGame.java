package com.wzy.bp.game;

import com.alibaba.fastjson.JSONObject;
import com.wzy.bp.http.client.HttpClientHandlerForAI;
import com.wzy.bp.model.AckGameLogic;
import com.wzy.bp.model.AckInitCard;
import com.wzy.bp.model.ReqGameLogic;
import com.wzy.bp.model.ReqInitCard;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RunnerGame {
    /**
     * 跑得快游戏服务
     */
    public static RunnerGame instance = new RunnerGame();

    private RunnerGame() {
    }

    public static RunnerGame getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        JSONObject req = new JSONObject();
        String i = RunnerGame.getInstance().doAction(req);
        System.out.println(i);
    }

    public String doAction(JSONObject AIInfo) {
        JSONObject result = new JSONObject();
        String gameId = UUID.randomUUID().toString();
        int bombNumber = 0; //炸弹个数,
        int betterSeat = 3;  //优势座位号：0,1,2 随机：3
        int tidiness = 0;   //手牌的整齐度 0:不调整 1:调整
        int firstSeat = 0;  //当局先出牌的座位号：0,1,2，随机：3        List<RobotInfo> robotInfos = new ArrayList<>();
        String initCardUrl = "http://localhost:23234/pdk/initcard";//"http://154.223.71.76:29031" + "/pdkAI" + "/pdk" + "/initcard";//访问路径+服务名+AI版本+接口名
        String gamelogic = "http://localhost:23234/pdk/gamelogic";//"http://154.223.71.76:29031" + "/pdkAI" + "/pdk" + "/gamelogic";
        ReqInitCard reqInitCard = new ReqInitCard();
        if (AIInfo.containsKey("initCard")) {
            initCardUrl = AIInfo.getString("initCard");
        }
        if (AIInfo.containsKey("bombNumber")) {
            bombNumber = AIInfo.getInteger("bombNumber");
        }
        if (AIInfo.containsKey("betterSeat")) {
            betterSeat = AIInfo.getInteger("betterSeat");
        }
        if (AIInfo.containsKey("tidiness")) {
            tidiness = AIInfo.getInteger("tidiness");
        }
        if (AIInfo.containsKey("tidiness")) {
            firstSeat = AIInfo.getInteger("firstSeat");
        }
        reqInitCard.setBetterSeat(betterSeat);
        reqInitCard.setBombNumber(bombNumber);
        reqInitCard.setTidiness(tidiness);
        reqInitCard.setFirstSeat(firstSeat);
        reqInitCard.setGameId(gameId);
        String res = HttpClientHandlerForAI.getInstance().sendRequest(initCardUrl, JSONObject.toJSONString(reqInitCard));
        AckInitCard initCard = JSONObject.parseObject(res, AckInitCard.class);
        if (initCard.getCode() != 0) {
            result.put("code", 211);
            result.put("msg", "起手牌请求错误");
            return JSONObject.toJSONString(result);
        }
        List<String> cards = initCard.getCards();
        List<String> allhands = cards;
        result.put("start", cards.toString());
        List<List<String>> history = new ArrayList<>();
        int seatNow = firstSeat;
        int seatBig = firstSeat;
        while (StringUtils.isNotEmpty(allhands.get(0)) && StringUtils.isNotEmpty(allhands.get(1)) && StringUtils.isNotEmpty(allhands.get(2))) {
            ReqGameLogic gameLogic = new ReqGameLogic();
            String url = gamelogic;
            if (AIInfo.containsKey("aiList")) {
                JSONObject aiInfo = AIInfo.getJSONArray("aiList").getJSONObject(seatNow);
                url = aiInfo.getString("gameLogic");
            }
            gameLogic.setAllHands(allhands);
            gameLogic.setSeat(seatNow);
            gameLogic.setHistory(history);
            gameLogic.setGameId(gameId);
            AckGameLogic ackGameLogic = JSONObject.parseObject(HttpClientHandlerForAI.getInstance().
                    sendRequest(url, JSONObject.toJSONString(gameLogic)), AckGameLogic.class);
            if (ackGameLogic.getCode() != 0) {
                result.put("code", 212);
                result.put("msg", "请求打牌接口错误");
                return JSONObject.toJSONString(result);
            }
            System.out.println(ackGameLogic);
            String card = ackGameLogic.getCards();
            List<String> historyNow = new ArrayList<>();
            historyNow.add(0, seatNow + "");
            if (card.isEmpty()) {
                seatNow++;
                if (seatNow == 3) {
                    seatNow = 0;
                }
                continue;
            } else {
                seatBig = seatNow;
                historyNow.add(card);
                history.add(historyNow);
                String handNow = shortTheHands(allhands.get(seatNow), card);
                allhands.set(seatNow, handNow);
                seatNow++;
                if (seatNow == 3) {
                    seatNow = 0;
                }
                continue;
            }

        }
        result.put("history", history);
        result.put("env", reqInitCard);
        result.put("winner", seatBig);
        result.put("point", point(allhands));
        result.put("code",200);
        return JSONObject.toJSONString(result);

    }

    public static String shortTheHands(String hand, String card) {
        int len = card.length();
        int i = 0;
        while ((i + 2) <= len) {
            String cardI = card.substring(i, i + 2);
            hand = hand.replace(cardI, "");
            i++;
        }
        return hand;
    }

    public static Integer point(List<String> allHands) {
        Integer point = 0;
        for (int i = 0; i < allHands.size(); i++) {
            String hand = allHands.get(i);
            int len = 0;
            if (StringUtils.isNotEmpty(hand)) {
                len = StringUtils.length(hand) / 2;
                if (len > 1 && len < 16)
                    point += StringUtils.length(hand) / 2;
            } else {
                point += 32;
            }
        }
        return point;
    }
}
