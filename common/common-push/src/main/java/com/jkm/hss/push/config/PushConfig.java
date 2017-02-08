
package com.jkm.hss.push.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigCache;

@UtilityClass
public class PushConfig {
//    https://help.aliyun.com/document_detail/29532.html

    public static final String APPID;
    public static final String APPKEY;
    public static final String MASTERSECRET;
    public static final String URL;


    /**
     * 测试
     */
    public static final String TEST = "test";

    /**
     * 提现
     */
    public static final String MERCHANT_WITHDRAW = "hss_merchant_withdraw";

    static {
        final PushConfigs pushConfigs = getConfigs();
        APPID = pushConfigs.appId();
        Preconditions.checkState(!Strings.isNullOrEmpty(APPID), "load appid error");
        APPKEY = pushConfigs.appKey();
        Preconditions.checkState(!Strings.isNullOrEmpty(APPKEY), "load appkey error");
        MASTERSECRET = pushConfigs.masterSecret();
        Preconditions.checkState(!Strings.isNullOrEmpty(MASTERSECRET), "load mastersecret error");
        URL = pushConfigs.url();
        Preconditions.checkState(!Strings.isNullOrEmpty(URL), "load url error");

    }
    /**
     * 获得配置
     */
    public static PushConfigs getConfigs() {
        return ConfigCache.getOrCreate(PushConfigs.class);
    }

    /**
     * mq配置
     */
    @org.aeonbits.owner.Config.Sources("classpath:push.properties")
    private interface PushConfigs extends org.aeonbits.owner.Config {

        @Key("push.appId")
        @DefaultValue("")
        String appId();

        @Key("push.appKey")
        @DefaultValue("")
        String appKey();

        @Key("push.masterSecret")
        @DefaultValue("")
        String masterSecret();

        @Key("push.url")
        @DefaultValue("")
        String url();


    }
}
