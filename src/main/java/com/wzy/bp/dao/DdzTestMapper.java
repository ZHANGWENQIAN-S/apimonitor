package com.wzy.bp.dao;


import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
@Mapper
public interface DdzTestMapper {
   /**
     * 根据牌例等级进行测试
     * @param level
     * @return
     */
    @Select("SELECT * FROM game_case g  left join game_request r on g.game_request_id = r.id WHERE g.`level` = #{level}")
    public List<JSONObject> selectReqListByLevel(@Param("level") String level);

    /**
     * 根据游戏类型进行测试
     * @param gameType
     * @return
     */
    @Select("SELECT * FROM game_case g  left join game_request r on g.game_request_id = r.id WHERE r.`urlName` = #{gameType} or r.`game` = #{gameType}")
    public List<JSONObject> selectReqListByGameType(@Param("gameType") String gameType);


/*    @Select("SELECT * FROM game_case g  left join game_request r on g.game_request_id = r.id WHERE r.`url` = #{url}")
    public List<JSONObject> selectReqListByUrl(@Param("url") String url);*/

    /**
     * 全查询
     * @return
     */
    @Select("SELECT * FROM game_case g  left join game_request r on g.game_request_id = r.id")
    public List<JSONObject> selectReqList();


    /**
     * 存入测试结果
     * @param resultMap
     */
    @Insert("INSERT INTO game_response_log(`game_case_id`, `response_code`, `trueResult`, `time`) " +
            "VALUES(#{resultMap.case_id},#{resultMap.code},#{resultMap.trueResult},SYSDATE())")
    public void insertResponseLog(@Param("resultMap") Map resultMap);

}
