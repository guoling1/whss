package com.jkm.hss.bill.helper;

import lombok.experimental.UtilityClass;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigCache;

/**
 * Created by yulong.zhang on 2017/6/23.
 */
@UtilityClass
public final class ApplicationConsts {

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

        /**
         * 域名
         *
         * @return
         */
        @Key("oss.file.path")
        @DefaultValue("http://img.jinkaimen.cn/")
        String ossFilePath();

        /**
         * 邮件服务器server
         *
         * @return
         */
        @Key("email.server.host")
        @DefaultValue("smtp.mxhichina.com")
        String emailServerHost();

        /**
         * 邮件服务器端口
         *
         * @return
         */
        @Key("email.server.port")
        @DefaultValue("25")
        String emailServerPort();

        /**
         * 邮件用户名
         *
         * @return
         */
        @Key("email.user.name")
        @DefaultValue("CSC@jinkaimen.com")
        String emailUserName();

        /**
         * 密码
         *
         * @return
         */
        @Key("email.user.password")
        @DefaultValue("Qwer1234")
        String emailPassword();

        /**
         * 发送地址
         *
         * @return
         */
        @Key("email.from.address")
        @DefaultValue("CSC@jinkaimen.com")
        String emailFromAddress();
    }
}
