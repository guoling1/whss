package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/3/10.
 *
 * 查询快捷通道支持的银行卡列表入参
 */
@Data
public class QueryChannelSupportBankRequest {
    /**
     * 通道唯一标志
     */
    private int channelSign;
}
