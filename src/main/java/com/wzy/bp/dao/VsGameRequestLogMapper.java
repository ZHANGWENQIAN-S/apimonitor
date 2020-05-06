package com.wzy.bp.dao;

import com.wzy.bp.model.VsGameHistory;
import com.wzy.bp.model.VsGameRequestHistory;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface VsGameRequestLogMapper {

    @Insert("INSERT INTO vs_game_request_log(URI,`status`,`costTime`,log,`createTime`,gameId"
            + ")  VALUES("
            + "#{vsGameHistory.URI},#{vsGameHistory.status},#{vsGameHistory.costTime},#{vsGameHistory.log}," +
            "#{vsGameHistory.createTime},#{vsGameHistory.gameId})")
    @Options(useGeneratedKeys = true, keyProperty = "vsGameHistory.id", keyColumn = "id")
    void insert(@Param("vsGameHistory") VsGameRequestHistory vsGameHistory);

    @Select("select ROUND(AVG(t.costTime)) as costTime from vs_game_request_log t where t.guid = #{guid}")
    Object selectAvgCostTimeByGuid(@Param("guid") String URI);

    @Select("select t.`status` from vs_game_log t where t.guid = #{guid} order by t.createTime desc limit 1")
    Object selectRecentStatusByGuid(@Param("guid") String URI);

    @Select("select t.`status`,count(t.`status`) as count from vs_game_log t where t.guid = #{guid} group by t.`status`")
    List<Map<String, Object>> selectUsabilityByGuid(@Param("guid") String URI);

    @Select("select t.* from vs_game_log t where t.guid = #{guid}")
    List<Map<String, Object>> getLogByGuid(@Param("guid") String guid);

    @Select("select t.* from vs_game_log t where t.id = #{id} limit 1")
    Map<String, Object> getLogById(@Param("id") Long id);
}
