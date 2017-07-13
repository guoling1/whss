package com.jkm.hss.helper.response;

import com.jkm.hss.dealer.helper.response.DealerProfitSettingResponse;
import lombok.Data;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/12/9.
 */
@Data
public class FirstDealerProductDetailResponse {
    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品列表
     */
    private Product product;

    @Data
    public class Product {


        /**
         * 产品id
         */
        private long productId;

        /**
         * 产品名称 快收银
         */
        private String productName;


        /**
         * 通道列表
         */
        private List<Channel> channels;
    }

    @Data
    public static class Channel {

        /**
         * 通道类型
         *
         * {@link com.jkm.hss.product.enums.EnumPayChannelSign}
         */
        private int channelType;

        /**
         * 通道名称
         */
        private String channelName;
        /**
         * 最小支付结算手续费
         */
        private String minPaymentSettleRate;
        /**
         * 支付结算手续费
         */
        private String paymentSettleRate;

        /**
         * 结算类型（结算时间）
         *
         * {@link com.jkm.hss.product.enums.EnumBalanceTimeType}
         */
        private String settleType;
        /**
         * 最小提现结算费
         */
        private String minWithdrawSettleFee;
        /**
         * 提现结算费
         */
        private String withdrawSettleFee;

        /**
         * 商户结算手续费（商户支付手续费）
         */
        private String merchantSettleRate;
        /**
         * 最大商户结算手续费
         */
        private String maxMerchantSettleRate;
        /**
         * 最小商户结算手续费
         */
        private String minMerchantSettleRate;
        /**
         * 商户提现手续费
         */
        private String merchantWithdrawFee;
        /**
         * 最大商户提现手续费
         */
        private String maxMerchantWithdrawFee;
        /**
         * 最小商户提现手续费
         */
        private String minMerchantWithdrawFee;
    }

    /**
     * 合伙人推荐分润
     */
    private List<DealerUpgerdeRate> dealerUpgerdeRates;
    @Data
    public  class DealerUpgerdeRate {
        /**
         * 升级分润费率编码
         */
        private long id;
        /**
         * 产品编码
         */
        private long productId;
        /**
         * 代理商编码
         */
        private long dealerId;
        /**
         * 分润类型
         * {@link com.jkm.hss.dealer.enums.EnumDealerRateType}
         */
        private int type;
        /**
         *金开门分润比例
         */
        private String bossDealerShareRate;
        /**
         *金开门分润比例
         */
        private String oemShareRate;
        /**
         * 一代分润
         */
        private String firstDealerShareProfitRate;
        /**
         * 二代分润
         */
        private String secondDealerShareProfitRate;
    }
    /**
     * 合伙人推荐分润
     */
    private List<DealerProfitSettingResponse> dealerProfits;
}
