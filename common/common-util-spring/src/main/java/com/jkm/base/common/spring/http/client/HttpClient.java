package com.jkm.base.common.spring.http.client;

import com.google.common.base.Throwables;
import com.jkm.base.common.spring.http.client.factory.HttpClientAbstractFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by hutao on 15/6/23.
 * 下午4:02
 */
@Slf4j
public class HttpClient implements Closeable {
    /**
     * apache http client辅助抽象工厂
     */
    private final HttpClientAbstractFactory httpClientAbstractFactory;
    /**
     * apache http client
     */
    private final CloseableHttpClient closeableHttpClient;

    /**
     * CONSTRUCTOR
     */
    public HttpClient(final HttpClientAbstractFactory httpClientAbstractFactory) {
        this.httpClientAbstractFactory = httpClientAbstractFactory;
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
        postRequest.setConfig(httpClientAbstractFactory.createRequestConfig());
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
     * 发送post报文
     *
     * @param uri        URI
     * @param parameters 内容
     * @return 返回报文
     */
    public String post(final String uri, final List<Pair<String, String>> parameters) {
        return post(uri, parameters, "UTF-8");
    }

    /**
     * 发送post报文
     *
     * @param uri        URI
     * @param parameters 内容
     * @return 返回报文
     */
    public String post(final String uri,
                       final List<Pair<String, String>> parameters,
                       final String charset) {
        final HttpPost postRequest = new HttpPost(uri);
        postRequest.setConfig(httpClientAbstractFactory.createRequestConfig());
        final RequestBuilder requestBuilder = RequestBuilder.post()
                .setUri(uri)
                .setConfig(httpClientAbstractFactory.createRequestConfig())
                .setHeader(new BasicHeader(HTTP.CONTENT_ENCODING, charset));
        for (final Pair<String, String> parameter : parameters) {
            requestBuilder.addParameter(parameter.getKey(), parameter.getValue());
        }
        try (final CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(requestBuilder.build())) {
            return EntityUtils.toString(getResponseEntity(closeableHttpResponse));
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        throw new RuntimeException("should not be there");
    }

    /**
     * 发送post报文
     *
     * @param uri        URI
     * @param parameters 内容
     * @return 返回报文
     */
    public String post(final String uri, final Map<String, String> parameters) {
        return post(uri, parameters, "UTF-8");
    }

    /**
     * 发送post报文
     *
     * @param uri        URI
     * @param parameters 内容
     * @return 返回报文
     */
    public String post(final String uri,
                       final Map<String, String> parameters,
                       final String charset) {
        final RequestBuilder requestBuilder = RequestBuilder.post()
                .setUri(uri)
                .setConfig(httpClientAbstractFactory.createRequestConfig())
                .addHeader(new BasicHeader(HTTP.CONTENT_ENCODING, charset));
        for (final Map.Entry<String, String> entry : parameters.entrySet()) {
            requestBuilder.addParameter(entry.getKey(), entry.getValue());
        }
        requestBuilder.setEntity(new UrlEncodedFormEntity(requestBuilder.getParameters(),
                Charset.forName(charset)));
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
        getRequest.setConfig(httpClientAbstractFactory.createRequestConfig());
        try (final CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(getRequest)) {
            return EntityUtils.toString(getResponseEntity(closeableHttpResponse));
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        throw new RuntimeException("should not be there");
    }

    /**
     * 回收apache http client
     */
    @Override
    public void close() {
        try {
            httpClientAbstractFactory.recycleHttpClient(closeableHttpClient);
        } catch (Exception ignore) {
            log.debug("close error:", ignore);
        }
    }
}
