package com.wzy.bp.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.wzy.bp.dao.DdzTestMapper;
import com.wzy.bp.http.client.HttpClientHandlerForAI;
import com.wzy.bp.service.DdzTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DdzGameTestServiceImpl implements DdzTestService {

    @Autowired
    private DdzTestMapper ddzTestMapper;

    private static char[] str = {'3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A', '2', 'X', 'D'};

    private static class KeySet {
        static final String URL = "url";
        static final String LEVEL = "level";
        static final String RESPONSECODE = "responseCode";
        static final String RESULT = "result";
        static final String CARDS = "cards";//出牌结果
        static final String POOLCARDS = "poolCards";//底牌
        static final String SEATNO = "seatNo";//机器人座位号
        static final String ALLHANDS = "allHands";//手牌
        static final String DOUBLES = "doubles";//加倍信息
        static final String HISTORY = "history";//出牌历史记录
        static final String LORDNO = "lordNo";//地主座位号
        static final String SCORES = "scores";//当前局面 叫分信息
        static final String ASLORDS = "asLords";//叫地主过程
        static final String FIRSTCALLER = "firstCaller";//第一个叫地主或叫分的玩家
        static final String TIME = "time";//机器人思考时间
        static final String BOMBMULTIPLE = "bomb_multiple";
        static final String BETTERSEAT = "better_seat";
        static final String CARDTIDINESS = "card_tidiness";
        static final String CALLLORDFIRST = "call_lord_first";
        static final String BASEGOOD = "base_good";
        static final String PLAYERBOMB = "playerBomb";

    }

    /**
     * 传入参数牌例等级level进行测试
     *
     * @param level
     */
    @Override
    public void testByLevel(String level) {
        List<JSONObject> reqlist = ddzTestMapper.selectReqListByLevel(level);
        testDoAction(reqlist);
    }

    /**
     * 传入参数gameType进行测试
     *
     * @param gameType
     */
    @Override
    public void testByGameType(String gameType) {
        List<JSONObject> reqList = ddzTestMapper.selectReqListByGameType(gameType);
        testDoAction(reqList);
    }


    /**
     * 测试数据库中所有的牌例
     */
    @Override
    public void testAll() {
        List<JSONObject> reqList = ddzTestMapper.selectReqList();
        testDoAction(reqList);
    }

    @Override
    public List testByLevelhaveReturn(String level) {
        List<JSONObject> reqlist = ddzTestMapper.selectReqListByLevel(level);
        List<String> returnList = testDoAction(reqlist);
        return returnList;
    }


    public List testDoAction(List<JSONObject> reqlist) {
        Map<String, Object> resultMap = new HashMap<>();
        List<String> returnList = new ArrayList<>();
        String playUrl = "";
        String urlName = "";
        String result = "";
        String caseId = "";//用例ID
        for (JSONObject reqJson : reqlist) {
            JSONObject req = new JSONObject();
            resultMap = new HashMap<>();
            try {
                //请求接口名称
                if (reqJson.get("urlName") != null) {
                    urlName = reqJson.getString("urlName");
                }
                //用例ID
                if (reqJson.get("case_id") != null) {
                    caseId = reqJson.getString("case_id");
                }
                //预期返回结果
                if (reqJson.getString(KeySet.RESULT) != null) {
                    result = reqJson.getString(KeySet.RESULT);
                }else{
                    result = "";
                }

                //请求URL
                if (reqJson.getString(KeySet.URL) != null) {
                    playUrl = reqJson.getString(KeySet.URL);
                }
                //请求参数json
                JSONObject castJson = JSONObject.parseObject(reqJson.getString("case"));

                if (castJson.containsKey(KeySet.SEATNO)) {
                    int seatNo = castJson.getInteger(KeySet.SEATNO);
                    req.put(KeySet.SEATNO, seatNo);
                }
                if (castJson.containsKey(KeySet.LEVEL)) {
                    int levelTmp = castJson.getInteger(KeySet.LEVEL);
                    req.put(KeySet.LEVEL, levelTmp);
                }
                if (castJson.containsKey(KeySet.TIME)) {
                    int time = castJson.getInteger(KeySet.LEVEL);
                    req.put(KeySet.LEVEL, time);
                }
                if (castJson.containsKey(KeySet.ALLHANDS)) {
                    List<List<Integer>> allHands = new ArrayList<>();
                    allHands = (List<List<Integer>>) castJson.get(KeySet.ALLHANDS);
                    req.put(KeySet.ALLHANDS, allHands);
                }

                if (castJson.containsKey(KeySet.POOLCARDS)) {
                    List<Integer> poolCards = new ArrayList<>();
                    poolCards = (List<Integer>) castJson.get(KeySet.POOLCARDS);
                    req.put(KeySet.POOLCARDS, poolCards);
                }

                if (castJson.containsKey(KeySet.HISTORY)) {
                    List<List<Integer>> history = new ArrayList<>();
                    history = (List<List<Integer>>) castJson.get(KeySet.HISTORY);
                    req.put(KeySet.HISTORY, history);
                }

                if (castJson.containsKey(KeySet.LORDNO)) {
                   int lordNo = castJson.getInteger(KeySet.LORDNO);
                    req.put(KeySet.LORDNO, lordNo);
                }
                if (castJson.containsKey(KeySet.SCORES)) {
                    List<Integer> scores = new ArrayList<>();
                    scores = (List<Integer>) castJson.get(KeySet.SCORES);
                    req.put(KeySet.SCORES, scores);
                }
                if (castJson.containsKey(KeySet.ASLORDS)) {
                    List<Integer> asLords = new ArrayList<>();
                    asLords = (List<Integer>) castJson.get(KeySet.ASLORDS);
                    req.put(KeySet.ASLORDS, asLords);
                }
                if (castJson.containsKey(KeySet.DOUBLES)) {
                    List<Integer> doubles = new ArrayList<>();
                    doubles = (List<Integer>) castJson.get(KeySet.DOUBLES);
                    req.put(KeySet.DOUBLES, doubles);
                }
                if (castJson.containsKey(KeySet.FIRSTCALLER)) {
                   String firstCaller = castJson.getString(KeySet.FIRSTCALLER);
                    req.put(KeySet.FIRSTCALLER, firstCaller);
                }

                if (castJson.containsKey(KeySet.BOMBMULTIPLE)) {
                    req.put(KeySet.BOMBMULTIPLE, castJson.getString(KeySet.BOMBMULTIPLE));
                }
                if (castJson.containsKey(KeySet.BETTERSEAT)) {
                    req.put(KeySet.BETTERSEAT,castJson.getString(KeySet.BETTERSEAT));
                }
                if (castJson.containsKey(KeySet.BASEGOOD)) {
                    req.put(KeySet.BASEGOOD, castJson.getString(KeySet.BASEGOOD));
                }
                if (castJson.containsKey(KeySet.CALLLORDFIRST)) {
                    req.put(KeySet.CALLLORDFIRST,castJson.getString(KeySet.CALLLORDFIRST));
                }
                if (castJson.containsKey(KeySet.CARDTIDINESS)) {
                    req.put(KeySet.CARDTIDINESS,castJson.getString(KeySet.CARDTIDINESS));
                }
                if (castJson.containsKey(KeySet.PLAYERBOMB)) {
                    req.put(KeySet.PLAYERBOMB, castJson.getString(KeySet.PLAYERBOMB));
                }

                String resString = HttpClientHandlerForAI.getInstance().sendRequest(playUrl, JSONObject.toJSONString(req));
                JSONObject resJson = JSONObject.parseObject(resString);
                if ("fapai".equals(urlName)) {
                    if (resJson.getInteger("result") != 1) {
                        resultMap.put("case_id", caseId);
                        resultMap.put("code", resJson.getInteger("result"));
                        resultMap.put("msg", "请求接口错误:" + resJson.getString("message"));
                    } else {
                        resultMap.put("case_id", caseId);
                        resultMap.put("code", "1");
                    }
                } else if ("playCard".equals(urlName)) {
                    if (resJson.getInteger(KeySet.RESPONSECODE) != 1) {
                        resultMap.put("case_id", caseId);
                        resultMap.put("code", resJson.getInteger(KeySet.RESPONSECODE));
                        resultMap.put("msg", "请求接口错误:" + resJson.getString("message"));
                    } else {
                        List<String> cards = castCard((List<Integer>) resJson.get(KeySet.CARDS));
                        resultMap.put("trueResult", JSONObject.toJSONString(cards));
                        String cardsStr = "";
                        if (cards.size() > 0) {
                            for (String s : cards) {
                                cardsStr += s;
                            }
                            char[] cardsChar = cardsStr.toCharArray();
                            Arrays.sort(cardsChar);
                            cardsStr = String.valueOf(cardsChar);
                        } else {
                            cardsStr = "P";
                        }

                        if (result.contains("/")) {
                            String[] resultString = result.split("/");
                            String resultSort = "";
                            List<String> resultList = new ArrayList<>();
                            for (String re : resultString) {
                                char[] resChar = re.toCharArray();
                                Arrays.sort(resChar);
                                resultSort = String.valueOf(resChar);
                                resultList.add(resultSort);
                            }

                            if (resultList.contains(cardsStr)) {
                                resultMap.put("case_id", caseId);
                                resultMap.put("code", "0");

                            } else {
                                resultMap.put("case_id", caseId);
                                resultMap.put("code", "1");
                            }
                        } else {
                            char[] resultChar = result.toCharArray();
                            Arrays.sort(resultChar);
                            result = String.valueOf(resultChar);
                            if (cardsStr.equals(result)) {
                                resultMap.put("case_id", caseId);
                                resultMap.put("code", "0");

                            } else {
                                resultMap.put("case_id", caseId);
                                resultMap.put("code", "1");
                            }
                        }
                    }
                } else {
                    if (resJson.getInteger(KeySet.RESPONSECODE) != 1) {
                        resultMap.put("case_id", "用例Id：" + caseId);
                        resultMap.put("code", resJson.getInteger(KeySet.RESPONSECODE));
                        resultMap.put("msg", "请求接口错误:" + resJson.getString("message"));
                    } else {
                        String returnResult = resJson.getString(KeySet.RESULT);
                        resultMap.put("trueResult", returnResult);
                        if (result.contains("/")) {
                            String[] resultString = result.split("/");
                            List<String> resultList = new ArrayList<>();
                            for (String re : resultString) {
                                resultList.add(re);
                            }
                            if (resultList.contains(returnResult)) {
                                resultMap.put("case_id", caseId);
                                resultMap.put("code", "0");
                            } else {
                                resultMap.put("case_id", caseId);
                                resultMap.put("code", "1");
                            }
                        } else {
                            if (returnResult.equals(result)) {
                                resultMap.put("case_id", caseId);
                                resultMap.put("code", "0");
                            } else {
                                resultMap.put("case_id", caseId);
                                resultMap.put("code", "1");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            System.out.println(JSONObject.toJSONString(resultMap));
            returnList.add(JSONObject.toJSONString(resultMap));
            ddzTestMapper.insertResponseLog(resultMap);
        }
        return returnList;
    }

    //转换明文
    public List<String> castCard(List cardList) {
        int index = -1;
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < cardList.size(); i++) {
            Integer card = (Integer) cardList.get(i);
            if (card.equals(52)) {
                index = 13;
            } else if (card.equals(53)) {
                index = 14;
            } else {
                index = card % 13;
            }
            resultList.add(String.valueOf(str[index]));
        }
        return resultList;
    }
}
