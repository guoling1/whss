package com.jkm.hss.bill.helper.responseparam;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wayne on 17/5/17.
 */
public class HsyTradeListResponse {
    private int canRefund;
    /**
     * 笔数
     */
    private int number;
    /**
     * 金额
     */
    private BigDecimal totalAmount;
    /**
     * 退款状态
     */
    private int refundStatus;
    /**
     * 退款状态
     */
    private String refundStatusValue;
    /**
     * 交易类型
     */
    private int tradeType;
    /**
     * 支付类型
     * com.jkm.hss.product.enums.EnumPaymentChannel
     */
    private int channel;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 创建时间
     */
    private Date time;
    /**
     * 订单号
     */
    private String ordernumber;
    /**
     * 确认码
     */
    private String confirmcode;
    /**
     * 交易单号
     */
    private String orderno;
    /**
     * 订单状态
     */
    private String orderstatusname;
    /**
     * 退款金额
     */
    private BigDecimal refundamount;

    public int getCanRefund() {
        return canRefund;
    }

    public void setCanRefund(int canRefund) {
        this.canRefund = canRefund;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRefundStatusValue() {
        return refundStatusValue;
    }

    public void setRefundStatusValue(String refundStatusValue) {
        this.refundStatusValue = refundStatusValue;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getConfirmcode() {
        return confirmcode;
    }

    public void setConfirmcode(String confirmcode) {
        this.confirmcode = confirmcode;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getOrderstatusname() {
        return orderstatusname;
    }

    public void setOrderstatusname(String orderstatusname) {
        this.orderstatusname = orderstatusname;
    }

    public BigDecimal getRefundamount() {
        return refundamount;
    }

    public void setRefundamount(BigDecimal refundamount) {
        this.refundamount = refundamount;
    }
}
