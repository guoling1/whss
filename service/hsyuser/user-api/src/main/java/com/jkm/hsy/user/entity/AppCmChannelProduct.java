package com.jkm.hsy.user.entity;

import java.math.BigDecimal;

/**
 * Created by Allen on 2017/5/26.
 */
public class AppCmChannelProduct {
    private Long id;
    private String channelName;//通道名称
    private String channelType;//通道类型 微信wechat 支付宝alipay
    private String channelSettleType;//通道结算类型 D0 T1
    private BigDecimal channelRate;//交易费率 有代理商的情况是代理商定制的费率
    private BigDecimal channelWithdrawFee;//提现成本费用 元／笔 有代理商的情况是代理商定制的提现费
    private Integer channelSign;//通道唯一标识
    private Integer isNeed;//是否需要商户入网 1 需要 2 不需要

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelSettleType() {
        return channelSettleType;
    }

    public void setChannelSettleType(String channelSettleType) {
        this.channelSettleType = channelSettleType;
    }

    public BigDecimal getChannelRate() {
        return channelRate;
    }

    public void setChannelRate(BigDecimal channelRate) {
        this.channelRate = channelRate;
    }

    public BigDecimal getChannelWithdrawFee() {
        return channelWithdrawFee;
    }

    public void setChannelWithdrawFee(BigDecimal channelWithdrawFee) {
        this.channelWithdrawFee = channelWithdrawFee;
    }

    public Integer getChannelSign() {
        return channelSign;
    }

    public void setChannelSign(Integer channelSign) {
        this.channelSign = channelSign;
    }

    public Integer getIsNeed() {
        return isNeed;
    }

    public void setIsNeed(Integer isNeed) {
        this.isNeed = isNeed;
    }
}
