package com.jkm.hss.dealer.helper;

import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigCache;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
@UtilityClass
public class DealerConsts {

    /**
     * token 过期时间
     */
    public static final long TOKEN_EXPIRE_MILLIS = getDealerConfig().tokenExpireMillis();

    /**
     * 获取用户配置
     *
     * @return
     */
    public static DealerConfig getDealerConfig() {
        return ConfigCache.getOrCreate(DealerConfig.class);
    }

    @org.aeonbits.owner.Config.Sources("classpath:dealer.properties")
    public interface DealerConfig extends org.aeonbits.owner.Config {

        /**
         * dealer信息表密码
         *
         * @return
         */
        @Key("tb.dealer.encrypt.key")
        @DefaultValue("0j2Xfh4x6OZvxlwSKPnJOem0hMOjOxsb")
        String tbDealerEncryptKey();

        /**
         * dealer passport token encrypt key
         *
         * @return
         */
        @Key("tb.dealer.passport.encrypt.key")
        @DefaultValue("fsjYLgYHW8dREFXPD0DiTSJF3CJEX9RM")
        String tbDealerPassportEncryptKey();

        /**
         * user passport token expire time
         *
         * @return
         */
        @Key("tb.dealer.passport.expire.millis")
        @DefaultValue("1800000")
        long tokenExpireMillis();
    }
}
