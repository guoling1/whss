package com.jkm.hsy.user.entity;

import java.math.BigDecimal;
import java.util.Date;

/** app_au_user*/
public class AppAuUser {
    private Long id;
    private String globalID;//对外ID
    private String cellphone;//手机号
    private String password;//密码
    private String nickname;//昵称
    private String realname;//真实姓名
    private Integer status;//状态 1正常 2待审核 99禁用
    private String profile;//头像
    private String auStep;//步骤
    private String idcard;//身份证号码
    private String idcardf;//身份证正面照
    private String idcardb;//身份证背面照
    private String idcardh;//身份证手持照
    private Long accountID;//账户ID
    private Long dealerID;//代理商ID
    private Long productID;//产品ID
    private BigDecimal weixinRate;//微信费率
    private BigDecimal alipayRate;//支付宝费率
    private BigDecimal fastRate;//快捷费率
    private Long parentID;
    private Integer roleTemp;//临时角色
    private Date createTime;
    private Date updateTime;

    //展示字段
    private String code;//验证码
    private String shopName;//店铺名称
    private String shopShortName;//店铺简称
    private String industryCode;//行业代码
    private String profileURL;//头像URL
    private String idcardfURL;//身份证正面照URL
    private String idcardbURL;//身份证背面照URL
    private String idcardhURL;//身份证手持照URL
    private Integer role;//角色
    private String roleName;//角色名称
    private String deviceID;//设备号
    private Long sid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGlobalID() {
        return globalID;
    }

    public void setGlobalID(String globalID) {
        this.globalID = globalID;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getIdcardf() {
        return idcardf;
    }

    public void setIdcardf(String idcardf) {
        this.idcardf = idcardf;
    }

    public String getIdcardb() {
        return idcardb;
    }

    public void setIdcardb(String idcardb) {
        this.idcardb = idcardb;
    }

    public String getIdcardh() {
        return idcardh;
    }

    public void setIdcardh(String idcardh) {
        this.idcardh = idcardh;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getAuStep() {
        return auStep;
    }

    public void setAuStep(String auStep) {
        this.auStep = auStep;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getIdcardfURL() {
        return idcardfURL;
    }

    public void setIdcardfURL(String idcardfURL) {
        this.idcardfURL = idcardfURL;
    }

    public String getIdcardbURL() {
        return idcardbURL;
    }

    public void setIdcardbURL(String idcardbURL) {
        this.idcardbURL = idcardbURL;
    }

    public String getIdcardhURL() {
        return idcardhURL;
    }

    public void setIdcardhURL(String idcardhURL) {
        this.idcardhURL = idcardhURL;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getDealerID() {
        return dealerID;
    }

    public void setDealerID(Long dealerID) {
        this.dealerID = dealerID;
    }

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public BigDecimal getWeixinRate() {
        return weixinRate;
    }

    public void setWeixinRate(BigDecimal weixinRate) {
        this.weixinRate = weixinRate;
    }

    public BigDecimal getAlipayRate() {
        return alipayRate;
    }

    public void setAlipayRate(BigDecimal alipayRate) {
        this.alipayRate = alipayRate;
    }

    public BigDecimal getFastRate() {
        return fastRate;
    }

    public void setFastRate(BigDecimal fastRate) {
        this.fastRate = fastRate;
    }

    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public Long getParentID() {
        return parentID;
    }

    public void setParentID(Long parentID) {
        this.parentID = parentID;
    }

    public Integer getRoleTemp() {
        return roleTemp;
    }

    public void setRoleTemp(Integer roleTemp) {
        this.roleTemp = roleTemp;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getShopShortName() {
        return shopShortName;
    }

    public void setShopShortName(String shopShortName) {
        this.shopShortName = shopShortName;
    }
}
