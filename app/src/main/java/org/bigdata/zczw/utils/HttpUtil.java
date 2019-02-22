package org.bigdata.zczw.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HttpUtil {
    private static final String TAG = "HttpUtil";

    public static final int METHOD_GET = 1;
    public static final int METHOD_POST = 2;
    public static final String BASE_URL = "http://....";

    /**
     * 远程访问服务器
     * 
     * @param uri
     * @param params
     *            参数
     * @param method
     *            访问方式get/post
     * @return HttpEntity
     * @throws IOException
     */
    public static HttpEntity getEntity(String uri,
            ArrayList<BasicNameValuePair> params, int method)
            throws IOException {
        HttpEntity entity = null;
        HttpClient client = new DefaultHttpClient();
        HttpUriRequest request = null;
        switch (method) {
        case METHOD_GET:
            StringBuffer sb = null;
            if (uri.indexOf("http")==0) {
                sb = new StringBuffer(uri);
            }else {
                sb = new StringBuffer(BASE_URL + uri);
            }
            if (params != null && !params.isEmpty()) {
                sb.append("?");
                for (BasicNameValuePair param : params) {
                    sb.append(param.getName()).append("=")
                            .append(param.getValue()).append("&");
                }
                sb.deleteCharAt(sb.length() - 1);
            }
            request = new HttpGet(sb.toString());
            break;
        case METHOD_POST:
            if (uri.indexOf("http")==0) {
                request = new HttpPost(uri);
            }else {
                request = new HttpPost(BASE_URL + uri);
            }
            if (params != null && !params.isEmpty()) {
                UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(params,"UTF-8");
                ((HttpPost) request).setEntity(reqEntity);
            }
            break;
        }
        HttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode() == 200) {
            entity = response.getEntity();
        }
        return entity;
    }

    /**
     * 访问服务器，返回IO流
     * 
     * @param uri
     * @param params
     * @param method
     * @return InputStream
     * @throws IOException
     */
    public static InputStream getInputStream(String uri,
            ArrayList<BasicNameValuePair> params, int method)
            throws IOException {
        HttpEntity httpEntity = getEntity(uri, params, method);
        if (httpEntity == null) {
            return null;
        }
        return httpEntity.getContent();
    }

}