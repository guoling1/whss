package com.jkm.hss.product.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.product.enums.EnumMerchantWithdrawType;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-11-23.
 *
 * hss产品列表
 *{@link EnumPayChannelSign}
 * tb_product
 */
@Data
public class Product extends BaseEntity {

    //产品名称，产品通道配置，产品基础手续费，产品结，结算规则

    /**
     * 本产品对应的资金账户id
     */
    private long accountId;

    /**
     * 产品名称 快收银
     */
    private String productName;

    /**
     * 支付手续费加价限额(允许一级代理商提高商户的手续费最高限制，例如：0.05%)
     */
    private BigDecimal limitPayFeeRate;

    /**
     * 允许一级代理商提高商户的手续费最高限制，例如：0.5元／笔
     */
    private BigDecimal limitWithdrawFeeRate;

    /**
     * 商户提现模式
     * {@link EnumMerchantWithdrawType}
     */
    private String merchantWithdrawType;

    /**
     * 代理商结算模式
     */
    private String dealerBalanceType;
    /**
     * 类型 hss,hsy
     * {@link EnumProductType}
     */
    private String type;
}
