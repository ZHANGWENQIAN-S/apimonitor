package com.wzy.bp.dao;

import com.wzy.bp.model.HttpSequence;
import com.wzy.bp.model.VsGame;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
@Mapper
public interface HttpGameMapper {

    @Select("SELECT * FROM vs_game t where t.archived = 0")
    List<Map<String,Object>> selectGameList();

    @Select("select t.* from vs_game t where t.guid=#{guid}")
    VsGame getByGuid(String guid);

    Object selectAvgCostTimeByPguid(String guid);

    Object selectRecentStatusByPguid(String guid);

    @Insert("INSERT INTO vs_game(guid,`name`,`group`,remark,jobName,frequency,AIInfo) " +
            "VALUES(#{vsGame.guid},#{vsGame.name},#{vsGame.group},#{vsGame.remark},#{vsGame.jobName},#{vsGame.frequency},#{vsGame.AIInfo})")
    void insert(@Param("vsGame") VsGame vsGame);

    @Update("UPDATE vs_game t SET t.`name`= #{vsGame.name},t.`group`= #{vsGame.group},t.remark=#{vsGame.remark},t.jobName=#{vsGame.jobName}" +
            ",t.frequency=#{vsGame.frequency},t.AIInfo=#{vsGame.AIInfo} where t.guid= #{vsGame.guid}")
    void update(@Param("vsGame") VsGame vsGame);

    @Update("UPDATE vs_game t SET t.`archived` = 1 WHERE t.`guid`= #{guid} ")
    void archived(@Param("guid")String guid);

    @Update("update vs_game t set t.`enable`=#{enable} where t.guid=#{guid}")
    void updateEnable(@Param("enable") boolean enable,@Param("guid") String guid);

    @Delete("")
    void deleteLog(String guid);
}
