package com.jkm.base.common.spring.http.client.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.jkm.base.common.spring.http.client.factory.HttpClientAbstractFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by hutao on 15/6/23.
 * 下午4:02
 */
@Slf4j
public class HttpClient implements Closeable{
    /**
     * apache http client
     */
    private final CloseableHttpClient closeableHttpClient;

    /**
     * CONSTRUCTOR
     */
    public HttpClient(final HttpClientAbstractFactory httpClientAbstractFactory) {
        this.closeableHttpClient = httpClientAbstractFactory.createHttpClient();
    }

    /**
     * 发送post报文
     *
     * @param uri     URI
     * @param content 内容
     * @return 返回报文
     */
    public String post(final String uri, final String content) {
        final HttpPost postRequest = new HttpPost(uri);
        final ByteArrayEntity entity = new ByteArrayEntity(content.getBytes());
        entity.setContentEncoding("UTF-8");
        postRequest.setEntity(entity);
        try (final CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(postRequest)) {
            return EntityUtils.toString(getResponseEntity(closeableHttpResponse));
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        throw new RuntimeException("should not be there");
    }

    /**
     * application/json post
     *
     * @param uri        URI
     * @param paramMap 内容
     * @return 返回报文
     */
    public String jsonPost(final String uri,
                       final Map<String, String> paramMap) {
        final RequestBuilder requestBuilder = RequestBuilder.post()
                .setUri(uri)
                .addHeader(new BasicHeader(HTTP.CONTENT_ENCODING, "UTF-8"));
        requestBuilder.addHeader(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        requestBuilder.setEntity(new StringEntity(JSONObject.toJSONString(paramMap),
                Charset.forName("UTF-8")));
        try (final CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(requestBuilder.build())) {
            return EntityUtils.toString(getResponseEntity(closeableHttpResponse));
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        throw new RuntimeException("should not be there");
    }

    /**
     * form 表单 post
     *
     * @param uri
     * @param paramMap
     * @return
     */
    public String formPost(final String uri,
                           final Map<String, String> paramMap) {

        final RequestBuilder requestBuilder = RequestBuilder.post()
                .setUri(uri)
                .addHeader(new BasicHeader(HTTP.CONTENT_ENCODING, "UTF-8"));
        for (final Map.Entry<String, String> entry : paramMap.entrySet()) {
            requestBuilder.addParameter(entry.getKey(), entry.getValue());
        }
        requestBuilder.setEntity(new UrlEncodedFormEntity(requestBuilder.getParameters(),
                Charset.forName("UTF-8")));
        try (final CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(requestBuilder.build())) {
            return EntityUtils.toString(getResponseEntity(closeableHttpResponse));
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        throw new RuntimeException("should not be there");
    }





    private HttpEntity getResponseEntity(final CloseableHttpResponse closeableHttpResponse) {
        final int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new RuntimeException("http request error:" + statusCode);
        }
        return closeableHttpResponse.getEntity();
    }

    /**
     * 发送get请求
     *
     * @param uri URI
     * @return 返回报文
     */
    public String get(final String uri) {
        final HttpGet getRequest = new HttpGet(uri);
        try (final CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(getRequest)) {
            return EntityUtils.toString(getResponseEntity(closeableHttpResponse));
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        throw new RuntimeException("should not be there");
    }



    @Override
    public void close() {
        try {
            closeableHttpClient.close();
        } catch (final IOException e) {
            log.error("shutdown closeable httpClient error", e);
        }
    }
}

