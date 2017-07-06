package com.jkm.hss.admin.helper;

import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigCache;

/**
 * Created by huangwei on 3/2/16.
 */
@UtilityClass
public class AdminConsts {

    /**
     * token 过期时间
     */
    public static final long TOKEN_EXPIRE_MILLIS = getAdminConfig().tokenExpireMillis();

    /**
     * 获取用户配置
     *
     * @return
     */
    public static AdminConfig getAdminConfig() {
        return ConfigCache.getOrCreate(AdminConfig.class);
    }

    @org.aeonbits.owner.Config.Sources("classpath:admin-config.properties")
    public interface AdminConfig extends org.aeonbits.owner.Config {

        /**
         * admin信息表密码
         *
         * @return
         */
        @Key("tb.admin.encrypt.key")
        @DefaultValue("0bADlmLHNlngci5FTlogupeG0m4fLWV9")
        String tbAdminEncryptKey();

        /**
         * admin user passport encrypt key
         *
         * @return
         */
        @Key("tb.admin.passport.encrypt.key")
        @DefaultValue("0SWOaCDnIS5jZljQXdmqAhsBJguD4M6h")
        String tbAdminUserTokenEncryptKey();

        /**
         * user token expire time
         *
         * @return
         */
        @Key("tb.admin.passport.expire.millis")
        @DefaultValue("1800000")
        long tokenExpireMillis();

    }

}
