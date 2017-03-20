package com.jkm.hss.helper.response;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/3/17.
 */
@Data
public class QuerySupportBankResponse {

    private long id;
    /**
     * 通道名称
     */
    private String channelName;
    /**
     * 通道编码
     */
    private String channelCode;
    /**
     * 银行编码
     */
    private String bankCode;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 卡类型
     */
    private String cardType;
    /**
     * 单笔限额
     */
    private String singleLimitAmount;
    /**
     * 日累计限额
     */
    private String dayLimitAmount;
    /**
     * 状态
     */
    private int status;
}
