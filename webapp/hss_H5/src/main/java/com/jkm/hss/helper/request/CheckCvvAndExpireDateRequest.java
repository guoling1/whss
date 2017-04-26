package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/4/25.
 */
@Data
public class CheckCvvAndExpireDateRequest {
    /**
     * 渠道
     */
    private int channel;
    /**
     * 信用卡id
     */
    private long creditCardId;
}
