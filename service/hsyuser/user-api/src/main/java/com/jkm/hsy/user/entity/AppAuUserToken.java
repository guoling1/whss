package com.jkm.hsy.user.entity;

import java.util.Date;

/**
 * Created by Allen on 2017/2/8.
 */
public class AppAuUserToken {
    private Long uid;
    private Long tid;
    private Integer status;//登录状态1登录 2未登录
    private Date loginTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
}
