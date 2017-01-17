package com.jkm.hsy.user.entity;

import java.util.Date;

/** app_au_user*/
public class AppAuUser {
    private long id;
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
    private Date createTime;
    private Date updateTime;

    //展示字段
    private String code;//验证码
    private String shopName;//店铺名称
    private String industryCode;//行业代码
    private String profileURL;//头像URL
    private String idcardfURL;//身份证正面照URL
    private String idcardbURL;//身份证背面照URL
    private String idcardhURL;//身份证手持照URL
    private Long role;//角色
    private String roleName;//角色名称

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
