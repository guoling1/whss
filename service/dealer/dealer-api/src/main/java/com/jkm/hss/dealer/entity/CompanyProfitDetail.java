package com.jkm.hss.dealer.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.dealer.enums.EnumProfitType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-12-05.
 *
 * 公司直属商户的分润
 * tb_company_profit_detail
 */
@Data
public class CompanyProfitDetail extends BaseEntity {

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
     * 分润类型(收单, 提现分润)
     * {@link EnumProfitType}
     */
    private int profitType;
    /**
     * 通道成本
     */
    private BigDecimal channelCost;
    /**
     * 产品分润
     */
    private BigDecimal productShallAmount;
    /**
     * 通道分润
     */
    private BigDecimal channelShallAmount;
    /**
     * 分润日期
     */
    private String profitDate;
}
