package com.jkm.hsy.user.entity;

import java.math.BigDecimal;

/**
 * Created by Allen on 2017/6/14.
 */
public class AppChannelRate {
    private Long uid;
    private BigDecimal wechatTradeRateT1;
    private BigDecimal alipayTradeRateT1;
    private Integer wechatIsOpen;
    private Integer alipayIsOpen;
    private Integer isOpenD0;
    private BigDecimal withdrawAmount;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public BigDecimal getWechatTradeRateT1() {
        return wechatTradeRateT1;
    }

    public void setWechatTradeRateT1(BigDecimal wechatTradeRateT1) {
        this.wechatTradeRateT1 = wechatTradeRateT1;
    }

    public BigDecimal getAlipayTradeRateT1() {
        return alipayTradeRateT1;
    }

    public void setAlipayTradeRateT1(BigDecimal alipayTradeRateT1) {
        this.alipayTradeRateT1 = alipayTradeRateT1;
    }

    public Integer getWechatIsOpen() {
        return wechatIsOpen;
    }

    public void setWechatIsOpen(Integer wechatIsOpen) {
        this.wechatIsOpen = wechatIsOpen;
    }

    public Integer getAlipayIsOpen() {
        return alipayIsOpen;
    }

    public void setAlipayIsOpen(Integer alipayIsOpen) {
        this.alipayIsOpen = alipayIsOpen;
    }

    public Integer getIsOpenD0() {
        return isOpenD0;
    }

    public void setIsOpenD0(Integer isOpenD0) {
        this.isOpenD0 = isOpenD0;
    }

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }
}
