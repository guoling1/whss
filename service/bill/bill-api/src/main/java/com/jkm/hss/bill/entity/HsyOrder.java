package com.jkm.hss.bill.entity;

import com.jkm.base.common.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wayne on 17/5/15.
 * 好收银业务订单
 */
public class HsyOrder extends BaseEntity {
    /**
     * 订单号
     */
    private String ordernumber;
    private int orderstatus;
    /**
     * 店铺ID
     */
    private long shopid;

    private String shopname;
    /**
     * 交易订单
     */
    private long orderno;
    /**
     * 确认码
     */
    private String validationcode;
    private BigDecimal amount;
    private BigDecimal poundage;
    private String qrcode;
    private int cashierid;
    private String cashiername;
    private String paysn;
    private String paytype;
    private Date paysuccesstime;
    private BigDecimal refundamount;
    private int refundstatus;
    private Date refundtime;
    private String goodsname;
    private String goodsdescribe;
    private String remark;

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
        return orderno;
    }

    public void setOrderid(long orderid) {
        this.orderno = orderid;
    }

    public String getValidationcode() {
        return validationcode;
    }

    public void setValidationcode(String validationcode) {
        this.validationcode = validationcode;
    }

    public int getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(int orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public long getOrderno() {
        return orderno;
    }

    public void setOrderno(long orderno) {
        this.orderno = orderno;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPoundage() {
        return poundage;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public int getCashierid() {
        return cashierid;
    }

    public void setCashierid(int cashierid) {
        this.cashierid = cashierid;
    }

    public String getCashiername() {
        return cashiername;
    }

    public void setCashiername(String cashiername) {
        this.cashiername = cashiername;
    }

    public String getPaysn() {
        return paysn;
    }

    public void setPaysn(String paysn) {
        this.paysn = paysn;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public Date getPaysuccesstime() {
        return paysuccesstime;
    }

    public void setPaysuccesstime(Date paysuccesstime) {
        this.paysuccesstime = paysuccesstime;
    }

    public BigDecimal getRefundamount() {
        return refundamount;
    }

    public void setRefundamount(BigDecimal refundamount) {
        this.refundamount = refundamount;
    }

    public int getRefundstatus() {
        return refundstatus;
    }

    public void setRefundstatus(int refundstatus) {
        this.refundstatus = refundstatus;
    }

    public Date getRefundtime() {
        return refundtime;
    }

    public void setRefundtime(Date refundtime) {
        this.refundtime = refundtime;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getGoodsdescribe() {
        return goodsdescribe;
    }

    public void setGoodsdescribe(String goodsdescribe) {
        this.goodsdescribe = goodsdescribe;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
