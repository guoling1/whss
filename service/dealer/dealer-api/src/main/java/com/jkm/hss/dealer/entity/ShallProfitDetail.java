package com.jkm.hss.dealer.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.dealer.enums.EnumProfitType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-11-24.
 *
 * 收单提现分润明细表
 *
 * tb_shall_profit_detail
 */
@Data
public class ShallProfitDetail extends BaseEntity {
    /**
     * 商户id / 店铺id
     */
    private long merchantId;

    /**
     * 产品类型 ， hss hsy
     * {@link com.jkm.hss.product.enums.EnumProductType}
     */
    private int productType;

    /**
     * 商户交易流水号
     */
    private String paymentSn;
    /**
     * 通道类型
     * {@link com.jkm.hss.product.enums.EnumPayChannelSign}
     */
    private int channelType;
    /**
     * 总金额
     */
    private BigDecimal totalFee;
    /**
     * 待分润金额
     */
    private BigDecimal waitShallAmount;
    /**
     * 待分润原始金额(抹零前的金额)
     */
    private BigDecimal waitShallOriginAmount;
    /**
     * 分润类型(收单, 体现分润)
     * {@link EnumProfitType}
     */
    private int profitType;
    /**
     *是否是直管理分润(一级代理→店铺)
     * 0否 1是
     */
    private int isDirect;
    /**
     * 通道成本
     */
    private BigDecimal channelCost;
    /**
     * 通道分润
     */
    private BigDecimal channelShallAmount;
    /**
     * 产品分润
     */
    private BigDecimal productShallAmount;
    /**
     * 一级代理商id
     */
    private long firstDealerId;
    /**
     * 一级代理商分润
     */
    private BigDecimal firstShallAmount;
    /**
     * 二级代理商id
     */
    private long secondDealerId;
    /**
     * 二级代理商分润
     */
    private BigDecimal secondShallAmount;

    /**
     * 分润日期
     * 1999-09-09
     */
    private String profitDate;
}
