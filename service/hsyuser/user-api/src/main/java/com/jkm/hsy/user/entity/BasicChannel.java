package com.jkm.hsy.user.entity;

/**
 * Created by Allen on 2017/7/3.
 */
public class BasicChannel {
    private Long id;
    private Long accountid;
    private String channelTypeSign;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountid() {
        return accountid;
    }

    public void setAccountid(Long accountid) {
        this.accountid = accountid;
    }

    public String getChannelTypeSign() {
        return channelTypeSign;
    }

    public void setChannelTypeSign(String channelTypeSign) {
        this.channelTypeSign = channelTypeSign;
    }
}
