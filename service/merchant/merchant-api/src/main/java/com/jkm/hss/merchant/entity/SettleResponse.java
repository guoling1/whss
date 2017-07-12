package com.jkm.hss.merchant.entity;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class SettleResponse {

    /**
     * 微信费率
     */
    private BigDecimal weixinRate;
    /**
     * 支付宝费率
     */
    private BigDecimal alipayRate;
    /**
     * 快捷费率
     */
    private BigDecimal fastRate;


}
