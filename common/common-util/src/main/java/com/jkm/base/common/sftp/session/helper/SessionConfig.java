package com.jkm.base.common.sftp.session.helper;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.Map;

/**
 * Created by hutao on 15/12/3.
 * 下午2:31
 */
@Builder
@Getter
public class SessionConfig {
    private static final int DEFAULT_CONNECT_TIME = 3000;
    @Singular
    private Map<String, String> configs;
    private int connectTime = DEFAULT_CONNECT_TIME;

    /**
     * @return
     */
    public static SessionConfigBuilder custom() {
        return SessionConfig.builder().connectTime(DEFAULT_CONNECT_TIME)
                .config("StrictHostKeyChecking", "no");
    }

    /**
     * 默认配置
     *
     * @return
     */
    public static SessionConfig createDefault() {
        return custom().build();
    }
}
