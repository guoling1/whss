package com.jkm.base.common.spring.http.client.factory;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created by hutao on 15/6/23.
 * 下午5:14
 */
public interface HttpClientAbstractFactory {
    /**
     * 创建apache http client
     *
     * @return apache http client
     */
    CloseableHttpClient createHttpClient();

    /**
     * 回收apache http client
     *
     * @param closeableHttpClient apache http client
     */
    void recycleHttpClient(CloseableHttpClient closeableHttpClient);

    /**
     * 创建的http request参数
     *
     * @return
     */
    RequestConfig createRequestConfig();
}
