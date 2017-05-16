package com.jkm.hss.bill.entity;

import com.jkm.base.common.entity.BaseEntity;

/**
 * Created by wayne on 17/5/15.
 * 好收银业务订单
 */
public class HsyOrder extends BaseEntity {
    /**
     * 订单号
     */
    private String ordernumber;
    /**
     * 店铺ID
     */
    private long shopid;
    /**
     * 交易订单ID
     */
    private long orderid;
    /**
     * 确认码
     */
    private String validationcode;

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public long getShopid() {
        return shopid;
    }

    public void setShopid(long shopid) {
        this.shopid = shopid;
    }

    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }

    public String getValidationcode() {
        return validationcode;
    }

    public void setValidationcode(String validationcode) {
        this.validationcode = validationcode;
    }
}
