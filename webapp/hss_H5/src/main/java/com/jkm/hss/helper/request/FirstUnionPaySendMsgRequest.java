package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/3/13.
 *
 * 首次支付， 发送验证码， 入参
 */
@Data
public class FirstUnionPaySendMsgRequest {

    /**
     * 金额
     */
    private String amount;
    /**
     * 渠道
     */
    private int channel;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 信用卡行卡号
     */
    private String bankCardNo;
    /**
     * 信用卡有效期
     */
    private String expireDate;
    /**
     * 信用卡cvv2
     */
    private String cvv2;
    /**
     * 信用卡预留手机号
     */
    private String mobile;
}
