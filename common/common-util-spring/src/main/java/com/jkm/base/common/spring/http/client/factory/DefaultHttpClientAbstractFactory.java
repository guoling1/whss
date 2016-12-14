package com.jkm.base.common.spring.http.client.factory;

import com.google.common.base.Throwables;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.StackObjectPool;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by hutao on 15/6/23.
 * 下午6:07
 * TODO 后期用连接池做性能优化
 */
@Slf4j
@Component
public class DefaultHttpClientAbstractFactory implements HttpClientAbstractFactory {
    /**
     * http client pool
     */
    private final ObjectPool<CloseableHttpClient> objectPool = new StackObjectPool<>(new BasePoolableObjectFactory<CloseableHttpClient>() {
        /**
         * {@inheritDoc}
         */
        @Override
        public CloseableHttpClient makeObject() throws Exception {
            final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(getSslContext(), new HostnameVerifier() {
                        /**
                         * {@inheritDoc}
                         */
                        @Override
                        public boolean verify(final String s, final SSLSession sslSession) {
                            return true;
                        }
                    }))
                    .build();
            final PoolingHttpClientConnectionManager poolingHttpClientConnectionManager =
                    new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            return HttpClients.custom()
                    .setConnectionManager(poolingHttpClientConnectionManager).build();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void destroyObject(final CloseableHttpClient obj) throws Exception {
            try {
                obj.close();
            } catch (Exception ignore) {
                log.debug("close http client error", ignore);
            }
        }
    });
    @Setter
    private int connectTimeout = 5000;
    @Setter
    private int connectionRequestTimeout = 10000;
    @Setter
    private int socketTimeout = 20000;

    /**
     * {@inheritDoc}
     */
    @Override
    public CloseableHttpClient createHttpClient() {
        try {
            return objectPool.borrowObject();
        } catch (Exception e) {
            log.debug("get http client error", e);
            throw Throwables.propagate(e);
        }
    }

    private SSLContext getSslContext() throws Exception {
        return SSLContexts.custom()
                .loadTrustMaterial(
                        null, new TrustStrategy() {
                            /**
                             *
                             * @param chain
                             * @param authType
                             * @return
                             * @throws CertificateException
                             */
                            public boolean isTrusted(final X509Certificate[] chain,
                                                     final String authType)
                                    throws CertificateException {
                                return true;
                            }
                        })
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void recycleHttpClient(final CloseableHttpClient closeableHttpClient) {
        try {
            objectPool.returnObject(closeableHttpClient);
        } catch (Exception e) {
            log.debug("recycle http client error", e);
            throw Throwables.propagate(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RequestConfig createRequestConfig() {
        return RequestConfig.copy(RequestConfig.DEFAULT)
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .build();
    }
}
