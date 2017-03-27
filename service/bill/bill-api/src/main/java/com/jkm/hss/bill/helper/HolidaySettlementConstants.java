package com.jkm.hss.bill.helper;

import lombok.experimental.UtilityClass;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigCache;

/**
 * Created by yulong.zhang on 2017/3/16.
 */
@UtilityClass
public final class HolidaySettlementConstants {


    public static final boolean HOLIDAY_OPEN;


    static {
        final HolidaySettlementConfig config = getConfig();
        HOLIDAY_OPEN = config.holidayOpen();
    }


    private static HolidaySettlementConfig getConfig() {
        return ConfigCache.getOrCreate(HolidaySettlementConfig.class);
    }

    @Config.Sources("classpath:mergeTableSettlement.properties")
    private interface HolidaySettlementConfig extends Config {

        @Key("holiday.open")
        @DefaultValue("false")
        boolean holidayOpen();
    }
}
