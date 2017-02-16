package com.jkm.hss.helper.response;

import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.enums.EnumMerchantWithdrawType;
import com.jkm.hss.product.enums.EnumProductType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yuxiang on 2016-12-01.
 */
@Data
public class ProductListResponse {
    /**
     * 产品id
     */
    private long productId;

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
     * 产品通道列表
     */
    private List<ProductChannelDetail> list;

    /**
     * 类型 hss,hsy
     * {@link EnumProductType}
     */
    private String type;
}
