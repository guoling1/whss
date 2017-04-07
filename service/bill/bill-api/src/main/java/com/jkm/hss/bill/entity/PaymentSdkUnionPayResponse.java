package com.jkm.hss.bill.entity;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/3/13.
 *
 * 支付中心 快捷 预下单  出参
 */
@Data
public class PaymentSdkUnionPayResponse {

    /**
     * 交易订单号
     */
    private String orderNo;

    /**
     * 流水号
     */
    private String sn;

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
