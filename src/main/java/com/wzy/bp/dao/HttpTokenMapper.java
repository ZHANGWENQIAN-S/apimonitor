package com.wzy.bp.dao;

import com.wzy.bp.model.HttpToken;
import org.apache.ibatis.annotations.*;

@Mapper
public interface HttpTokenMapper {

	@Insert("INSERT INTO http_token(token,refresh,expire)  VALUES("
			+"#{httpToken.access_token},#{httpToken.refresh_token},#{httpToken.expires_in}")
	void insert(@Param("httpToken") HttpToken httpToken);

	@Update("update http_token t set t.`token`=#{httpToken.access_token},t.refresh=#{httpToken.refresh_token}" +
			",t.expired=#{httpToken.expires_in} where t.id=1")
	void update(@Param("httpToken") HttpToken httpToken);

	@Select("SELECT token FROM http_token where id = 1")
	String findToken();
}
