package com.jkm.hss.helper.response;

import com.jkm.hss.dealer.helper.response.DealerProfitSettingResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xingliujie on 2017/2/15.
 */
@Data
public class DealerPolicyResponse {
    /**
     * 是否展示合伙人推荐分润设置 1展示 2不展示
     */
    private String isShow;
    /**
     * 推广码
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
//    private BigDecimal totalProfitSpace;
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
    private List<DealerProfitSettingResponse> dealerProfits;
}
