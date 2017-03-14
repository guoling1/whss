package com.jkm.hss.bill.entity;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/3/13.
 *
 * 快捷支付 确认支付  入参
 */
@Data
public class PaymentSdkConfirmUnionPayRequest {

    /**
     * 交易订单号
     */
    private String orderNo;
    /**
     * 验证码
     */
    private String code;
}
