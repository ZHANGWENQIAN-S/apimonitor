package com.wzy.bp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import java.util.List;

@SpringBootTest
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, SqlScriptsTestExecutionListener.class})
@Transactional
@Rollback(false)

public class DdzTestServiceTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private DdzTestService ddzTestService;

    @Test
    public void case1(){
        System.out.println("/测试开始...");
        String level = "1";
//        String req = "{\"base_good\": 1, \"playerBomb\": 3, \"better_seat\": 2, \"bomb_multiple\": 4, \"card_tidiness\": 1, \"call_lord_first\": 0}";
//        String playUrl = "http://120.53.1.58:29031/ddzAI/ddz/cardsdealer?access_token=91caf550-47cd-4604-9451-94fa1ae5c28a";
//        String response = HttpClientHandlerForAI.getInstance().sendRequest(playUrl, JSONObject.toJSONString(req));

        List<String> returnList = ddzTestService.testByLevelhaveReturn(level);
        for (String result : returnList){
            System.out.println(result);
        }
        System.out.println("/测试结束...");

    }
    @Test
    public void case2(){
        System.out.println("case2");
    }


}