package com.jkm.hss.helper.request;

import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.enums.EnumMerchantWithdrawType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yuxiang on 2016-11-29.
 */
@Data
public class ProductAddRequest {

    /**
     * 产品类型
     */
    private String type;

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
     * {@link com.jkm.hss.product.enums.EnumDealerBalanceType}
     */
    private String dealerBalanceType;

    /**
     * 通道
     */
    private List<ProductChannelDetail> channels;
}
