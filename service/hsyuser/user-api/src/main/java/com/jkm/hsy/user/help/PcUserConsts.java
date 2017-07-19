package com.jkm.hsy.user.help;

import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigCache;

/**
 * Created by huangwei on 3/2/16.
 */
@UtilityClass
public class PcUserConsts {

    /**
     * token 过期时间
     */
    public static final long TOKEN_EXPIRE_MILLIS = getConfig().tokenExpireMillis();

    /**
     * 获取用户配置
     *
     * @return
     */
    public static Config getConfig() {
        return ConfigCache.getOrCreate(Config.class);
    }

    @org.aeonbits.owner.Config.Sources("classpath:admin-config.properties")
    public interface Config extends org.aeonbits.owner.Config {

        @Key("tb.app.pc.user.encrypt.key")
        @DefaultValue("6gtFTuQl9ZJojyYNSLXxzUxgNqcBMtKV")
        String tbAppPcEncryptKey();

        /**
         * admin user passport encrypt key
         *
         * @return
         */
        @Key("tb.app.pc.user.passport.encrypt.key")
        @DefaultValue("YLCHI8MGzK2zVHovEGLDgPuLnBbXtujU")
        String tbAppPcUserTokenEncryptKey();

        /**
         * user token expire time
         *
         * @return
         */
        @Key("tb.app.pc.user.passport.expire.millis")
        @DefaultValue("1800000")
        long tokenExpireMillis();

    }

}
