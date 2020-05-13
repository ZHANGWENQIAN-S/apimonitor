package com.wzy.gameJunit;

import com.wzy.bp.MainApplication;
import com.wzy.bp.service.DdzTestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;


@SpringBootTest(classes = MainApplication.class)
@RunWith(SpringRunner.class)
public class DdzGameTest {
    @Autowired
    private DdzTestService ddzTestService;

    public static DdzGameTest ddzGameTest;

    @PostConstruct
    public void init() {
        ddzGameTest = this;
        ddzGameTest.ddzTestService = this.ddzTestService;
    }

    @Test
    public void test() {
//        String level = "2";
//        List<String> returnList = ddzTestService.testByLevelRetrun(level);
//        for (String msg : returnList){
//            System.out.println(msg);
//        }
        String gameType = "double";
        ddzTestService.testByGameType(gameType);
//        ddzTestService.testAll();
//        for (String msg : returnList) {
//            System.out.println(msg);
//        }
    }


}
