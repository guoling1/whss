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
    private String idcard;//身份证号码
    private String idcardf;//身份证正面照
    private String idcardb;//身份证背面照
    private String idcardh;//身份证手持照
    private Date createTime;
    private Date updateTime;

    //展示字段
    private String code;//验证码

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
}
