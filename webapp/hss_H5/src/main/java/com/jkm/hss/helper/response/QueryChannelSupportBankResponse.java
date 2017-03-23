package com.jkm.hss.helper.response;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/3/10.
 *
 * 查询快捷通道支持的银行卡列表  出参
 */
@Data
public class QueryChannelSupportBankResponse {


    private long id;
    /**
     * 通道唯一标志
     */
    private int channelSign;
    /**
     * 银行名字
     */
    private String bankName;
    /**
     * 单笔限额
     */
    private String singleLimitAmount;
    /**
     * 日累计限额
     */
    private String dayLimitAmount;
}
