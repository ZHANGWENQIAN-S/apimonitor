package com.wzy.bp.game;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wzy.bp.http.client.HttpClientHandlerForAI;
import com.wzy.bp.model.VsGameRequestHistory;
import com.wzy.bp.util.ResponseEnum;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.*;

public class DdzGame {
    /**
     * 斗地主游戏服务
     */
    public static DdzGame instance = new DdzGame();

    private static class KeySet {
        static final String CARDS = "cards";
        static final String PLAYURL = "playUrl";
        static final String DOUBLEURL = "doubleUrl";
        static final String RESPONDECODE = "responseCode";
        static final String CODE = "code";
        static final String FIRSTCALLER = "firstCaller";
        static final String RECALL = "reCall";
        static final String CALLURL = "callUrl";
        static final String ASLORDS = "asLords";
        static final String DOUBLE = "double";
        static final String POOLCARDS = "poolCards";
        static final String SCORES = "scores";
        static final String RESULT = "result";
        static final String SEATNO = "seatNo";
        static final String ALLHANDS = "allHands";
        static final String SCOREURL = "scoreUrl";
        static final String SCORE = "score";//叫分模式
        static final String CALL = "call";//叫地主模式
        static final String LEVEL = "level";//机器人等级
        static final String HISTORY = "history";
        static final String GAMETYPE = "gameType";//游戏类型 叫分score叫地主call
        static final String INITPOSITION = "initPosition";//起始位置
        static final String TIME = "time";//起始位置
        static final String INITCARDURL = "initCardUrl";//起始位置
        static final String INITCARD = "initCard";//起始手牌
        static final String DARKTYPE = "darkType";//明暗牌
        static final String ROBOTINFO = "robotInfo";//机器人你信息
    }

    private DdzGame() {
    }

    public static DdzGame getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        JSONObject req = new JSONObject();
        req.put(KeySet.INITPOSITION, 0);
        req.put(KeySet.LEVEL, 1);
        req.put(KeySet.INITCARDURL, "");
        List<List<Integer>> initCard = new ArrayList<>();
        initCard = DdzGame.getInstance().handsNew();
        String card1 = "16,29,4,17,30,5,12,25,11,24,7,20,6,19,32,13,26";
        String card2 = "18,31,44,14,27,38,37,10,23,36,9,8,45,2,15,52,53";
        String card3 = "21,34,47,22,35,48,49,28,39,40,41,42,43,33,46,51,50";

        List<Integer> card1List = new ArrayList<>();
        List<Integer> card2List = new ArrayList<>();
        List<Integer> card3List = new ArrayList<>();
        String list1[] = card1.split(",");
        String list2[] = card2.split(",");
        String list3[] = card3.split(",");
        for (int i = 0; i < 17; i++) {
            card1List.add(Integer.valueOf(list1[i]));
            card2List.add(Integer.valueOf(list2[i]));
            card3List.add(Integer.valueOf(list3[i]));
        }
        initCard.add(card1List);
        initCard.add(card2List);
        initCard.add(card3List);
        req.put(KeySet.INITCARD, initCard);
        List<Integer> poolCards = new ArrayList<>();
        poolCards.add(3);
        poolCards.add(0);
        poolCards.add(1);
        JSONObject robotInfo1 = new JSONObject();
        JSONObject robotInfo2 = new JSONObject();
        JSONObject robotInfo3 = new JSONObject();
        robotInfo1.put(KeySet.CALLURL, "http://120.53.1.58:29031/ddzAI/ddz/bemaster?access_token=91caf550-47cd-4604-9451-94fa1ae5c28a");
        robotInfo1.put(KeySet.DOUBLEURL, "http://120.53.1.58:29031/ddzAI/ddz/doubleScore?access_token=91caf550-47cd-4604-9451-94fa1ae5c28a");
        robotInfo1.put(KeySet.PLAYURL, "http://120.53.1.58:29031/ddzAI/ddz/playcard?access_token=91caf550-47cd-4604-9451-94fa1ae5c28a");
        robotInfo1.put(KeySet.RECALL, "http://120.53.1.58:29031/ddzAI/ddz/remaster?access_token=91caf550-47cd-4604-9451-94fa1ae5c28a");
        robotInfo2.put(KeySet.CALLURL, "http://120.53.1.58:29031/ddzAI/ddz/bemaster?access_token=91caf550-47cd-4604-9451-94fa1ae5c28a");
        robotInfo2.put(KeySet.DOUBLEURL, "http://120.53.1.58:29031/ddzAI/ddz/doubleScore?access_token=91caf550-47cd-4604-9451-94fa1ae5c28a");
        robotInfo2.put(KeySet.PLAYURL, "http://120.53.1.58:29031/ddzAI/ddz/playcard?access_token=91caf550-47cd-4604-9451-94fa1ae5c28a");
        robotInfo2.put(KeySet.RECALL,  "http://120.53.1.58:29031/ddzAI/ddz/remaster?access_token=91caf550-47cd-4604-9451-94fa1ae5c28a");
        robotInfo3.put(KeySet.CALLURL, "http://120.53.1.58:29031/ddzAI/ddz/bemaster?access_token=91caf550-47cd-4604-9451-94fa1ae5c28a");
        robotInfo3.put(KeySet.DOUBLEURL, "http://120.53.1.58:29031/ddzAI/ddz/doubleScore?access_token=91caf550-47cd-4604-9451-94fa1ae5c28a");
        robotInfo3.put(KeySet.PLAYURL, "http://120.53.1.58:29031/ddzAI/ddz/playcard?access_token=91caf550-47cd-4604-9451-94fa1ae5c28a");
        robotInfo3.put(KeySet.RECALL, "http://120.53.1.58:29031/ddzAI/ddz/remaster?access_token=91caf550-47cd-4604-9451-94fa1ae5c28a");

        List<JSONObject> robotInfo = new ArrayList<>();
        robotInfo.add(robotInfo1);
        robotInfo.add(robotInfo2);
        robotInfo.add(robotInfo3);
        req.put(KeySet.ROBOTINFO, robotInfo);
        req.put(KeySet.POOLCARDS, poolCards);
        req.put(KeySet.INITCARDURL, "NOURL");
        System.out.println(req);
        String i = DdzGame.getInstance().doAction(req);
        System.out.println(i);
    }

    public String doAction(JSONObject AIInfo) {
        JSONObject result = new JSONObject();
        String type = AIInfo.getString(KeySet.GAMETYPE);
        int initPosition = 0;
        int lordNo = 0;
        List<List<Integer>> history = new ArrayList<>();//历史出牌信息
        int level = 1;//机器人等级1-3
        int time = 5000;//机器人思考时间5s
        boolean doubleScore = false;
        String initCard = "";
        JSONArray hisInfo = new JSONArray();
        if (AIInfo.containsKey(KeySet.INITPOSITION)) {
            initPosition = AIInfo.getInteger(KeySet.INITPOSITION);
        }
        if (AIInfo.containsKey(KeySet.LEVEL)) {
            level = AIInfo.getInteger(KeySet.LEVEL);
        }
        if (AIInfo.containsKey(KeySet.TIME)) {
            time = AIInfo.getInteger(KeySet.TIME);
        }
        if (AIInfo.containsKey(KeySet.INITCARD)) {
            initCard = AIInfo.getString(KeySet.INITCARD);
        }
        String initCardUrl = "";
        if (AIInfo.containsKey(KeySet.INITCARDURL) && StringUtils.isNotEmpty(AIInfo.getString(KeySet.INITCARDURL))) {
            initCardUrl = AIInfo.getString(KeySet.INITCARDURL);
        } else {
            result.put("code", ResponseEnum.RESPONSE_ERROR_403.getCode());
            result.put("msg", ResponseEnum.RESPONSE_ERROR_403.getMsg());
            result.put("error", "无发牌地址");
            return JSONObject.toJSONString(result);
        }
        String gameId = UUID.randomUUID().toString().replaceAll("-", "");
        List<List<Integer>> allHands = new ArrayList<>();//暂时无发牌逻辑
        List<Integer> poolCards = new ArrayList<>();
        JSONObject initCardJson = new JSONObject();
        if (StringUtils.isEmpty(initCard)) {
            if (StringUtils.equals(initCardUrl, "NOURL")) {
                allHands = DdzGame.getInstance().handsNew();
                poolCards = allHands.get(3);
                allHands.remove(3);
            } else {
                String initCardRes = HttpClientHandlerForAI.getInstance().sendRequest(initCardUrl, "");
                initCardJson = JSONObject.parseObject(initCardRes);
                if (initCardJson.getInteger("code") != 200) {
                    result.put("code", ResponseEnum.RESPONSE_ERROR_403.getCode());
                    result.put("msg", ResponseEnum.RESPONSE_ERROR_403.getMsg());
                    result.put("error", "起手牌错误");
                    return JSONObject.toJSONString(result);
                }
                allHands = (List<List<Integer>>) initCardJson.get(KeySet.INITCARD);
                poolCards = (List<Integer>) initCardJson.get(KeySet.POOLCARDS);
            }


        } else {
            allHands = (List<List<Integer>>) AIInfo.get(KeySet.INITCARD);
            poolCards = (List<Integer>) AIInfo.get(KeySet.POOLCARDS);

        }
        result.put("start", allHands.toString());
        result.put("gameId", gameId);
        result.put("reqHis", hisInfo);
        List<Integer> scores = new ArrayList<>();
        List<Integer> asLords = new ArrayList<>();
        List<Integer> doubleRec = new ArrayList<>();
        int firstCaller = 3;//第一个叫的玩家，0-2,判断<3时合法
        //叫分或者叫地主
        List<JSONObject> robotList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            JSONObject req = new JSONObject();
            int seatNo = (i + initPosition) % 3;
            int levelTmp = 3;
            JSONObject robot = AIInfo.getJSONArray(KeySet.ROBOTINFO).getJSONObject(seatNo);
            if (robot.containsKey(KeySet.LEVEL)) {
                levelTmp = robot.getInteger(KeySet.LEVEL);
            }
            //long id
            List<Integer> hands = allHands.get(seatNo);

            boolean darkType = true;

            if (AIInfo.containsKey(KeySet.DARKTYPE)) {
                darkType = AIInfo.getBoolean(KeySet.DARKTYPE);
            }
            if (darkType) {
                List<List<Integer>> ah = new ArrayList<>();
                ah.add(hands);
                req.put(KeySet.ALLHANDS, ah);
            } else {
                req.put(KeySet.ALLHANDS, allHands);
                req.put(KeySet.POOLCARDS, poolCards);
            }
            req.put(KeySet.LEVEL, levelTmp);
            req.put(KeySet.SEATNO, seatNo);
            robot.put("req", req);
            robotList.add(robot);
            if (StringUtils.equals(AIInfo.getString(KeySet.GAMETYPE), KeySet.SCORE)) {
                req.put(KeySet.SCORES, scores);
                String url = robot.getString(KeySet.SCOREURL);
                String scoreRes = HttpClientHandlerForAI.getInstance().sendRequest(url, JSONObject.toJSONString(req));
                JSONObject scoreJson = JSONObject.parseObject(scoreRes);
                addReqHis(hisInfo, url, scoreJson, gameId);
                if (scoreJson.getInteger(KeySet.RESPONDECODE) == 1) {
                    if (scoreJson.containsKey(KeySet.RESULT)) {
                        int resultScore = scoreJson.getInteger(KeySet.RESULT);
                        scores.add(resultScore);
                        if (resultScore == 3) {
                            lordNo = seatNo;
                            break;
                        }
                        if (scores.size() == 3 && resultScore == 0) {
                            result.put("code", ResponseEnum.RESPONSE_NO_SCORE.getCode());
                            result.put("msg", ResponseEnum.RESPONSE_NO_SCORE.getMsg());
                            return JSONObject.toJSONString(result);
                        }
                    } else {
                        result.put("code", ResponseEnum.RESPONSE_NO_SCORE.getCode());
                        result.put("msg", ResponseEnum.RESPONSE_NO_SCORE.getMsg());
                        return JSONObject.toJSONString(result);
                    }
                }
            } else {
                req.put(KeySet.ASLORDS, asLords);
                String url = robot.getString(KeySet.CALLURL);
                if (firstCaller != 3) {
                    url = robot.getString(KeySet.RECALL);
                    req.put(KeySet.FIRSTCALLER, firstCaller);
                }
                String callRes = HttpClientHandlerForAI.getInstance().sendRequest(url, JSONObject.toJSONString(req));
                JSONObject callJson = JSONObject.parseObject(callRes);
                addReqHis(hisInfo, url, callJson, gameId);
                if (callJson.containsKey(KeySet.RESPONDECODE) && callJson.getInteger("responseCode") == 1) {
                    if (callJson.containsKey(KeySet.RESULT)) {
                        int resultCall = callJson.getInteger(KeySet.RESULT);
                        if (resultCall == 1) {

                            if (firstCaller != 3) {
                                asLords.add(2);

                            } else {
                                asLords.add(1);
                            }
                            if (asLords.size() == 3) {
                                lordNo = seatNo;
                            }
                            if (firstCaller == 3) {
                                firstCaller = seatNo;
                            }
                        } else {
                            asLords.add(0);
                        }
                        if (asLords.size() == 3 && firstCaller == 3) {
                            result.put("code", ResponseEnum.RESPONSE_NO_CALL.getCode());
                            result.put("msg", ResponseEnum.RESPONSE_NO_CALL.getMsg());
                            return JSONObject.toJSONString(result);
                        }
                    } else {
                        result.put("code", ResponseEnum.RESPONSE_NO_CALL.getCode());
                        result.put("msg", ResponseEnum.RESPONSE_NO_CALL.getMsg());
                        return JSONObject.toJSONString(result);
                    }
                }
            }
        }
        //多线程同时请求是否加倍,不适用轮询
        ExecutorService executor = Executors.newCachedThreadPool();

        DoubleTask task1 = new DoubleTask(robotList.get(0), asLords, scores, poolCards, hisInfo, gameId);
        DoubleTask task2 = new DoubleTask(robotList.get(1), asLords, scores, poolCards, hisInfo, gameId);
        DoubleTask task3 = new DoubleTask(robotList.get(2), asLords, scores, poolCards, hisInfo, gameId);
        Future<JSONObject> doubleRes1 = executor.submit(task1);
        Future<JSONObject> doubleRes2 = executor.submit(task2);
        Future<JSONObject> doubleRes3 = executor.submit(task3);
        boolean doubleFinish = false;
        long start = System.currentTimeMillis();
        while (!doubleFinish) {
            long end = System.currentTimeMillis();

            if (doubleRes1.isDone() && doubleRes2.isDone() && doubleRes3.isDone()) {
                try {
                    JSONObject doubleJson1 = doubleRes1.get();
                    JSONObject doubleJson2 = doubleRes2.get();
                    JSONObject doubleJson3 = doubleRes3.get();
                    System.out.println("double" + doubleJson1);
                    System.out.println("double" + doubleJson2);
                    System.out.println("double" + doubleJson3);
                    if (doubleJson1.getInteger(KeySet.RESPONDECODE) == 1 && doubleJson2.getInteger(KeySet.RESPONDECODE) == 1 && doubleJson3.getInteger(KeySet.RESPONDECODE) == 1) {
                        doubleRec.add(doubleJson1.getInteger(KeySet.RESULT));
                        doubleRec.add(doubleJson2.getInteger(KeySet.RESULT));
                        doubleRec.add(doubleJson3.getInteger(KeySet.RESULT));
                        break;
                    }
                    doubleFinish = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    result.put("code", ResponseEnum.RESPONSE_NO_DOUBLE.getCode());
                    result.put("msg", ResponseEnum.RESPONSE_NO_DOUBLE.getMsg());
                    return JSONObject.toJSONString(result);
                } catch (ExecutionException e) {
                    result.put("code", ResponseEnum.RESPONSE_NO_DOUBLE.getCode());
                    result.put("msg", ResponseEnum.RESPONSE_NO_DOUBLE.getMsg());
                    return JSONObject.toJSONString(result);
                }
            }
            if (end - start > 3000) {
                break;
            }
        }
        executor.shutdown();
        boolean gameFinish = false;
        int seatNo = lordNo;

        //增加地主的牌
        List<List<Integer>> newHands = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<Integer> hand = new ArrayList<>();
//            if(i==lordNo){
//                hand.addAll(poolCards);
//            }
            newHands.add(hand);
        }
        while (!gameFinish) {
            seatNo = seatNo % 3;
            JSONObject robotInfo = robotList.get(seatNo);
            String playUrl = robotInfo.getString(KeySet.PLAYURL);
            JSONObject req = robotInfo.getJSONObject("req");
            boolean darkType = true;
            List<Integer> hands = allHands.get(seatNo);
            if (AIInfo.containsKey(KeySet.DARKTYPE)) {
                darkType = AIInfo.getBoolean(KeySet.DARKTYPE);
            }
            if (darkType) {
                List<List<Integer>> ah = new ArrayList<>();
                ah.add(hands);
                req.put(KeySet.ALLHANDS, ah);
                if (seatNo == lordNo) {
                    req.put(KeySet.POOLCARDS, poolCards);

                }
            } else {
                req.put(KeySet.ALLHANDS, allHands);
                req.put(KeySet.POOLCARDS, poolCards);
            }
            req.put("lordNo", lordNo);
            req.put(KeySet.HISTORY, history);
            System.out.println(req);
            String playRes = HttpClientHandlerForAI.getInstance().sendRequest(playUrl, JSONObject.toJSONString(req));
//            if(allHands.get(0).size()==0||allHands.get(2).size()==0||allHands.get(1).size()==0){
//                gameFinish = true;
//            }
            JSONObject playResJson = JSONObject.parseObject(playRes);
            addReqHis(hisInfo, playUrl, playResJson, gameId);

            if (playResJson.getInteger(KeySet.RESPONDECODE) != 1) {
                result.put("code", 212);
                result.put("msg", "请求打牌接口错误");
                return JSONObject.toJSONString(result);
            }
            System.out.println(playResJson);
            List<Integer> card = (List<Integer>) playResJson.get(KeySet.CARDS);
            List<Integer> historyNow = new ArrayList<>();

            if (card.size() == 0) {
                historyNow.add(seatNo);
                history.add(historyNow);
                seatNo++;
                continue;
            } else {
                historyNow = card;
                List<Integer> his = new ArrayList<>();
                his = newHands.get(seatNo);
                his.addAll(card);//牌数不减少
                newHands.set(seatNo, his);
                historyNow.add(0, seatNo);
                System.out.println(historyNow);
                history.add(historyNow);
                System.out.println("hist:" + history);
            }
            if ((seatNo == lordNo && newHands.get(seatNo).size() == 20) || (seatNo != lordNo && newHands.get(seatNo).size() == 17)) {
                result.put("winner", seatNo);
                gameFinish = true;
            }
            seatNo++;
        }
        result.put("history", history);
        JSONObject env = new JSONObject();
        result.put("env", env);
        int point = 0;
        result.put("point", point);
        result.put("code", 200);
        result.put(KeySet.DOUBLE, doubleRec);
        return JSONObject.toJSONString(result);
    }

    private void addReqHis(JSONArray his, String url, JSONObject resJson, String gameId) {
        VsGameRequestHistory vsGameRequestHistory = new VsGameRequestHistory();
        vsGameRequestHistory.setCostTime(resJson.getInteger("time"));
        vsGameRequestHistory.setGameId(gameId);
        vsGameRequestHistory.setLog(resJson.toJSONString());
        String URI = url.substring(url.lastIndexOf("/"));

        vsGameRequestHistory.setURI(URI);
        vsGameRequestHistory.setStatus(resJson.getInteger("responseCode"));
        his.add(vsGameRequestHistory);
    }

    /**
     * 随机生成牌谱
     *
     * @return
     */
    private List<List<Integer>> handsNew() {
        List<List<Integer>> allHands = new ArrayList<>();
        List<Integer> cards = new ArrayList<>();
        List<Integer> card01 = new ArrayList<>();
        List<Integer> card02 = new ArrayList<>();
        List<Integer> card03 = new ArrayList<>();
        for (int i = 53; i >= 0; i--) {
            cards.add(i);
        }
        for (int i = 53; i > 2; i--) {
            Random r = new Random();
            int number = r.nextInt(i);
            card01.add(cards.get(number));
            cards.remove(number);
            i--;
            int num2 = r.nextInt(i);

            card02.add(cards.get(num2));
            cards.remove(num2);

            i--;

            int num3 = r.nextInt(i);

            card03.add(cards.get(num3));
            cards.remove(num3);

        }
        allHands.add(card01);
        allHands.add(card02);
        allHands.add(card03);
        allHands.add(cards);
        return allHands;
    }

    class DoubleTask implements Callable<JSONObject> {
        private JSONObject req = new JSONObject();
        private List<Integer> asLords = new ArrayList<>();
        private List<Integer> scores = new ArrayList<>();
        private List<Integer> poolCards = new ArrayList<>();
        private JSONArray hisInfo = new JSONArray();
        private String gameId = "";

        DoubleTask(JSONObject req, List<Integer> asLords, List<Integer> scores, List<Integer> poolCards, JSONArray hisInfo, String gameId) {
            this.req = req;
            this.asLords = asLords;
            this.scores = scores;
            this.poolCards = poolCards;
            this.hisInfo = hisInfo;
            this.gameId = gameId;
        }

        @Override
        public JSONObject call() throws Exception {
            JSONObject json;
            JSONObject reqMsg = req.getJSONObject("req");
            String url = req.getString(KeySet.DOUBLEURL);
            if (asLords.size() > 0) {
                reqMsg.put(KeySet.ASLORDS, asLords);
            } else if (scores.size() > 0) {
                reqMsg.put(KeySet.SCORES, scores);
            }
            reqMsg.put("doubles", new ArrayList<>());
            reqMsg.put(KeySet.POOLCARDS, poolCards);
            String res = HttpClientHandlerForAI.getInstance().sendRequest(url, JSONObject.toJSONString(reqMsg));
            json = JSONObject.parseObject(res);
            addReqHis(hisInfo, url, json, gameId);
            return json;
        }
    }

}
