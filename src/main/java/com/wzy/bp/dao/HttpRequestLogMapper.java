package com.wzy.bp.dao;

import java.util.List;
import java.util.Map;

import com.wzy.bp.model.HttpRequestLog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HttpRequestLogMapper {

	@Insert("INSERT INTO http_request_log(pid,ppguid,pguid,`status`,`costTime`,`statusCode`,responseBody,log"
			+ ")  VALUES("
			+"#{httpRequestLog.pid},#{httpRequestLog.ppguid},#{httpRequestLog.pguid},#{httpRequestLog.status},#{httpRequestLog.costTime},"
			+ "#{httpRequestLog.statusCode},#{httpRequestLog.responseBody},#{httpRequestLog.log})")
	void insert(@Param("httpRequestLog") HttpRequestLog httpRequestLog);
	
	@Select("SELECT * FROM http_request_log")
	List<HttpRequestLog> selectAll();

	@Select("select t.* from http_request_log t where t.pguid=#{pguid}")
	HttpRequestLog getByGuid(@Param("pguid")String pguid);

	@Delete("delete from http_request_log where ppguid=#{ppguid}")
	void delete(@Param("ppguid")String ppguid);
	
	@Select("select r.httpMethod,r.url,t.`status`,t.statusCode,t.costTime,t.log from http_request_log t,http_request r where t.pguid=r.guid and t.pid=#{id}")
	List<Map<String,Object>> getByPid(@Param("id")int id);
	

	@Delete("delete from http_request_log where createTime < date_sub(curdate(),interval #{day} day)")
	void cleanLogByDay(@Param("day")int day);
}
