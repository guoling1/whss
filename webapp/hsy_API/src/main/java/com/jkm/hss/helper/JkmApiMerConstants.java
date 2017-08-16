package com.jkm.hss.helper;

import com.google.common.collect.ImmutableMap;


/**
 * Created by yuxiang on 2017-08-13.
 */
public class JkmApiMerConstants {



    private static final ImmutableMap<String, String> INIT_MAP;

    static {
        final ImmutableMap.Builder<String, String> builder = new ImmutableMap.Builder<>();
            builder.put("110000000093", "X1w8LsNHoluMlnF8jxG3J2Mw0ZTR5qyu");
            builder.put("110000000132", "X1w8LsNHoluMlnF8jxG3J2Mw0ZTR5132");
             builder.put("110000000047", "X1w8LsNHoluMlnF8jxG3J2Mw0ZTR5047");
            INIT_MAP = builder.build();
    }

    public static String keyOf(final String merchantNo) {
        return INIT_MAP.get(merchantNo);
    }

}
