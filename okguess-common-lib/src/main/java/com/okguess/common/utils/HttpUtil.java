package com.okguess.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class HttpUtil {

    private static PoolingHttpClientConnectionManager conManager = new PoolingHttpClientConnectionManager();

    private static CloseableHttpClient httpClient;

    private static final String ENCODING = "UTF-8";

    static {
        conManager.setMaxTotal(500);
        conManager.setDefaultMaxPerRoute(500);

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).setConnectionRequestTimeout(3000).build();

        httpClient = HttpClients.custom().setConnectionManager(conManager).setDefaultRequestConfig(requestConfig).setRetryHandler(new
                DefaultHttpRequestRetryHandler(3, false)).build();
    }

    private HttpUtil() {
    }


    public static final String postWithPara(String url, Map<String, String> params) throws Exception {
        return postWithUrlEntityParaAndHeader(url, params, null);
    }

    public static final String postWithPara(String url) throws Exception {
        return postWithUrlEntityParaAndHeader(url, null, null);
    }

    public static final String postWithHeader(String url, Map<String, String> headers) throws Exception {
        return postWithStringEntityParaAndHeader(url, null, headers);
    }


    public static final String postWithPara(String url, StringEntity paraEntity) throws Exception {
        return postWithStringEntityParaAndHeader(url, paraEntity, null);
    }

    public static final String postWithStringEntityParaAndHeader(String url, StringEntity paraEntity, Map<String, String> headers) throws Exception {

        HttpPost httpPost = new HttpPost(url);

        if (paraEntity != null) {
            httpPost.setEntity(paraEntity);
        }

        return executeRequest(httpPost, headers);
    }

    public static final String postWithUrlEntityParaAndHeader(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
        HttpPost httpPost = new HttpPost(url);

        if (params != null && !params.isEmpty()) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (String name : params.keySet()) {
                String value = params.get(name);
                nvps.add(new BasicNameValuePair(name, value));
            }

            httpPost.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));
        }

        return executeRequest(httpPost, headers);
    }

    public static final String get(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);

        return executeRequest(httpGet, null);
    }

    private static String executeRequest(HttpRequestBase request, Map<String, String> headers) throws Exception {
        HttpEntity entity = null;
        try {
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    request.addHeader(header.getKey(), header.getValue());
                }
            }
            HttpResponse response = httpClient.execute(request);
            entity = response.getEntity();
            return EntityUtils.toString(entity, ENCODING);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (entity != null) {
                EntityUtils.consume(entity);
            }
            request.abort();
        }
    }
}
