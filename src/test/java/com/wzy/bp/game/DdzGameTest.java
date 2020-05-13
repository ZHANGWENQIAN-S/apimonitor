package com.wzy.bp.game;

import com.alibaba.fastjson.JSONObject;
import com.wzy.bp.http.client.HttpClientHandlerForAI;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, SqlScriptsTestExecutionListener.class})
@Transactional
@Rollback(false)
public class DdzGameTest {
    @Test
    @Parameters({ "ip","token" })
    public void initCard(String ip,String token) {
        JSONObject req = new JSONObject();
        String initCardUrl = "http://"+ip+"/ddzAI/ddz/cardsdealer"+token;
//        String initCardUrl = "http://154.223.71.76:29031/ddzAI/ddz/cardsdealer";
        req.put("base_good", 1);
        req.put("playerBomb", 1);
        req.put("better_seat", 2);
        req.put("bomb_multiple", 4);
        req.put("card_tidiness", 1);
        req.put("call_lord_first", 0);
        String i = HttpClientHandlerForAI.getInstance().sendRequest(initCardUrl, JSONObject.toJSONString(req));
        System.out.println("控牌接口返回结果:"+i);
    }


    @Test
    @Parameters({ "ip","token" })
    public void ddzGame(String ip,String token) {
        JSONObject req = new JSONObject();
        req.put("initPosition", 0);
        req.put("level", 2);
        req.put("initCardUrl", "");
        List<List<Integer>> initCard = new ArrayList<>();
        String card1 = "21,8,47,34,35,22,48,26,20,33,44,4,19,30,17,40,2";
        String card2 = "7,10,6,14,5,23,27,13,43,28,15,31,9,36,1,41,45";
        String card3 = "39,0,16,42,3,29,46,49,50,11,37,24,38,51,25,52,53";
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
        req.put("initCard", initCard);
        List<Integer> poolCards = new ArrayList<>();
        poolCards.add(32);
        poolCards.add(18);
        poolCards.add(12);
        JSONObject robotInfo1 = new JSONObject();
        JSONObject robotInfo2 = new JSONObject();
        JSONObject robotInfo3 = new JSONObject();
        robotInfo1.put("callUrl", "http://"+ip+"/ddzAI/ddz/bemaster"+token);
        robotInfo1.put("doubleUrl", "http://"+ip+"/ddzAI/ddz/doubleScore"+token);
        robotInfo1.put("playUrl", "http://"+ip+"/ddzAI/ddz/playcard"+token);
        robotInfo1.put("reCall", "http://"+ip+"/ddzAI/ddz/remaster"+token);
        robotInfo2.put("callUrl",  "http://"+ip+"/ddzAI/ddz/bemaster"+token);
        robotInfo2.put("doubleUrl","http://"+ip+"/ddzAI/ddz/doubleScore"+token);
        robotInfo2.put("playUrl", "http://"+ip+"/ddzAI/ddz/playcard"+token);
        robotInfo2.put("reCall", "http://"+ip+"/ddzAI/ddz/remaster"+token);
        robotInfo3.put("callUrl", "http://"+ip+"/ddzAI/ddz/bemaster"+token);
        robotInfo3.put("doubleUrl", "http://"+ip+"/ddzAI/ddz/doubleScore"+token);
        robotInfo3.put("playUrl", "http://"+ip+"/ddzAI/ddz/playcard"+token);
        robotInfo3.put("reCall", "http://"+ip+"/ddzAI/ddz/remaster"+token);
//        robotInfo1.put("callUrl", "http://154.223.71.76:29031/ddzAI/ddz/bemaster");
//        robotInfo1.put("doubleUrl", "http://154.223.71.76:29031/ddzAI/ddz/doubleScore");
//        robotInfo1.put("playUrl", "http://154.223.71.76:29031/ddzAI/ddz/playcard");
//        robotInfo1.put("reCall", "http://154.223.71.76:29031/ddzAI/ddz/remaster");
//        robotInfo2.put("callUrl", "http://154.223.71.76:29031/ddzAI/ddz/bemaster");
//        robotInfo2.put("doubleUrl", "http://154.223.71.76:29031/ddzAI/ddz/doubleScore");
//        robotInfo2.put("playUrl", "http://154.223.71.76:29031/ddzAI/ddz/playcard");
//        robotInfo2.put("reCall",  "http://154.223.71.76:29031/ddzAI/ddz/remaster");
//        robotInfo3.put("callUrl", "http://154.223.71.76:29031/ddzAI/ddz/bemaster");
//        robotInfo3.put("doubleUrl", "http://154.223.71.76:29031/ddzAI/ddz/doubleScore");
//        robotInfo3.put("playUrl", "http://154.223.71.76:29031/ddzAI/ddz/playcard");
//        robotInfo3.put("reCall", "http://154.223.71.76:29031/ddzAI/ddz/remaster");
        List<JSONObject> robotInfo = new ArrayList<>();
        robotInfo.add(robotInfo1);
        robotInfo.add(robotInfo2);
        robotInfo.add(robotInfo3);
        req.put("robotInfo", robotInfo);
        req.put("poolCards", poolCards);
        req.put("initCardUrl", "NOURL");
        System.out.println(req);
        String i = DdzGame.getInstance().doAction(req);
        System.out.println("AI打牌结果:"+i);
    }

}