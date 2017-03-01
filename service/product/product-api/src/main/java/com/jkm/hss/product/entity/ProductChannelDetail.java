package com.jkm.hss.product.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.product.enums.EnumBalanceTimeType;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductChannelDetailStatus;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-11-25.
 *
 * tb_product_channel_detail
 *{@link EnumProductChannelDetailStatus}
 * 产品下属通道详情
 */
@Data
public class ProductChannelDetail extends BaseEntity{

    /**
     * 产品id
     */
    private long productId;

    /**
     * 基础通道id
     */
    private long basicChannelId;

    /**
     * 通道标识
     * {@link EnumPayChannelSign}
     */
    private int channelTypeSign;

    /**
     * 通道类型
     */
    private String xx;

    /**
     * 产品支付结算手续费率
     */
    private BigDecimal productTradeRate;

    /**
     * 结算时间
     * {@link EnumBalanceTimeType}
     */
    private String productBalanceType;

    /**
     * 产品提现结算费用 每笔
     */
    private BigDecimal productWithdrawFee;

    /**
     * 产品商户支付手续费率
     */
    private BigDecimal productMerchantPayRate;

    /**
     * 商户体现手续费
     */
    private BigDecimal productMerchantWithdrawFee;
}
