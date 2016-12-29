
package com.jkm.hss.mq.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigCache;

@UtilityClass
public class MqConfig {
//    https://help.aliyun.com/document_detail/29532.html

    public static final String TOPIC;
    public static final String PRODUCER_ID;
    public static final String CONSUMER_ID;
    public static final String ACCESS_KEY;
    public static final String SECRET_KEY;
    public static final String ONSADDR;

    /**
     * 测试
     */
    public static final String TEST = "test";

    /**
     * 提现
     */
    public static final String MERCHANT_WITHDRAW = "hss_merchant_withdraw";

    static {
        final MqConfigs mqConfigs = getMqConfigs();
        TOPIC = mqConfigs.topic();
        Preconditions.checkState(!Strings.isNullOrEmpty(TOPIC), "load topic error");
        PRODUCER_ID = mqConfigs.producerId();
        Preconditions.checkState(!Strings.isNullOrEmpty(PRODUCER_ID), "load producerId error");
        CONSUMER_ID = mqConfigs.consumerId();
        Preconditions.checkState(!Strings.isNullOrEmpty(CONSUMER_ID), "load consumerId error");
        ACCESS_KEY = mqConfigs.accessKey();
        Preconditions.checkState(!Strings.isNullOrEmpty(ACCESS_KEY), "load accessKey error");
        SECRET_KEY = mqConfigs.secretKey();
        Preconditions.checkState(!Strings.isNullOrEmpty(SECRET_KEY), "load secretKey error");
        ONSADDR = mqConfigs.onsaddr();
        Preconditions.checkState(!Strings.isNullOrEmpty(ONSADDR), "load onsaddr error");
    }
    /**
     * 获得配置
     */
    public static MqConfigs getMqConfigs() {
        return ConfigCache.getOrCreate(MqConfigs.class);
    }

    /**
     * mq配置
     */
    @org.aeonbits.owner.Config.Sources("classpath:mq.properties")
    private interface MqConfigs extends org.aeonbits.owner.Config {

        @Key("mq.topic")
        @DefaultValue("")
        String topic();

        @Key("mq.producer.id")
        @DefaultValue("")
        String producerId();

        @Key("mq.consumer.id")
        @DefaultValue("")
        String consumerId();

        @Key("mq.access.key")
        @DefaultValue("")
        String accessKey();

        @Key("mq.secret.key")
        @DefaultValue("")
        String secretKey();

        @Key("mq.onsaddr")
        @DefaultValue("")
        String onsaddr();
    }
}
