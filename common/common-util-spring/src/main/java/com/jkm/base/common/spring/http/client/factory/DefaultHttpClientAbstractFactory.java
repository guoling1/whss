package com.jkm.base.common.spring.http.client.factory;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

@Slf4j
@Component
public class DefaultHttpClientAbstractFactory implements HttpClientAbstractFactory {

    /**
     * 默认配置
     */
    private final static RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
            .setConnectTimeout(5000)
            .setSocketTimeout(120000)
            .setConnectionRequestTimeout(120000)
            .build();

    /**
     * 获得 PoolingHttpClientConnectionManager
     *
     * @return
     * @throws Exception
     */
    private PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() throws Exception{
        final SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
        final SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
            /**
             * {@inheritDoc}
             */
            @Override
            public boolean verify(final String s, final SSLSession sslSession) {
                return true;
            }
        });
        final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslConnectionSocketFactory)
                .build();
        return new PoolingHttpClientConnectionManager(socketFactoryRegistry);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CloseableHttpClient createHttpClient() {
        try {
            final PoolingHttpClientConnectionManager connectionManager = this.getPoolingHttpClientConnectionManager();
            final CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
            return httpClient;
        } catch (Exception e) {
            log.debug("get http client error", e);
            throw Throwables.propagate(e);
        }
    }
}
