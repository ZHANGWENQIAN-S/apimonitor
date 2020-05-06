package com.wzy.bp.service.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.wzy.bp.dao.HttpTokenMapper;
import com.wzy.bp.model.HttpToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wzy.bp.dao.HttpRequestLogMapper;
import com.wzy.bp.dao.HttpRequestMapper;
import com.wzy.bp.http.client.HttpSequenceHandle;
import com.wzy.bp.model.HttpRequest;
import com.wzy.bp.model.HttpRequestLog;
import com.wzy.bp.model.HttpSequence;
import com.wzy.bp.quartz.DynamicJobManager;
import com.wzy.bp.service.HttpRequestService;
import com.wzy.bp.service.HttpSequenceService;
import com.wzy.bp.util.GuidGenerator;
import com.github.pagehelper.StringUtil;

@Service
public class HttpRequestServiceImpl implements HttpRequestService {



	@Autowired
	private HttpRequestMapper httpRequestMapper;
	

	@Autowired
	private HttpSequenceService httpSequenceService;

	@Autowired
	private HttpRequestLogMapper httpRequestLogMapper;

	@Autowired
	private HttpTokenMapper httpTokenMapper;

	@Value("${tokenId}")
	private  String tokenId = "f42ed4f975c04ae98795ccef95155a02";
	
    @Transactional
    @Override
    public boolean enableMonitor(String guid) {
    	HttpSequence instance = httpSequenceService.getByGuid(guid);
    	DynamicJobManager dynamicJobManager = new DynamicJobManager(instance);
        return dynamicJobManager.enable();
    }

    @Transactional
    @Override
    public boolean disableMonitor(String guid) {
    	HttpSequence instance = httpSequenceService.getByGuid(guid);
    	DynamicJobManager dynamicJobManager = new DynamicJobManager(instance);
        return dynamicJobManager.kill();
    }
    

    @Transactional
    @Override
    public boolean deleteMonitor(String guid) {
    	HttpSequence instance = httpSequenceService.getByGuid(guid);
    	DynamicJobManager dynamicJobManager = new DynamicJobManager(instance);
        return dynamicJobManager.delete();
    }
    
    
    
    @Transactional
    @Override
    public void executeRequest(String guid) {
    	HttpSequence instance = httpSequenceService.getByGuid(guid);
    	if(instance==null){
    		//删除任务
    		return;
    	}
    	String token = httpTokenMapper.findToken();
    	List<HttpRequest> list = this.getHttpRequestListByPguid(guid);
    	instance.setHttpRequest(list);
    	
    	//执行接口探测
    	HttpSequenceHandle httpSequenceHandle = new HttpSequenceHandle(instance,token);
    	httpSequenceHandle.execute();
    	httpSequenceService.insertLog(httpSequenceHandle.httpSequenceLog);
    	
    	//保存日志
    	for(HttpRequestLog httpRequestLog : httpSequenceHandle.httpRequestLogList){
    		httpRequestLog.setPid(httpSequenceHandle.httpSequenceLog.getId());
    		if (StringUtils.equals(httpRequestLog.getStatusCode(),"200")&&StringUtils.equals(httpRequestLog.getPpguid(),tokenId)){
				HttpToken httpToken = JSONObject.parseObject(httpRequestLog.getResponseBody(),HttpToken.class);
				httpTokenMapper.update(httpToken);
			}
    		httpRequestLogMapper.insert(httpRequestLog);

    	}
    }
    
    

    @Transactional
    @Override
	public void archivedHttpData(String guid){
    	httpSequenceService.archived(guid);
		httpRequestMapper.archived(guid);
	}
    

    @Transactional
    @Override
	public void deleteHttpLog(String guid){
    	httpSequenceService.deleteLog(guid);
		httpRequestLogMapper.delete(guid);
	}
    

    @Transactional
    @Override
    public void cleanMonitorLogs(int day) {
		httpRequestLogMapper.cleanLogByDay(day);
		httpSequenceService.cleanLog(day);
    }
    
    
	@Override
	public void updateEnabled(HttpSequence httpSequence){
		httpSequenceService.updateEnabled(httpSequence);
	}
	

	
	
	@Override
	public List<HttpRequest> getAllHttpRequest(){
		return httpRequestMapper.selectAll();
	}
	
	@Override
	public HttpRequest getHttpRequestByGuid(String guid){
		return httpRequestMapper.getByGuid(guid);
	}
	

	@Override
	public List<HttpRequest> getHttpRequestListByPguid(String pguid){
		return httpRequestMapper.getListByPguid(pguid);
	}
	

	@Override
	public void insertHttpRequest(HttpRequest httpRequest){
		if(StringUtil.isEmpty(httpRequest.getGuid())){
			httpRequest.setGuid(GuidGenerator.generate());
		}
		httpRequestMapper.insert(httpRequest);
	}

	@Override
	public void updateHttpRequest(HttpRequest httpRequest){
		httpRequestMapper.update(httpRequest);
	}

	
	@Override
	public List<Map<String, Object>> getHttpRequestLogByPid(String id){
		List<Map<String, Object>> list = httpRequestLogMapper.getByPid(Integer.parseInt(id));
		return list;
		
	}
}
