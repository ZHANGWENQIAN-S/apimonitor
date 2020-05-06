package com.wzy.bp.http.client;

import com.wzy.bp.model.HttpRequest;
import com.wzy.bp.model.HttpRequestLog;
import com.wzy.bp.model.HttpSequence;
import com.wzy.bp.model.HttpSequenceLog;
import com.github.pagehelper.StringUtil;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpSequenceHandle {

    protected static final Logger LOGGER = LoggerFactory.getLogger(HttpClientHandler.class);


    private HttpSequence httpSequence;

    public HashMap<String, String> variables = new HashMap<String, String>();

    public ArrayList<HttpRequestLog> httpRequestLogList = new ArrayList<HttpRequestLog>();

    public HttpSequenceLog httpSequenceLog;

    public String token;

    public HttpSequenceHandle(HttpSequence httpSequence, String token) {
        this.httpSequence = httpSequence;
        this.token = token;
    }


    //cookie保存
    CookieStore cookieStore = new BasicCookieStore();

    public void execute() {
        List<HttpRequest> requests = httpSequence.getHttpRequest();
        for (HttpRequest request : requests) {
            HttpClientHandler handle = new HttpClientHandler(request, this, token);
            HttpRequestLog requestLog = handle.execute();
            requestLog.setPguid(request.getGuid());
            requestLog.setPpguid(httpSequence.getGuid());
            httpRequestLogList.add(requestLog);
        }
        handleLog();

    }

    private void handleLog() {
        httpSequenceLog = new HttpSequenceLog();
        StringBuffer tempLog = new StringBuffer();
        long costTime = 0l;
        for (HttpRequestLog log : httpRequestLogList) {
            costTime += log.getCostTime();
            if (StringUtil.isNotEmpty(log.getLog())) {
                if (tempLog.length() != 0) {
                    tempLog.append("\r\n");
                }
                tempLog.append(log.getLog());
            }
        }
        httpSequenceLog.setPguid(httpSequence.getGuid());
        httpSequenceLog.setCostTime(costTime);
        httpSequenceLog.setLog(tempLog.toString());
        httpSequenceLog.setStatus(StringUtil.isEmpty(tempLog.toString()));
    }


    public void setVariables(String key, String value) {
        variables.put(key, value);
    }

    public String getVariables(String val) {
        for (String variableName : variables.keySet()) {
            if (variableName.equals(val)) {
                return variables.get(variableName);
            }
        }
        return null;
    }


}
