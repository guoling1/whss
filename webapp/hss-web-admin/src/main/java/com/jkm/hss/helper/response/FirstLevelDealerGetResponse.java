package com.jkm.hss.helper.response;

import lombok.Data;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/12/9.
 */
@Data
public class FirstLevelDealerGetResponse {

    /**
     * 代理手机号
     */
    private String mobile;

    /**
     * 代理名称
     */
    private String name;

    /**
     * 所在地
     */
    private String belongArea;

    /**
     * 结算卡
     */
    private String bankCard;

    /**
     * 银行开户名称
     */
    private String bankAccountName;

    /**
     * 银行预留手机号
     */
    private String bankReserveMobile;

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
