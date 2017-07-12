package com.jkm.hsy.user.entity;

/**
 * Created by Allen on 2017/5/12.
 */
public class AppPolicyMembershipCardShop {
    private Long mcid;//会员卡ID
    private Long sid;//店铺ID

    public Long getMcid() {
        return mcid;
    }

    public void setMcid(Long mcid) {
        this.mcid = mcid;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }
}
