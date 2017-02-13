package com.jkm.hss.helper.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/12/9.
 */
@Data
public class FirstLevelDealerGet2Response {
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 合伙人推荐功能开关
     * {@link com.jkm.hss.dealer.enums.EnumRecommendBtn}
     */
    private int recommendBtn;
    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 推广功能
     * {@link com.jkm.hss.dealer.enums.EnumInviteBtn}
     */
    private int inviteBtn;

    /**
     * 收单总分润空间
     */
    private BigDecimal totalProfitSpace;
    /**
     * 合伙人推荐分润
     */
    private List<DealerUpgerdeRate> dealerUpgerdeRates;
    @Data
    public static class DealerUpgerdeRate {
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
         *一级代理商分润比例
         */
        private String firstDealerShareProfitRate;

        /**
         *二级代理商分润比例
         */
        private String secondDealerShareProfitRate;

        /**
         *金开门分润比例
         */
        private String bossDealerShareRate;
    }

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
         * 支付手续费加价限额(允许一级代理商提高商户的手续费最高限制，例如：0.05%)
         */
        private String limitPayFeeRate;

        /**
         * 允许一级代理商提高商户的手续费最高限制，例如：0.5元／笔
         */
        private String limitWithdrawFeeRate;

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
         * 提现结算费
         */
        private String withdrawSettleFee;

        /**
         * 商户结算手续费（商户支付手续费）
         */
        private String merchantSettleRate;

        /**
         * 商户提现手续费
         */
        private String merchantWithdrawFee;
    }
}
