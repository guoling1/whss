package com.jkm.chronos;

import org.aeonbits.owner.ConfigCache;

/**
 * Created by huangwei on 3/10/16.
 */
public class ChronosConsts {

    /**
     * 获取定时任务配置
     *
     * @return
     */
    public static ChronosConfig getChronosConfig() {
        return ConfigCache.getOrCreate(ChronosConfig.class);
    }

    @org.aeonbits.owner.Config.Sources("classpath:application.properties")
    public interface ChronosConfig extends org.aeonbits.owner.Config {

    }


}
