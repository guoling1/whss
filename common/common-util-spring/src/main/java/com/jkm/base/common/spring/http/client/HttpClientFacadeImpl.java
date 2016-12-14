package com.jkm.base.common.spring.http.client;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.jkm.base.common.spring.http.client.factory.HttpClientAbstractFactory;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by hutao on 15/6/23.
 * 下午5:12
 */
@Component
public class HttpClientFacadeImpl implements HttpClientFacade {
    /**
     * 线程池大小
     */
    private static final int MAX_THREAD_NUM = 10;
    /**
     * 线程池
     */
    private static final ListeningExecutorService LISTENING_EXECUTOR_SERVICE = MoreExecutors.listeningDecorator(
            Executors.newFixedThreadPool(MAX_THREAD_NUM));
    /**
     * apache http client辅助抽象工厂
     */
    @Autowired
    private HttpClientAbstractFactory httpClientAbstractFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public String post(final String uri, final String content) {
        try (final HttpClient httpClient = createHttpClient()) {
            return httpClient.post(uri, content);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String post(final String uri,
                       final List<Pair<String, String>> parameters) {
        try (final HttpClient httpClient = createHttpClient()) {
            return httpClient.post(uri, parameters);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String post(final String uri, final Map<String, String> parameters) {
        try (final HttpClient httpClient = createHttpClient()) {
            return httpClient.post(uri, parameters);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String post(final String uri,
                       final Map<String, String> parameters,
                       final String charset) {
        try (final HttpClient httpClient = createHttpClient()) {
            return httpClient.post(uri, parameters, charset);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<String> asyncPost(final String uri, final String content) {
        return LISTENING_EXECUTOR_SERVICE.submit(buildPostTask(uri, content));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(final String uri) {
        try (final HttpClient httpClient = createHttpClient()) {
            return httpClient.get(uri);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<String> asyncGet(final String uri) {
        return LISTENING_EXECUTOR_SERVICE.submit(buildGetTask(uri));
    }

    private HttpClient createHttpClient() {
        return new HttpClient(httpClientAbstractFactory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void asyncPost(final String uri, final String content, final FutureCallback<String> futureCallback) {
        Futures.addCallback(LISTENING_EXECUTOR_SERVICE.submit(
                buildPostTask(uri, content)), futureCallback);
    }

    private AsyncPostTask buildPostTask(final String uri, final String content) {
        return new AsyncPostTask(uri, content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void asyncGet(final String uri, final FutureCallback<String> futureCallback) {
        Futures.addCallback(LISTENING_EXECUTOR_SERVICE.submit(
                buildGetTask(uri)), futureCallback);
    }

    private AsyncGetTask buildGetTask(final String uri) {
        return new AsyncGetTask(uri);
    }

    /**
     * 异步发送post请求任务
     */
    class AsyncPostTask implements Callable<String> {
        /**
         * URI
         */
        private final String uri;
        /**
         * 内容
         */
        private final String content;

        /**
         * @param uri     URI
         * @param content 内容
         */
        public AsyncPostTask(final String uri, final String content) {
            this.uri = uri;
            this.content = content;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String call() throws Exception {
            return post(uri, content);
        }
    }

    /**
     * 异步发送GET
     * 请求任务
     */
    class AsyncGetTask implements Callable<String> {
        /**
         * URI
         */
        private final String uri;

        /**
         * @param uri URI
         */
        public AsyncGetTask(final String uri) {
            this.uri = uri;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String call() throws Exception {
            return get(uri);
        }
    }
}
