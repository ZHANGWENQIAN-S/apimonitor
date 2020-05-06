package com.wzy.bp.http.client;

import com.alibaba.fastjson.JSONObject;
import com.wzy.bp.model.VsGameRequestHistory;
import com.wzy.bp.service.HttpGameService;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.mortbay.util.ajax.JSON;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class HttpClientHandlerForAI {


    private static HttpClientHandlerForAI instance = new HttpClientHandlerForAI();

    private HttpClientHandlerForAI() {
    }

    public static HttpClientHandlerForAI getInstance() {
        return instance;
    }

    public String sendRequest(String url, String req) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(addRequestParams(req));
        CloseableHttpClient client = createHttpClient();
        long start = System.currentTimeMillis();

        try {
            CloseableHttpResponse response = client.execute(httpPost);
            String res = getResponseBody(response);
            JSONObject resJson = JSONObject.parseObject(res);
            long end = System.currentTimeMillis();
            resJson.put("time", end - start);
            return resJson.toJSONString();

        } catch (IOException e) {
            e.printStackTrace();
            JSONObject resError = new JSONObject();
            resError.put("code", 203);
            resError.put("msg", "请求失败");
            resError.put("error", e);
            return JSON.toString(resError);
        }
    }


    protected String getResponseBody(CloseableHttpResponse httpResponse) throws ParseException, IOException {
        if (httpResponse == null) return null;
        HttpEntity entity = httpResponse.getEntity();
        if (entity == null) return null;
        String webPage = EntityUtils.toString(entity, "UTF-8");
        return webPage;
    }

    protected CloseableHttpClient createHttpClient() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        org.apache.http.auth.Credentials credentials = new org.apache.http.auth.UsernamePasswordCredentials("admin", "123456");
        credsProvider.setCredentials(org.apache.http.auth.AuthScope.ANY, credentials);
        httpClientBuilder.setDefaultCredentialsProvider(credsProvider);
        return httpClientBuilder.build();
    }

    protected StringEntity addRequestParams(String req) {
        StringEntity entity = new StringEntity(req, ContentType.APPLICATION_JSON);
        return entity;
    }


}