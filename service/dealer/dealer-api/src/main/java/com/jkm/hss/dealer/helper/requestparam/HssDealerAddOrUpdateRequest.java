package com.jkm.hss.dealer.helper.requestparam;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/12/9.
 */
@Data
public class HssDealerAddOrUpdateRequest {

    /**
     * 一级代理商id
     */
    private long dealerId;

    /**
     * 合伙人推荐功能开关
     * {@link com.jkm.hss.dealer.enums.EnumRecommendBtn}
     */
    private int recommendBtn;
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
     * 产品列表
     */
    private Product product;
    /**
     * 合伙人推荐分润
     */
    private List<DealerUpgradeRate> dealerUpgerdeRates;
    @Data
    public static class DealerUpgradeRate {
        /**
         * 代理商id
         */
        private long dealerId;

        /**
         * 升级分润费率编码
         */
        private Long id;
        /**
         * 产品编码
         */
        private long productId;
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


    @Data
    public class Product {

        /**
         * 产品id
         */
        private long productId;

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
