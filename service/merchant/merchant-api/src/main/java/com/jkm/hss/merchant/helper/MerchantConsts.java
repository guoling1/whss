package com.jkm.hss.merchant.helper;

import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigCache;

/**
 *
 */
@UtilityClass
public class MerchantConsts {

    /**
     * token 过期时间
     */
    public static final long TOKEN_EXPIRE_MILLIS = getMerchantConfig().tokenExpireMillis();

    /**
     * 获取用户配置
     *
     * @return
     */
    public static MerchantConfig getMerchantConfig() {
        return ConfigCache.getOrCreate(MerchantConfig.class);
    }

    @org.aeonbits.owner.Config.Sources("classpath:merchant.properties")
    public interface MerchantConfig extends org.aeonbits.owner.Config {

        /**
         * dealer信息表密码
         *
         * @return
         */
        @Key("tb.merchant.encrypt.key")
        @DefaultValue("0K2Xfh4x6OZvxlwSKPnJOem0hMOjOxsg")
        String tbMerchantEncryptKey();

        /**
         * dealer passport token encrypt key
         *
         * @return
         */
        @Key("tb.merchant.passport.encrypt.key")
        @DefaultValue("OJYLgYHW8dREFXPD0DiTSJF3CJEX9PS")
        String tbMerchantPassportEncryptKey();

        /**
         * user passport token expire time
         *
         * @return
         */
        @Key("tb.merchant.passport.expire.millis")
        @DefaultValue("1800000")
        long tokenExpireMillis();

        @Key("pay.domain")
        @DefaultValue("")
        String domain();

        @Key("pay.back.domain")
        @DefaultValue("")
        String backDomain();

        @Key("pay.trade.url")
        @DefaultValue("")
        String trade();

        @Key("pay.otherPay.url")
        @DefaultValue("")
        String otherPay();

        @Key("pay.otherPay.thirdFee")
        @DefaultValue("0.50")
        String thirdFee();

        @Key("pay.otherPay.channelFee")
        @DefaultValue("2.00")
        String channelFee();

        @Key("pay.otherPay.dfQuery")
        @DefaultValue("")
        String dfQuery();
        @Key("pay.trade.orderQuery")
        @DefaultValue("")
        String orderQuery();
    }
}
