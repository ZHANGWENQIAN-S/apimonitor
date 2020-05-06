package com.wzy.game;

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
public class SmokeTest {
    @Autowired
    private DdzTestService ddzTestService;

    public static SmokeTest smokeTest;

    @PostConstruct
    public void init() {
        smokeTest = this;
        smokeTest.ddzTestService = this.ddzTestService;
    }

    @Test
    public void testDoAction(){
        String gameType = "initCard";
        ddzTestService.testByGameType(gameType);

    }



}
