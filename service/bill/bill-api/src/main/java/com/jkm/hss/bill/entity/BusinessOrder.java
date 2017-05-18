package com.jkm.hss.bill.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.bill.enums.EnumBusinessOrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2017/5/18.
 *
 * 业务订单
 *
 * tb_business_order
 *
 * {@link EnumBusinessOrderStatus}
 */
@Data
public class BusinessOrder extends BaseEntity {

    /**
     * 业务线
     *
     * {@link com.jkm.hss.account.enums.EnumAppType}
     */
    private String appId;

    /**
     * 业务订单号
     */
    private String orderNo;

    /**
     * 交易订单号
     */
    private String tradeOrderNo;

    /**
     * 流水号
     */
    private String sn;

    /**
     * 金额
     */
    private BigDecimal tradeAmount;

    /**
     * 商户id
     */
    private long merchantId;

    /**
     * 付款账户（支付宝，微信，快捷)
     *
     */
    private String payAccount;

    /**
     * 付款账户类型
     *
     * {@link com.jkm.hss.product.enums.EnumPaymentChannel}
     */
    private int payAccountType;

    /**
     * 付款成功时间（交易成功）
     */
    private Date paySuccessTime;

    /**
     * 手续费
     */
    private BigDecimal poundage;

    /**
     * 支付费率（只有支付时，有费率，提现没有）
     */
    private BigDecimal payRate;

    /**
     * 商品名字（好收收店铺名）
     */
    private String goodsName;

    /**
     * 商品描述（好收收店铺名）
     */
    private String goodsDescribe;

    /**
     * 备注
     */
    private String remark;

    /**
     * 支付渠道标识（101， 102， 103）
     *
     * {@link com.jkm.hss.product.enums.EnumPayChannelSign}
     */
    private int payChannelSign;

    /**
     * 银行流水号
     */
    private String bankTradeNo;

    /**
     * 交易卡类型
     *
     * {@link com.jkm.hss.account.enums.EnumBankType}
     */
    private String tradeCardType;

    /**
     * 交易卡号
     */
    private String tradeCardNo;

    /**
     * 支付宝/微信订单号
     */
    private String wechatOrAlipayOrderNo;
}
