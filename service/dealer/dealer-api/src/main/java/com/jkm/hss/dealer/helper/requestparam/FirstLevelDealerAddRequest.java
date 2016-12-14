package com.jkm.hss.dealer.helper.requestparam;

import lombok.Data;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/11/24.
 *
 * 增加代理商 入参
 */
@Data
public class FirstLevelDealerAddRequest {

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
