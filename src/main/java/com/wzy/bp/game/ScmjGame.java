package com.wzy.bp.game;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wzy.bp.http.client.HttpClientHandlerForAI;
import com.wzy.bp.util.JsonUtils;
import com.wzy.bp.util.MahjongUtils;
import com.wzy.bp.util.ResponseEnum;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.concurrent.*;

/**
 * 四川麻将游戏服务
 */
public class ScmjGame {

    public static ScmjGame instance = new ScmjGame();

    private static class KeySet {
        static final String CURPOS   = "curPos";
        static final String WALL     = "wall";
        static final String MSG      = "msg";
        static final String HUPAI       = "huPai";
        static final        String ACTION   = "action";
        static final        String ZIMO     = "ziMo";
        static final        String MAXFAN   = "maxFan";
        static final        String TYPE     = "type";
        static final        String USERINFO = "userInfo";
        static final        String LEGALACT = "legalAct";
        static final        String NEXTCARD = "nextCardUrl";
        static final        String HISTORY  = "history";
        static final        String GAMELOGIC      = "gameLogicUrl";
        static final        String CHANGETILESURL = "changeTilesUrl";
        static final        String PROMISEURL     = "promiseUrl";
        static final        String AILEVEL        = "AILevel";
        static final        String USERID         = "userId";
        static final        String TILES          = "tiles";
        static final        String GAMEID         = "gameId";
        static final        String CODE           = "code";
        static final        String EXCHANGETYPE   = "exchangeType";
        static final        String GRADE          = "grade";
        static final        String DEALER         = "dealer";
        static final        String RULE           = "rule";
        static final        String ROBOTINFO      = "robotInfo";
    }

    private ScmjGame() {
    }

    public static ScmjGame getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        JSONObject req = new JSONObject();
        req.put("initCardUrl", "http://localhost:8011/scmj/initcard");
        req.put(KeySet.DEALER, 0);
        req.put(KeySet.EXCHANGETYPE, 1);
        List<Integer> grade = new ArrayList<>();
        grade.add(0);
        grade.add(-1);
        grade.add(1);
        grade.add(10);
        JSONArray robotInfo = new JSONArray();

        req.put(KeySet.NEXTCARD, "http://localhost:8011/scmj/nextcard");

        List<Object> userInfo = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Random random = new Random();
            JSONObject robot = new JSONObject();
            robot.put(KeySet.USERID, i);
            robot.put(KeySet.PROMISEURL, "http://localhost:8011/scmj/promisecolor");
            robot.put(KeySet.CHANGETILESURL, "http://localhost:8011/scmj/changetiles");
            robot.put(KeySet.GAMELOGIC, "http://localhost:8011/scmj/gamelogic");
            robot.put(KeySet.AILEVEL, random.nextInt(3));
            robotInfo.add(robot);
            List<Integer> usrInfo = new ArrayList<>();
            usrInfo.add(10000);
            usrInfo.add(1);
            userInfo.add(usrInfo);

        }

        JSONObject rule = new JSONObject();
        rule.put(KeySet.TYPE, 1);
        rule.put(KeySet.MAXFAN, 512);
        rule.put(KeySet.ZIMO, 1);
        req.put(KeySet.ROBOTINFO, robotInfo);
        req.put(KeySet.GRADE, grade);
        req.put(KeySet.RULE, rule);
        req.put(KeySet.USERINFO, userInfo);
        System.out.println(JSON.toJSONString(req));
        String i = ScmjGame.getInstance().doAction(req);
        System.out.println(i);
    }

    public String doAction(JSONObject AIInfo) {
        Map result = new HashMap();
        JsonUtils jsonUtils = new JsonUtils();
        List<List<Object>> history = new ArrayList<>();//历史信息
        Map<String, Object> AIInfoMap =new HashMap<>();//用于将AIInfo转换为Map
        Iterator it =AIInfo.entrySet().iterator();
        if (!checkAIInfo(AIInfo)) {
            return jsonUtils.toJSONString(result);
        }
        String rule = AIInfo.getString(KeySet.RULE);
        int dealer = AIInfo.getInteger(KeySet.DEALER);
        String gameId = UUID.randomUUID().toString();
        int exchangeType = AIInfo.getInteger(KeySet.EXCHANGETYPE);
        List<Integer> getCardGrade = (List<Integer>) AIInfo.get(KeySet.GRADE);
        String getCardUrl = AIInfo.getString("initCardUrl");
        List<List<String>> hands = new ArrayList<>();
        //获取手牌
        Map<String,Object> getCardReq = new HashMap();
        getCardReq.put(KeySet.DEALER, dealer);
        getCardReq.put(KeySet.GAMEID, gameId);
        getCardReq.put(KeySet.EXCHANGETYPE, exchangeType);
        getCardReq.put(KeySet.GRADE, getCardGrade);
        String getCardRes = HttpClientHandlerForAI.getInstance().sendRequest(getCardUrl, jsonUtils.toJSONString(getCardReq));
        //解析起手牌返回JSON
        JSONObject getCardJson = JSON.parseObject(getCardRes);
//        Map getCardJson = jsonUtils.toMap(getCardRes,Map.class);
        List<String> tiles = new ArrayList<>();
        if (getCardJson.getInteger(KeySet.CODE) == 0 && getCardJson.containsKey(KeySet.TILES)) {
//        if (Integer.valueOf(getCardJson.get(KeySet.CODE).toString()) == 0 && getCardJson.containsKey(KeySet.TILES)) {
//            tiles = (List<String>) getCardJson.get(KeySet.TILES);
            for (int i = 0; i < 4; i++) {
                List<String> hand = new ArrayList<>();
                if (i == dealer) {
                    addHistory(history, i, 0, tiles.get(i) + tiles.get(4));//增加起手牌信息
                    hand.add(tiles.get(i) + tiles.get(4));
                    hands.add(hand);

                } else {
                    hand.add(tiles.get(i));
                    hands.add(hand);
                    addHistory(history, i, 0, tiles.get(i));//增加起手牌信息
                }


            }
        }
        JSONArray robotList = AIInfo.getJSONArray(KeySet.ROBOTINFO);
        List<JSONObject> robotInfo = robotPromiseInfo(robotList, getCardJson);
        List<String> exchangeTiles = new ArrayList<>();
        List<String> withoutCard = new ArrayList<>();
        //换三张请求
        if (exchangeType != 0) {
            exchangeTiles = multiRequst(robotInfo, KeySet.CHANGETILESURL);
            if (exchangeTiles.size() == 4) {
                //robotInfo = exchangeTileByType(robotInfo);
                for (int i = 0; i < 4; i++) {
                    addChangeHistory(history, i, exchangeType, 17, exchangeTiles);//增加换牌信息
                    List<String> hand = hands.get(i);
                    String tile = hand.get(0);
                    String exchangeTile = exchangeTiles.get(i);
                    String recTile = exchangeTiles.get((i + exchangeType + 2) % 4);

                    for (int j = 0; j < 6; j = j + 2) {
                        tile = tile.replaceFirst(exchangeTile.substring(j, j + 2), recTile.substring(j, j + 2));
                    }
                    hand.set(0, tile);
                    hands.set(i, hand);
                }
            }
        }
        withoutCard = multiRequst(robotInfo, KeySet.PROMISEURL);
        //异步请求定缺
        if (withoutCard.size() == 4) {
            //robotInfo = exchangeTileByType(robotInfo);
            for (int i = 0; i < 4; i++) {
                addHistory(history, i, 16, withoutCard.get(i));//增加定缺信息

            }
        }
        boolean gameFinish = false;
        //addHistory(history, dealer, 1, tiles.get(4));//不需要庄家抓牌的信息
        int curPos = dealer;
        List<String> legalAct = new ArrayList<>();
        JSONArray status = new JSONArray();//和牌状态
        String walls = tiles.get(5);
        while (!gameFinish) {
            curPos = curPos % 4;
            int nextPos = (curPos + 1) % 4;
            List<Integer> nextPosMove = new ArrayList<>();
            List<String> hand = hands.get(curPos);
            List<String> action = new ArrayList<>();
            Map<String,Object> robot = robotList.getJSONObject(curPos);
            if(robot.containsKey(KeySet.HUPAI))
            robot.put(KeySet.HISTORY, history);
            System.out.println(jsonUtils.toJSONString(robotList));
            robot.put(KeySet.CURPOS, curPos);
            robot.put(KeySet.LEGALACT, legalAct);
            robot.put(KeySet.USERINFO, AIInfo.get(KeySet.USERINFO));
            robot.put(KeySet.RULE, AIInfo.get(KeySet.RULE));
            System.out.println(jsonUtils.toJSONString(robot));
            System.out.println(jsonUtils.toJSONString(robot));
            String playUrl = jsonUtils.toJSONString(robot.get(KeySet.GAMELOGIC));
            String playRes = HttpClientHandlerForAI.getInstance().sendRequest(playUrl, jsonUtils.toJSONString(robot));
            Map<String,Object> playJson = jsonUtils.toMap(playRes);
            if (playJson.containsKey(KeySet.CODE) && Integer.valueOf(playJson.get(KeySet.CODE).toString()) == 0) {
                action = (List<String>) playJson.get(KeySet.ACTION);
                int actionType = Integer.valueOf(action.get(0));
                if (actionType != 4) {
                    addHistory(history, curPos, actionType, action.get(1));

                }
                if (!StringUtils.contains(hand.get(0), action.get(1))) {
                    System.out.println("手牌是:" + hand.get(0) + "不包括行动牌:" + action.get(1));
                }
                switch (actionType) {
                    case 2:
                        //手切询问其余用户
                        MahjongUtils.changeHandTiles(hand, 2, action.get(1));
                        nextPosMove = askOtherPosition(history, curPos, action, robotList, AIInfo, hands);
                        break;
                    case 3:
                        //明杠，调用摸牌
                        break;
                    case 4:
                        //补杠,询问其他家是否和，然后抓牌
                        nextPosMove = askOtherPosition(history, curPos, action, robotList, AIInfo, hands);
                        break;
                    case 5:
                        //暗杠，摸牌
                        nextPos = curPos;
                        MahjongUtils.changeHandTiles(hand, 5, action.get(1));
                        break;
                    case 6:
                        //摸切
                        nextPosMove = askOtherPosition(history, curPos, action, robotList, AIInfo, hands);
                        MahjongUtils.changeHandTiles(hand, 6, action.get(1));
                        break;
                    case 7:
                        //血战减少玩家,血流直接++
                        curPos = nextPos;
                        break;
                    case 8:
                        //点和
                        break;
                    case 14:
                        //碰牌
                        break;
                    case 18:
                        //过牌
                        curPos++;
                        break;
                    case 21:
                        //抢杠胡
                        break;

                }

            } else {
                result.put(KeySet.CODE, ResponseEnum.RESPONSE_ERROR_403.getCode());
                result.put(KeySet.MSG, "请求gamelogic接口错误");
                return jsonUtils.toJSONString(result);
            }
            String nextCard = AIInfo.getString(KeySet.NEXTCARD);
            if (nextPosMove.size() > 0) {
                for (int i = 0; i < nextPosMove.size(); i++) {

                }
                nextPos = nextPosMove.get(0);
                int nextMove = nextPosMove.get(1);
                if (walls.length() > 0) {
                    //牌墙长度大于0
                    if (nextMove == 8 || nextMove == 21) {
                        nextPos = (nextPos + 1) % 4;
                        curPos = nextPos;
                    } else if (nextMove == 14) {
                        curPos = nextPos;
                        MahjongUtils.changeHandTiles(hands.get(nextPos), 14, action.get(1));
                        continue;
                    } else if (nextMove == 4) {
                        //明杠补杠时使用
                        curPos = nextPos;
                        MahjongUtils.changeHandTiles(hands.get(nextPos), Integer.valueOf(action.get(0)), action.get(1));
                    } else {
                        if (Integer.valueOf(action.get(0)) != 2) {
                            MahjongUtils.changeHandTiles(hand, Integer.valueOf(action.get(0)), action.get(1));
                        }
                        curPos = nextPos;
                    }
                } else {
                    //海底只可以和，不可以吃碰杠
                    break;
                }

            }
            System.out.println(jsonUtils.toJSONString(robotList));

            robot.put(KeySet.CURPOS, nextPos);
            robot.put(KeySet.WALL, walls);
            System.out.println(jsonUtils.toJSONString(robotList));

            String newCardRes = HttpClientHandlerForAI.getInstance().sendRequest(nextCard, jsonUtils.toJSONString(robot));
            Map newCardJson = jsonUtils.toMap(newCardRes);
                if (Integer.valueOf(newCardJson.get(KeySet.CODE).toString()) == 0) {
                    String tile = jsonUtils.toJSONString(newCardJson.get("tile"));
                    addHistory(history, nextPos, 1, tile);
                    int tilePos = walls.indexOf(tile);
                    walls = walls.replaceFirst(tile, "");
                    MahjongUtils.changeHandTiles(hands.get(nextPos), 1, tile);
            }

        }

        return jsonUtils.toJSONString(history);

    }


    /**
     * 轮询其他位置是否吃碰杠和
     *
     * @param history
     * @param curPos
     * @param action
     * @param robotList1
     * @param AIInfo
     * @param hands
     * @return
     */
    private List<Integer> askOtherPosition(List<List<Object>> history, int curPos, List<String> action, JSONArray robotList1, JSONObject AIInfo, List<List<String>> hands) {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<GameLogicTask> gameLogicTasks = new ArrayList<>();
        List<Future<String>> futures = new ArrayList<>();
        List<String> moves = new ArrayList<>();
        int nextPosition = curPos;
        List<Integer> nextPosMove = new ArrayList<>();
        JSONArray robotList = new JSONArray();
        for (int i = 0; i <= 3; i++) {
            JSONObject robot = robotList1.getJSONObject(i);

            List<String> legalAct = new ArrayList<>();
            robot.put(KeySet.HISTORY, history);
            robot.put(KeySet.CURPOS, i);
            robot.put(KeySet.LEGALACT, legalAct);
            robot.put(KeySet.USERINFO, AIInfo.get(KeySet.USERINFO));
            robot.put(KeySet.RULE, AIInfo.get(KeySet.RULE));
            robotList.add(robot);

        }
        System.out.println(curPos + "" + (curPos + 1) % 4 + "" + (curPos + 2) % 4 + "" + (curPos + 3) % 4);
        System.out.println(JSONObject.toJSONString(robotList));
        GameLogicTask gameLogicTask01 = new GameLogicTask(robotList.getJSONObject((curPos + 1) % 4), robotList.getJSONObject((curPos + 1) % 4).getString(KeySet.GAMELOGIC));
        GameLogicTask gameLogicTask02 = new GameLogicTask(robotList.getJSONObject((curPos + 2) % 4), robotList.getJSONObject((curPos + 2) % 4).getString(KeySet.GAMELOGIC));
        GameLogicTask gameLogicTask03 = new GameLogicTask(robotList.getJSONObject((curPos + 3) % 4), robotList.getJSONObject((curPos + 3) % 4).getString(KeySet.GAMELOGIC));
        if (gameLogicTask01.req == gameLogicTask02.req) {
            System.out.println("WHT");
        }
        Future<String> gameLogicRes01 = executor.submit(gameLogicTask01);
        Future<String> gameLogicRes02 = executor.submit(gameLogicTask02);
        Future<String> gameLogicRes03 = executor.submit(gameLogicTask03);
        executor.shutdown();
        long gameLogicStart = System.currentTimeMillis();
        boolean gameLogicCon = true;
        int nonNum = 0;//点和个数
        int pong = 0;//碰杠位置
        int kan = 0;//杠位置,3为没有位置填充
        boolean chi = false;//下家是否吃
        while (gameLogicCon) {
            if (gameLogicRes01.isDone() && gameLogicRes02.isDone() && gameLogicRes03.isDone()) {
                try {
                    moves.add(gameLogicRes01.get());
                    moves.add(gameLogicRes02.get());
                    moves.add(gameLogicRes03.get());
                    System.out.println(moves);
                    for (int i = 2; i >= 0; i--) {

                        String movement = moves.get(i);
                        if (StringUtils.isNotEmpty(movement)) {
                            switch (movement) {
                                case "8":
                                    if (nonNum == 0) {
                                        nextPosition = (curPos + i + 1) % 4;
                                        nextPosMove.add(nextPosition);
                                        nextPosMove.add(8);
                                    }
                                    nonNum++;
                                    addHistory(history, (curPos + i + 1) % 4, 8, action.get(1));
                                    break;
                                case "21":
                                    if (nonNum == 0) {
                                        nextPosition = (curPos + i) % 4;
                                        nextPosMove.add(nextPosition);
                                        nextPosMove.add(21);
                                    }
                                    nonNum++;
                                    addHistory(history, (curPos + i + 1) % 4, 21, action.get(1));
                                    break;
                                case "14":
                                    pong = i + 1;
                                    String hand = hands.get((curPos + i + 1) % 4).get(0);
                                    break;
                                case "3":
                                    kan = i + 1;
                                    break;
                                case "18":
                                    //addHistory(history, (curPos + i) % 4, 18, action.get(1));
                                    break;

                            }

                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return nextPosMove;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    return nextPosMove;
                }
                if (nonNum == 0 && !StringUtils.equals(action.get(0), "4")) {
                    if (kan != 0) {
                        addHistory(history, (curPos + kan) % 4, 3, action.get(1));
                        nextPosition = (curPos + kan) % 4;
                        nextPosMove.add(nextPosition);
                        nextPosMove.add(3);
                        gameLogicCon = false;

                    }
                    if (pong != 0) {
                        addHistory(history, (curPos + pong) % 4, 14, action.get(1));
                        nextPosition = (curPos + pong) % 4;
                        nextPosMove.add(nextPosition);
                        nextPosMove.add(14);
                        gameLogicCon = false;

                    }
                    if (kan == 0 && pong == 0 && !chi) {

                        nextPosition = (curPos + 1) % 4;
                        nextPosMove.add(nextPosition);
                        nextPosMove.add(1);
                        gameLogicCon = false;

                    }


                } else if (nonNum == 0 && StringUtils.equals(action.get(0), "4")) {
                    addHistory(history, curPos, 4, action.get(1));

                    nextPosition = curPos;
                    nextPosMove.add(nextPosition);
                    nextPosMove.add(1);
                    gameLogicCon = false;
                }
                break;
            }
            long gameLogicNow = System.currentTimeMillis();
//            if (gameLogicNow - gameLogicStart >= 5000) {
//                gameLogicCon = false;
//            }
        }

        return nextPosMove;


    }

    /**
     * 添加换三张历史
     *
     * @param history
     * @param curPosition
     * @param movement
     * @param exchangeTiles
     */
    private void addChangeHistory(List<List<Object>> history, int curPosition, int type, int movement, List<String> exchangeTiles) {
        List<Object> moveInfo = new ArrayList<>();
        moveInfo.add(curPosition);
        moveInfo.add(movement);
        moveInfo.add(type);
        moveInfo.add(exchangeTiles.get(curPosition));
        moveInfo.add(exchangeTiles.get((curPosition + type + 2) % 4));
        history.add(moveInfo);
    }

    private void addHistory(List<List<Object>> history, int curPosition, int movement, String tile) {
        List<Object> his = new ArrayList<>();
        his.add(curPosition);
        his.add(movement);
        his.add(tile);
        history.add(his);
    }

    /**
     * 按照请求返回值进行换三张
     *
     * @param robotInfo
     * @return
     */
    private List<JSONObject> exchangeTileByType(List<JSONObject> robotInfo) {
        return null;
    }

    /**
     * 异步方法请求
     */
    private List<String> multiRequst(List<JSONObject> robotInfo, String Type) {
        List<String> result = new ArrayList<>();
        ExecutorService executor = Executors.newCachedThreadPool();
        PromiseTask promiseTask01 = new PromiseTask(robotInfo.get(0), Type);
        PromiseTask promiseTask02 = new PromiseTask(robotInfo.get(1), Type);
        PromiseTask promiseTask03 = new PromiseTask(robotInfo.get(2), Type);
        PromiseTask promiseTask04 = new PromiseTask(robotInfo.get(3), Type);
        Future<String> promiseRes01 = executor.submit(promiseTask01);
        Future<String> promiseRes02 = executor.submit(promiseTask02);
        Future<String> promiseRes03 = executor.submit(promiseTask03);
        Future<String> promiseRes04 = executor.submit(promiseTask04);

        boolean promiseConti = true;
        long promiseStart = System.currentTimeMillis();
        executor.shutdown();
        while (promiseConti) {
            long promiseEnd = System.currentTimeMillis();
            if (promiseRes01.isDone() && promiseRes02.isDone() && promiseRes03.isDone() && promiseRes04.isDone()) {
                try {
                    String promise01 = promiseRes01.get();
                    String promise02 = promiseRes02.get();
                    String promise03 = promiseRes03.get();
                    String promise04 = promiseRes04.get();
                    if (StringUtils.isNotEmpty(promise01) && StringUtils.isNotEmpty(promise02) && StringUtils.isNotEmpty(promise03)
                            && StringUtils.isNotEmpty(promise04)) {
                        result.add(promise01);
                        result.add(promise02);
                        result.add(promise03);
                        result.add(promise04);
                    }
                    promiseConti = false;
                    return result;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return result;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    return result;
                }
            }
            if (promiseEnd - promiseStart >= 5000) {
                return result;
            }
        }
        return result;
    }

    /**
     * 请求定缺接口
     */
    class PromiseTask implements Callable<String> {
        JSONObject req;
        String     reqType;

        PromiseTask(JSONObject req, String reqType) {
            this.req = req;
            this.reqType = reqType;
        }

        @Override
        public String call() {
            String url = req.getString(reqType);
            //req.remove(KeySet.PROMISEURL);
            String res = HttpClientHandlerForAI.getInstance().sendRequest(url, JSON.toJSONString(req));
            JSONObject resJson = JSON.parseObject(res);
            System.out.println(req);
            System.out.println(resJson);
            if (resJson.getInteger(KeySet.CODE) == 0) {
                if (StringUtils.equals(reqType, KeySet.PROMISEURL)) {
                    String color = resJson.getString("color");
                    return color;
                } else if (StringUtils.equals(reqType, KeySet.CHANGETILESURL)) {
                    String tiles = resJson.getString("tiles");
                    return tiles;
                }
            }
            return "";
        }
    }


    /**
     * 根据tiles生成请求参数
     * 可按需求换座
     *
     * @param robotList
     * @param getCardJson
     * @return
     */
    private List<JSONObject> robotPromiseInfo(JSONArray robotList, JSONObject getCardJson) {
        List<JSONObject> robotInfo = new ArrayList<>();
        List<String> titles = (List<String>) getCardJson.get("tiles");
        for (int i = 0; i < robotList.size(); i++) {
            JSONObject robot;
            robot = robotList.getJSONObject(i);
            JSONObject robotN = new JSONObject();
            String tile = titles.get(i);
            if (i == getCardJson.getInteger(KeySet.DEALER)) {
                tile = tile.concat(titles.get(4));
            }
            robotN.put(KeySet.TILES, new String(tile));
            robotN.put(KeySet.USERID, Long.valueOf(robot.getLong(KeySet.USERID)));
            robotN.put(KeySet.GAMEID, String.valueOf(getCardJson.getString(KeySet.GAMEID)));
            robotN.put(KeySet.PROMISEURL, String.valueOf(robot.getString(KeySet.PROMISEURL)));
            robotN.put(KeySet.CHANGETILESURL, String.valueOf(robot.getString(KeySet.CHANGETILESURL)));
            robotInfo.add(robotN);
        }
        return robotInfo;
    }

    /**
     * 请求游戏逻辑接口
     */
    class GameLogicTask implements Callable<String> {
        JSONObject req;
        String     url;

        GameLogicTask(JSONObject req, String url) {
            this.req = req;
            this.url = url;
        }

        @Override
        public String call() {
            String res = HttpClientHandlerForAI.getInstance().sendRequest(url, JSON.toJSONString(req));
            System.out.println("req:" + req);
            JSONObject resJson = JSON.parseObject(res);
            if (resJson.getInteger(KeySet.CODE) == 0) {
                List<String> action = (List<String>) resJson.get(KeySet.ACTION);
                System.out.println(res);
                String movement = action.get(0);
                return movement;
            }
            return "";
        }
    }

    /**
     * 校验AIInfo键值
     *
     * @param aiInfo
     * @return
     */
    private boolean checkAIInfo(JSONObject aiInfo) {
        boolean infoFull = true;
        if (!aiInfo.containsKey("initCardUrl"))
            infoFull = false;
        if (!aiInfo.containsKey(KeySet.RULE))
            infoFull = false;
        if (!aiInfo.containsKey(KeySet.DEALER))
            infoFull = false;
        if (!aiInfo.containsKey(KeySet.EXCHANGETYPE))
            infoFull = false;
        if (!aiInfo.containsKey(KeySet.ROBOTINFO)) {
            infoFull = false;
        } else {
            JSONArray robotInfos = aiInfo.getJSONArray(KeySet.ROBOTINFO);
            if (robotInfos.size() > 0) {
                for (int i = 0; i < robotInfos.size(); i++) {
                    JSONObject robotInfo = robotInfos.getJSONObject(i);
                    if (!robotInfo.containsKey(KeySet.AILEVEL))
                        infoFull = false;
                }
            }
        }


        return infoFull;
    }

}
