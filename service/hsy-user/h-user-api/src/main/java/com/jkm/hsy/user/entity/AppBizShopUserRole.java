package com.jkm.hsy.user.entity;

/**app_biz_shop_user_role*/
public class AppBizShopUserRole {
    private Long sid;//店铺ID
    private Long uid;//用户ID
    private Integer role;//角色 1法人 2店长 3店员  4财务
    private Integer status;//状态：1 正常  99禁用

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
