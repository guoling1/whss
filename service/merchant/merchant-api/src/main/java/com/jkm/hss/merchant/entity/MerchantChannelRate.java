package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-11-24.
 *
 * 代理商通道费率表
 *
 * tb_merchant_channel_rate
 */
@Data
public class MerchantChannelRate extends BaseEntity{


    /**
     * 商户id
     */
    private long merchantId;

    /**
     * 产品id
     */
    private long productId;

    /**
     * 通道唯一标识
     */
    private int channelTypeSign;


    /**
     * 结算时间
     * @link com.jkm.hss.product.enums.EnumBalanceTimeType
     */
    private String merchantBalanceType;


    /**
     * 商户支付手续费
     */
    private BigDecimal merchantPayRate;

    /**
     * 商户提现手续费
     */
    private BigDecimal merchantWithdrawFee;

    /**
     * 三方支付公司
     * {@link com.jkm.hss.merchant.enums.EnumPayMethod}
     *
     */
    private String thirdCompany;

    /**
     *是否已入网
     * {@link com.jkm.hss.merchant.enums.EnumEnterNet}
     */
    private int enterNet;
    /**
     * 公司
     */
    private String channelCompany;

    /**
     * 入网信息
     */
    private String remarks;

}

