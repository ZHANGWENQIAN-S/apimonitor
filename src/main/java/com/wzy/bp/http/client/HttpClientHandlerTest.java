package com.wzy.bp.http.client;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;

public class HttpClientHandlerTest {

    public static void main(String[] args) {
        String url = "http://154.223.71.76:29030/uaa/oauth/token";

        RequestBuilder builder = RequestBuilder.post();

        HashMap<String, String> map = new HashMap<>();
        map.put("grant_type", "password");
        map.put("username", "admin");
        map.put("password", "123456");

        /**refreshtoken使用方法         */
//        map.put("grant_type","refresh_token");
//        map.put("token",token);
        for (String key : map.keySet()) {
            String val = map.get(key);
            builder.addParameter(new BasicNameValuePair(key, val));
        }
        HttpUriRequest request = builder.setUri(url).build();

        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        CloseableHttpClient client = createHttpClient();
        try {
            CloseableHttpResponse response = client.execute(request);
            String statusCode = String.valueOf(getStatusCode(response));
            String body = getResponseBody(response);
            System.out.println(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected static CloseableHttpClient createHttpClient() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        org.apache.http.auth.Credentials credentials = new org.apache.http.auth.UsernamePasswordCredentials("webApp", "123456");
        credsProvider.setCredentials(org.apache.http.auth.AuthScope.ANY, credentials);
        httpClientBuilder.setDefaultCredentialsProvider(credsProvider);
        return httpClientBuilder.build();
    }

    protected static String getResponseBody(CloseableHttpResponse httpResponse) throws ParseException, IOException {
        if (httpResponse == null) return null;
        HttpEntity entity = httpResponse.getEntity();
        if (entity == null) return null;
        String webPage = EntityUtils.toString(entity, "UTF-8");
        return webPage;
    }

    protected static int getStatusCode(CloseableHttpResponse httpResponse) {
        int status = httpResponse.getStatusLine().getStatusCode();
        return status;
    }
}