package com.jkm.base.common.sftp.helper;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by hutao on 15/12/3.
 * 下午4:09
 */
@Builder
@Getter
public class SftpConfig {
    private static final int DEFAULT_CONNECT_TIME = 3000;
    private int connectTimeout;

    /**
     * 创建默认的配置
     *
     * @return
     */
    public static SftpConfig createDefault() {
        return custom().build();
    }

    /**
     * @return
     */
    private static SftpConfigBuilder custom() {
        return SftpConfig.builder().connectTimeout(DEFAULT_CONNECT_TIME);
    }
}
