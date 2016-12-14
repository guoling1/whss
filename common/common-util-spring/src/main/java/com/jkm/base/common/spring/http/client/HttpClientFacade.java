package com.jkm.base.common.spring.http.client;

import com.google.common.util.concurrent.FutureCallback;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by hutao on 15/6/23.
 * 下午4:17
 */
public interface HttpClientFacade {
    /**
     * 同步发送post http请求
     *
     * @param uri     uri
     * @param content 内容
     * @return 返回报文
     */
    String post(String uri, String content);

    /**
     * 同步发送post http请求
     *
     * @param uri
     * @param parameters
     * @return
     */
    String post(final String uri, final List<Pair<String, String>> parameters);

    /**
     * 同步发送post http请求
     *
     * @param uri
     * @param parameters
     * @return
     */
    String post(final String uri, final Map<String, String> parameters);

    /**
     * 同步发送post http请求
     *
     * @param uri
     * @param parameters
     * @return
     */
    String post(final String uri, final Map<String, String> parameters, final String charset);

    /**
     * 异步发送post http请求
     *
     * @param uri     uri
     * @param content 内容
     * @return 返回报文
     */
    Future<String> asyncPost(String uri, String content);

    /**
     * 异步发送post http请求
     *
     * @param uri            uri
     * @param content        内容
     * @param futureCallback 回调
     */
    void asyncPost(String uri, String content, FutureCallback<String> futureCallback);

    /**
     * 同步发送get http请求
     *
     * @param uri uri
     * @return 返回报文
     */
    String get(String uri);

    /**
     * 异步发送get http请求
     *
     * @param uri uri
     * @return future
     */
    Future<String> asyncGet(String uri);

    /**
     * 异步发送get http请求
     *
     * @param uri            uri
     * @param futureCallback 回调
     */
    void asyncGet(String uri, FutureCallback<String> futureCallback);
}
