package com.wzy.game;

import com.wzy.bp.MainApplication;
import com.wzy.bp.service.ScmjTestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;


@SpringBootTest(classes = MainApplication.class)
@RunWith(SpringRunner.class)
public class ScmjTest {

    @Autowired
    private ScmjTestService scmjTestService;

    public static ScmjTest scmjTest;

    @PostConstruct
    public void init() {
        scmjTest = this;
        scmjTest.scmjTestService= this.scmjTestService;
    }

    @Test
    public void test() {
//        String gameType = "scmj";
//        scmjTestService.testByGameType(gameType);
//        scmjTestService.testAll();
//        for (String msg : returnList) {
//            System.out.println(msg);
//        }

        String level = "4";
        scmjTestService.testByLevel(level);
    }


}

