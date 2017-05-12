package com.jkm.hsy.user.entity;

import java.util.Date;

/**
 * Created by Allen on 2017/5/12.
 */
/**app_policy_member*/
public class AppPolicyMember {
    private Long id;
    private Long accountID;//账户ID
    private String memberCellphone;//会员手机号
    private String memberCardNO;//会员卡号
    private String userID;//支付宝ID
    private String openID;//微信ID
    private String payPassword;//支付密码
    private Integer status;//状态
    private Date createTime;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public String getMemberCellphone() {
        return memberCellphone;
    }

    public void setMemberCellphone(String memberCellphone) {
        this.memberCellphone = memberCellphone;
    }

    public String getMemberCardNO() {
        return memberCardNO;
    }

    public void setMemberCardNO(String memberCardNO) {
        this.memberCardNO = memberCardNO;
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

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
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
}
