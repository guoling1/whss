package com.jkm.hsy.user.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Allen on 2017/5/12.
 */
/**app_policy_member*/
public class AppPolicyMember {
    private Long id;
    private Long cid;//消费者ID
    private Long mcid;//卡ID
    private Long accountID;//账户ID
    private String memberCardNO;//会员卡号
    private Integer status;//状态1正常
    private Date createTime;
    private Date updateTime;

    private Long uid;//主店ID
    private String consumerCellphone;//消费者手机号
    private String userID;//支付宝ID
    private String openID;//微信ID
    private String membershipName;//会员卡名称
    private String membershipShopName;//会员卡上显示的店铺名称
    private BigDecimal discount;//会员卡折扣(折)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public String getMemberCardNO() {
        return memberCardNO;
    }

    public void setMemberCardNO(String memberCardNO) {
        this.memberCardNO = memberCardNO;
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

    public Long getMcid() {
        return mcid;
    }

    public void setMcid(Long mcid) {
        this.mcid = mcid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getConsumerCellphone() {
        return consumerCellphone;
    }

    public void setConsumerCellphone(String consumerCellphone) {
        this.consumerCellphone = consumerCellphone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOpenID() {
        return openID;
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

    public void setOpenID(String openID) {
        this.openID = openID;

    }
}
