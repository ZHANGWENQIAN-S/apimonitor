package com.wzy.bp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wzy.bp.dao.DdzTestMapper;
import com.wzy.bp.http.client.HttpClientHandlerForAI;
import com.wzy.bp.service.ScmjTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScmjTestServiceImpl implements ScmjTestService {

    @Autowired
    private DdzTestMapper ddzTestMapper;

    private static class KeySet {
        static final String EXCHANGETYPE = "exchangeType";//换牌方向
        static final String DEALER = "dealer";//庄家位置
        static final String GRADE = "grade";//控制发牌级别
        static final String CURPOS = "curPos";//当前需要摸牌玩家的位置
        static final String WALL = "wall";//牌墙的剩余牌
        static final String ACTION = "action";
        static final String USERINFO = "userInfo";
        static final String LEGALACT = "legalAct";
        static final String HISTORY = "history";
        static final String AILEVEL = "AILevel";
        static final String USERID = "userId";
        static final String TILES = "tiles";
        static final String GAMEID = "gameId";
        static final String CODE = "code";
        static final String RULE = "rule";
        static final String RESULT = "result";
        static final String URL = "url";
        static final String URLNAME = "urlName";
        static final String CASEID = "case_id";
        static final String CASE = "case";
        static final String COLOR = "color";

    }


    @Override
    public void testByLevel(String level) {
        List<JSONObject> req = ddzTestMapper.selectReqListByLevel(level);
        testDoAction(req);
    }

    @Override
    public void testByGameType(String gameType) {
        List<JSONObject> req = ddzTestMapper.selectReqListByGameType(gameType);
        testDoAction(req);
    }

    @Override
    public void testAll() {
        List<JSONObject> req = ddzTestMapper.selectReqList();
        testDoAction(req);
    }


    public void testDoAction(List<JSONObject> reqList) {
        Map<String, Object> resultMap = new HashMap<>();
        JSONObject req = new JSONObject();
        String urlName = "";
        String caseId = "";
        String result = "";
        String playUrl = "";

        for (JSONObject reqJson : reqList) {
            //请求接口名称
            if (reqJson.get(KeySet.URLNAME) != null) {
                urlName = reqJson.getString(KeySet.URLNAME);
            }
            //用例ID
            if (reqJson.get(KeySet.CASEID) != null) {
                caseId = reqJson.getString(KeySet.CASEID);
            }
            //预期返回结果
            if (reqJson.getString(ScmjTestServiceImpl.KeySet.RESULT) != null) {
                result = reqJson.getString(ScmjTestServiceImpl.KeySet.RESULT);
            }
            //请求URL
            if (reqJson.getString(ScmjTestServiceImpl.KeySet.URL) != null) {
                playUrl = reqJson.getString(ScmjTestServiceImpl.KeySet.URL);
            }
            //请求参数json
            JSONObject castJson = JSONObject.parseObject(reqJson.getString(KeySet.CASE));
            if (castJson.containsKey(KeySet.GAMEID)) {
                req.put(KeySet.GAMEID, castJson.getString(KeySet.GAMEID));
            }
            if (castJson.containsKey(KeySet.EXCHANGETYPE)) {
                req.put(KeySet.EXCHANGETYPE, castJson.getInteger(KeySet.EXCHANGETYPE));
            }
            if (castJson.containsKey(KeySet.DEALER)) {
                req.put(KeySet.DEALER, castJson.getInteger(KeySet.DEALER));
            }
            if (castJson.containsKey(KeySet.GRADE)) {
                req.put(KeySet.GRADE, castJson.get(KeySet.GRADE));
            }
            if (castJson.containsKey(KeySet.USERID)) {
                req.put(KeySet.USERID, castJson.getLong(KeySet.USERID));
            }
            if (castJson.containsKey(KeySet.AILEVEL)) {
                req.put(KeySet.AILEVEL, castJson.getInteger(KeySet.AILEVEL));
            }
            if (castJson.containsKey(KeySet.WALL)) {
                req.put(KeySet.WALL, castJson.getString(KeySet.WALL));
            }
            if (castJson.containsKey(KeySet.CURPOS)) {
                req.put(KeySet.CURPOS, castJson.getInteger(KeySet.CURPOS));
            }
            if (castJson.containsKey(KeySet.HISTORY)) {
                req.put(KeySet.HISTORY, castJson.get(KeySet.HISTORY));
            }
            if (castJson.containsKey(KeySet.RULE)) {
                req.put(KeySet.RULE, castJson.get(KeySet.RULE));
            }
            if (castJson.containsKey(KeySet.USERINFO)) {
                req.put(KeySet.USERINFO, castJson.get(KeySet.USERINFO));
            }
            if (castJson.containsKey(KeySet.LEGALACT)) {
                req.put(KeySet.LEGALACT, castJson.get(KeySet.LEGALACT));
            }
            if (castJson.containsKey(KeySet.TILES)) {
                req.put(KeySet.TILES, castJson.getString(KeySet.TILES));
            }
            String res = HttpClientHandlerForAI.getInstance().sendRequest(playUrl, JSONObject.toJSONString(req));
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.getInteger(KeySet.CODE) != 0) {
                resultMap.put(KeySet.CODE, resJson.getInteger(KeySet.CODE));
                resultMap.put(KeySet.CASEID, caseId);
            } else {
                if ("play".equals(urlName)) {
                    List<String> action = (List<String>) resJson.get(KeySet.ACTION);
                    String type = action.get(0);
                    String card = action.get(1);
                    String resAction = String.valueOf(action);
                    resultMap.put("trueResult", resAction);
                    if (!result.contains("[")) {
                        if (result.contains("/")) {
                            String[] resultString = result.split("/");
                            List<String> resultList = new ArrayList<>();
                            for (String re : resultString) {
                                resultList.add(re);
                            }
                            if (resultList.contains(card)) {
                                resultMap.put(KeySet.CODE, 0);
                                resultMap.put(KeySet.CASEID, caseId);
                            } else {
                                resultMap.put(KeySet.CODE, 1);
                                resultMap.put(KeySet.CASEID, caseId);
                            }

                        } else if (result.equals("PE")) {
                            if (type.equals("14")) {
                                resultMap.put(KeySet.CODE, 0);
                                resultMap.put(KeySet.CASEID, caseId);
                            } else {
                                resultMap.put(KeySet.CODE, 1);
                                resultMap.put(KeySet.CASEID, caseId);
                            }

                        } else if (result.equals("PA")) {
                            if (type.equals("18")) {
                                resultMap.put(KeySet.CODE, 0);
                                resultMap.put(KeySet.CASEID, caseId);
                            } else {
                                resultMap.put(KeySet.CODE, 1);
                                resultMap.put(KeySet.CASEID, caseId);
                            }

                        }else if (result.equals("HU")) {
                            if (type.equals("8")||type.equals("7")||type.equals("21")) {
                                resultMap.put(KeySet.CODE, 0);
                                resultMap.put(KeySet.CASEID, caseId);
                            } else {
                                resultMap.put(KeySet.CODE, 1);
                                resultMap.put(KeySet.CASEID, caseId);
                            }
                        } else if (result.equals("GA")) {
                            if (type.equals("3")||type.equals("4")||type.equals("5")) {
                                resultMap.put(KeySet.CODE, 0);
                                resultMap.put(KeySet.CASEID, caseId);
                            } else {
                                resultMap.put(KeySet.CODE, 1);
                                resultMap.put(KeySet.CASEID, caseId);
                            }
                        } else {
                            if (action.contains(result)) {
                                resultMap.put(KeySet.CODE, 0);
                                resultMap.put(KeySet.CASEID, caseId);
                            } else {
                                resultMap.put(KeySet.CODE, 1);
                                resultMap.put(KeySet.CASEID, caseId);
                            }
                        }
                    } else {
                        if (resAction.equals(result)) {
                            resultMap.put(KeySet.CODE, 0);
                            resultMap.put(KeySet.CASEID, caseId);
                        } else {
                            resultMap.put(KeySet.CODE, 1);
                            resultMap.put(KeySet.CASEID, caseId);
                        }
                    }
                } else if ("que".equals(urlName)) {
                    String color = resJson.getString(KeySet.COLOR);
                    resultMap.put("trueResult", color);
                    if (color.equals(result)) {
                        resultMap.put(KeySet.CODE, 0);
                        resultMap.put(KeySet.CASEID, caseId);
                    } else {
                        resultMap.put(KeySet.CODE, 1);
                        resultMap.put(KeySet.CASEID, caseId);
                    }
                } else if ("change".equals(urlName)) {
                    String tiles = resJson.getString(KeySet.TILES);
                    resultMap.put("trueResult", tiles);
                    if (tiles.equals(result)) {
                        resultMap.put(KeySet.CODE, 0);
                        resultMap.put(KeySet.CASEID, caseId);
                    } else {
                        resultMap.put(KeySet.CODE, 1);
                        resultMap.put(KeySet.CASEID, caseId);
                    }
                } else if ("nextCard".equals(urlName)) {
                    String tile = resJson.getString("tile");
                    resultMap.put("trueResult", tile);
                    if (tile.equals(result)) {
                        resultMap.put(KeySet.CODE, 0);
                        resultMap.put(KeySet.CASEID, caseId);
                    } else {
                        resultMap.put(KeySet.CODE, 1);
                        resultMap.put(KeySet.CASEID, caseId);
                    }
                }
            }

            ddzTestMapper.insertResponseLog(resultMap);
        }
    }
}
