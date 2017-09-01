package com.jkm.base.sms.service.constants;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/9/1.
 */
public enum EnumSmsSdkChannel {

    DEFAULT("HAIDAI", "缺省通道"),

    ALIYUN("ALIYUN", "阿里云通道")
    ;

    @Getter
    private String code;
    @Getter
    private String value;

    EnumSmsSdkChannel(final String code, final String value) {
        this.code = code;
        this.value = value;
    }

    private final static ImmutableMap<String, EnumSmsSdkChannel> INIT_MAP;

    static {
        final ImmutableMap.Builder<String, EnumSmsSdkChannel> builder = new ImmutableMap.Builder<>();
        for (EnumSmsSdkChannel channel : EnumSmsSdkChannel.values()) {
            builder.put(channel.getCode(), channel);
        }
        INIT_MAP = builder.build();
    }

    public static EnumSmsSdkChannel of(final String code) {
        Preconditions.checkState(INIT_MAP.containsKey(code), "通道错误");
        return INIT_MAP.get(code);
    }
}
