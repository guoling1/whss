package com.jkm.hss.bill.entity;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/3/13.
 *
 * 快捷 确认支付  出参
 */
@Data
public class PaymentSdkConfirmUnionPayResponse {

    /**
     * 交易订单号
     */
    private String orderNo;

    /**
     * 成功标识
     *
     * {@link com.jkm.hss.bill.enums.EnumBasicStatus}
     */
    private int code;

    /**
     * 错误信息
     */
    private String message;
}
