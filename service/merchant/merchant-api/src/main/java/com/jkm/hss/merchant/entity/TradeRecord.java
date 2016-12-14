package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2016/11/22.
 */
@Data
public class TradeRecord extends BaseEntity {

    /**
     * 商户号
     */
    private long merchantId;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 金额(元)
     */
    private BigDecimal totalFee;

    /**
     * 手续费
     */
    private BigDecimal serviceFee;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 商户名称
     */
    private String subName;

    /**
     * 支付渠道
     */
    private String payChannel;

    /**
     * 支付参数
     */
    private String payParams;

    /**
     * 返回参数
     */
    private String resultParams;

    /**
     * 交易单号
     */
    private String outTradeNo;

    /**
     * 交易类型
     */
    private String tradeType;


}
