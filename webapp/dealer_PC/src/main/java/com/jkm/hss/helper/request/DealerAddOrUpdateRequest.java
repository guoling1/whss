package com.jkm.hss.helper.request;

import lombok.Data;

import java.util.List;

/**
 * Created by xingliujie on 2016/12/9.
 */
@Data
public class DealerAddOrUpdateRequest {

    /**
     * 一级代理商id
     */
    private long dealerId;

    /**
     * 推广功能
     * {@link com.jkm.hss.dealer.enums.EnumInviteBtn}
     */
    private int inviteBtn;

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
         * 提现结算费
         */
        private String withdrawSettleFee;

    }
}
