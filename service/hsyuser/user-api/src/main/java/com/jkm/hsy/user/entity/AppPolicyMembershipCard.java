package com.jkm.hsy.user.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Allen on 2017/5/12.
 */
/**app_policy_membership_card*/
public class AppPolicyMembershipCard {
    private Long id;
    private Long uid;//主店法人ID
    private String membershipName;//会员卡名称
    private String membershipShopName;//会员卡上显示的店铺名称
    private BigDecimal discount;//会员卡折扣(折)
    private Integer isDeposited;//是否储值0不储值 1储值
    private BigDecimal depositAmount;//开卡储值金额
    private Integer isPresentedViaActivate;//是否开卡赠送0否 1是
    private BigDecimal presentAmount;//赠送金额
    private Integer canRecharge;//是否继续充值 0否 1是
    private Integer isPresentedViaRecharge;//是否充值赠送0否 1是
    private BigDecimal rechargeLimitAmount;//单笔充值限额
    private BigDecimal rechargePresentAmount;//单笔充值赠送金额
    private Integer weight;//权重
    private Integer status;//状态1正常2停办
    private Date createTime;
    private Date updateTime;

    private String sids;//店铺ID 以,隔开

    private Integer memberCount;//会员卡数量
    private BigDecimal rechargeSum;//充值总额
    private BigDecimal consumeSum;//消费总值
    private String proportion;//比例
    private String discountInt;
    private String discountFloat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getMembershipName() {
        return membershipName;
    }

    public void setMembershipName(String membershipName) {
        this.membershipName = membershipName;
    }

    public String getMembershipShopName() {
        return membershipShopName;
    }

    public void setMembershipShopName(String membershipShopName) {
        this.membershipShopName = membershipShopName;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getIsDeposited() {
        return isDeposited;
    }

    public void setIsDeposited(Integer isDeposited) {
        this.isDeposited = isDeposited;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public Integer getIsPresentedViaActivate() {
        return isPresentedViaActivate;
    }

    public void setIsPresentedViaActivate(Integer isPresentedViaActivate) {
        this.isPresentedViaActivate = isPresentedViaActivate;
    }

    public BigDecimal getPresentAmount() {
        return presentAmount;
    }

    public void setPresentAmount(BigDecimal presentAmount) {
        this.presentAmount = presentAmount;
    }

    public Integer getIsPresentedViaRecharge() {
        return isPresentedViaRecharge;
    }

    public void setIsPresentedViaRecharge(Integer isPresentedViaRecharge) {
        this.isPresentedViaRecharge = isPresentedViaRecharge;
    }

    public BigDecimal getRechargeLimitAmount() {
        return rechargeLimitAmount;
    }

    public void setRechargeLimitAmount(BigDecimal rechargeLimitAmount) {
        this.rechargeLimitAmount = rechargeLimitAmount;
    }

    public BigDecimal getRechargePresentAmount() {
        return rechargePresentAmount;
    }

    public void setRechargePresentAmount(BigDecimal rechargePresentAmount) {
        this.rechargePresentAmount = rechargePresentAmount;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getSids() {
        return sids;
    }

    public void setSids(String sids) {
        this.sids = sids;
    }

    public Integer getCanRecharge() {
        return canRecharge;
    }

    public void setCanRecharge(Integer canRecharge) {
        this.canRecharge = canRecharge;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public BigDecimal getRechargeSum() {
        return rechargeSum;
    }

    public void setRechargeSum(BigDecimal rechargeSum) {
        this.rechargeSum = rechargeSum;
    }

    public BigDecimal getConsumeSum() {
        return consumeSum;
    }

    public void setConsumeSum(BigDecimal consumeSum) {
        this.consumeSum = consumeSum;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public String getDiscountInt() {
        return discountInt;
    }

    public void setDiscountInt(String discountInt) {
        this.discountInt = discountInt;
    }

    public String getDiscountFloat() {
        return discountFloat;
    }

    public void setDiscountFloat(String discountFloat) {
        this.discountFloat = discountFloat;
    }
}
