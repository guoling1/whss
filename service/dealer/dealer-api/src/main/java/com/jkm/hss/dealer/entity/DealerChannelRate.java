package com.jkm.hss.dealer.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-11-24.
 *
 * 代理商通道费率表
 *
 * tb_dealer_channel_rate
 */
@Data
public class DealerChannelRate extends BaseEntity{


    /**
     * 代理商id
     */
    private long dealerId;

    /**
     * 产品id
     */
    private long productId;

    /**
     * 通道唯一标识
     */
    private int channelTypeSign;

    /**
     * 产品支付结算手续费率
     */
    private BigDecimal dealerTradeRate;

    /**
     * 结算时间
     * @link com.jkm.hss.product.enums.EnumBalanceTimeType
     */
    private String dealerBalanceType;

    /**
     * 产品提现结算费用 每笔
     */
    private BigDecimal dealerWithdrawFee;

    /**
     * 产品商户支付手续费
     */
    private BigDecimal dealerMerchantPayRate;

    /**
     * 商户体现手续费
     */
    private BigDecimal dealerMerchantWithdrawFee;
}

