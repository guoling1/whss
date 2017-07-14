package com.jkm.hss.helper;

import lombok.experimental.UtilityClass;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigCache;

/**
 * Created by huangwei on 3/3/16.
 */
@UtilityClass
public class ApplicationConsts {

    /**
     * 请求地址
     */
    public static final String REQUEST_URL = "REQUEST_URL";

    /**
     * request中经销商登录信息key
     */
    public static final String REQUEST_USER_INFO_DATA_BIND_DEALER = "REQUEST_USER_INFO_DATA_BIND_DEALER";

    /**
     * 经销商的cookie key
     */
    public static final String DEALER_COOKIE_KEY = "_t_dealer";

    /**
     * request中boss后台登录信息key
     */
    public static final String REQUEST_USER_INFO_DATA_BIND_ADMIN = "REQUEST_USER_INFO_DATA_BIND_ADMIN";

    /**
     * boss后台的cookie key
     */
    public static final String ADMIN_COOKIE_KEY = "_t_admin";


    /**
     * request中商户登录信息key
     */
    public static final String REQUEST_USER_INFO_DATA_BIND_MERCHANT = "REQUEST_USER_INFO_DATA_BIND_MERCHANT";

    /**
     * 商户的cookie key
     */
    public static final String MERCHANT_COOKIE_KEY = "_t_merchant_info_010";

    /**
     * app用户pc登录
     */
    public static final String REQUEST_APP_USER_PC_DATA_BIND = "REQUEST_APP_USER_PC_DATA_BIND";

    /**
     * app用户pc登录cookie
     */
    public static final String APP_USER_PC_COOKIE_KEY = "APP_USER_PC_COOKIE_KEY";



    /**
     * 获取配置
     *
     * @return
     */
    public static ApplicationConfig getApplicationConfig() {
        return ConfigCache.getOrCreate(ApplicationConfig.class);
    }

    @Config.Sources("classpath:application.properties")
    public interface ApplicationConfig extends Config {
        /**
         * 当前域名
         *
         * @return
         */
        @Key("domain")
        String domain();

        /**
         * oss_bucke
         *
         * @return
         */
        @Key("oss_bucket")
        String ossBucke();

        /**
         * oss_bind_host
         *
         * @return
         */
        @Key("oss_bind_host")
        String ossBindHost();
    }
}
