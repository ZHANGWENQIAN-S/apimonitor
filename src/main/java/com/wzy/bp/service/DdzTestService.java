package com.wzy.bp.service;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface DdzTestService {

    /**
     * 传入参数牌例等级level进行测试
     * @param level
     */
    public void testByLevel(String level);

    /**
     * 传入参数gameType进行测试
     * @param gameType
     */
    public void testByGameType(String gameType);

    /**
     * 测试数据库中所有的牌例
     */
    public void testAll();

    public List testByLevelhaveReturn(String level);
}
