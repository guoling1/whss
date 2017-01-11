package com.jkm.hss.dealer.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.dealer.enums.EnumProfitType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2017-01-11.
 *
 * 推荐商户分润记录
 *
 * tb_partner_shall_profit_detail
 */
@Data
public class PartnerShallProfitDetail extends BaseEntity{

    /**
     * 商户id
     */
    private long merchantId;
    /**
     * 商户交易流水号
     */
    private String orderNo;
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
     * 分润类型(收单, 升级费分润)
     * {@link com.jkm.hss.dealer.enums.EnumDealerRateType}
     */
    private int profitType;

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
    private BigDecimal firstDealerShallAmount;
    /**
     * 二级代理商id
     */
    private long secondDealerId;
    /**
     * 二级代理商分润
     */
    private BigDecimal secondDealerShallAmount;
    /**
     * 上级商户id
     */
    private long firstMerchantId;
    /**
     * 上级商户分润
     */
    private BigDecimal firstMerchantShallAmount;
    /**
     * 上上级商户id
     */
    private long secondMerchantId;
    /**
     * 上上级商户分润
     */
    private BigDecimal secondMerchantShallAmount;

    /**
     * 分润日期
     * 1999-09-09
     */
    private String profitDate;
}
