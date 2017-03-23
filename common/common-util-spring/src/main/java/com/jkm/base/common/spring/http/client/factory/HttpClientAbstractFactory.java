package com.jkm.base.common.spring.http.client.factory;

import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created by yulong.zhang on 17/2/23.
 */
public interface HttpClientAbstractFactory {
    /**
     * 创建apache http client
     *
     * @return apache http client
     */
    CloseableHttpClient createHttpClient();
}
