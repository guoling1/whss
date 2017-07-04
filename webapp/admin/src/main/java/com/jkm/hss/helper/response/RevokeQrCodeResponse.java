package com.jkm.hss.helper.response;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/30.
 */
@Data
public class RevokeQrCodeResponse {
    /**
     * 开始码段
     */
    private String startCode;
    /**
     * 结束码段
     */
    private String endCode;
    /**
     * 成功数量
     */
    private long successCount;
    /**
     * 失败数量
     */
    private long failCount;
}
