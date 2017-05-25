package com.jkm.hsy.user.entity;

/**
 * Created by Allen on 2017/5/23.
 */

import java.math.BigDecimal;
import java.util.Date;

/**app_policy_recharge_order*/
public class AppPolicyRechargeOrder {
    private Long id;
    private String orderNO;//订单号
    private String tradeNO;//交易订单号
    private Long tradeID;//交易ID
    private BigDecimal tradeAmount;//交易金额
    private BigDecimal realPayAmount;//实付金额
    private BigDecimal marketingAmount;//营销金额
    private Long payeeAccountID;//收款人账户id
    private String ouid;//微信支付宝ID
    private Long memberID;//会员id
    private Long memberAccountID;//会员账户id
    private Long merchantReceiveAccountID;//商户收款账户id
    private String goodsName;//商品名称
    private String goodsDescribe;//商品描述
    private String merchantName;//店铺名称
    private String merchantNO;//globalID商户号
    private String paySN;//流水号
    private Integer payChannelSign;//支付渠道标识
    private Date paySuccessTime;//支付完成时间
    private Integer status;//状态 0待充值 1充值成功 2充值失败
    private Integer type;//类型 1开卡充值 2开卡送 3 充值 4充值送
    private Date createTime;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNO() {
        return orderNO;
    }

    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }

    public String getTradeNO() {
        return tradeNO;
    }

    public void setTradeNO(String tradeNO) {
        this.tradeNO = tradeNO;
    }

    public Long getTradeID() {
        return tradeID;
    }

    public void setTradeID(Long tradeID) {
        this.tradeID = tradeID;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public BigDecimal getRealPayAmount() {
        return realPayAmount;
    }

    public void setRealPayAmount(BigDecimal realPayAmount) {
        this.realPayAmount = realPayAmount;
    }

    public Long getPayeeAccountID() {
        return payeeAccountID;
    }

    public void setPayeeAccountID(Long payeeAccountID) {
        this.payeeAccountID = payeeAccountID;
    }

    public Long getMemberID() {
        return memberID;
    }

    public void setMemberID(Long memberID) {
        this.memberID = memberID;
    }

    public Long getMemberAccountID() {
        return memberAccountID;
    }

    public void setMemberAccountID(Long memberAccountID) {
        this.memberAccountID = memberAccountID;
    }

    public Long getMerchantReceiveAccountID() {
        return merchantReceiveAccountID;
    }

    public void setMerchantReceiveAccountID(Long merchantReceiveAccountID) {
        this.merchantReceiveAccountID = merchantReceiveAccountID;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsDescribe() {
        return goodsDescribe;
    }

    public void setGoodsDescribe(String goodsDescribe) {
        this.goodsDescribe = goodsDescribe;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantNO() {
        return merchantNO;
    }

    public void setMerchantNO(String merchantNO) {
        this.merchantNO = merchantNO;
    }

    public String getPaySN() {
        return paySN;
    }

    public void setPaySN(String paySN) {
        this.paySN = paySN;
    }

    public Integer getPayChannelSign() {
        return payChannelSign;
    }

    public void setPayChannelSign(Integer payChannelSign) {
        this.payChannelSign = payChannelSign;
    }

    public Date getPaySuccessTime() {
        return paySuccessTime;
    }

    public void setPaySuccessTime(Date paySuccessTime) {
        this.paySuccessTime = paySuccessTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getMarketingAmount() {
        return marketingAmount;
    }

    public void setMarketingAmount(BigDecimal marketingAmount) {
        this.marketingAmount = marketingAmount;
    }

    public String getOuid() {
        return ouid;
    }

    public void setOuid(String ouid) {
        this.ouid = ouid;
    }
}
