package com.jkm.hss.notifier.helper;

import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigCache;

/**
 * Created by hutao on 16/3/3.
 * 下午2:20
 */
@UtilityClass
public class NotifierConstants {
    /**
     * account config
     *
     * @return
     */
    public static NotifierConfig getNotifierConfig() {
        return ConfigCache.getOrCreate(NotifierConfig.class);
    }

    @org.aeonbits.owner.Config.Sources("classpath:notifier.properties")
    public interface NotifierConfig extends org.aeonbits.owner.Config {
        /**
         * 平台名称
         *
         * @return
         */
        @Key("platform.name")
        @DefaultValue("钱包++")
        String platformName();

        /**
         * 序列号
         *
         * @return
         */
        @Key("entinfo.sms.sn")
        @DefaultValue("SDK-SKY-010-02681")
        String sn();

        /**
         * 序列号
         *
         * @return
         */
        @Key("entinfo.sms.password")
        @DefaultValue("1CCD755D0C8F6B05ABEC110FE1A50EB8")
        String password();
    }
}
