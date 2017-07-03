package com.jkm.hsy.user.entity;

/**
 * Created by Allen on 2017/2/23.
 */
public class AdminUser {
    private Long id;
    private Integer status;
    /**登录名*/
    private String username;
    /**密码*/
    private String password;
    /**密码hash salt*/
    private String salt;
    /**用户姓名*/
    private String realname;
    /**邮箱*/
    private String email;
    /**手机号*/
    private String mobile;
    private Integer type;
    private Long dealID;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getDealID() {
        return dealID;
    }

    public void setDealID(Long dealID) {
        this.dealID = dealID;
    }
}
