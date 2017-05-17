package com.jkm.hss.bill.helper.responseparam;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wayne on 17/5/17.
 * hsy订单列表
 */
@Data
public class HsyTradeListResponse {
    private int id;
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
     * 支付
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
    private String orderNumber;
    /**
     * 确认码
     */
    private String validationCode;
    /**
     * 交易单号
     */
    private String orderNo;
    /**
     * 订单状态
     */
    private String orderstatusName;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 商铺名称
     */
    private String merchantName;

    private String sourceTypeName;
    /**
     * 支付类型 二维码付款/充值等
     */
    private int sourceType;

    public HsyTradeListResponse(){
        this.orderstatusName="";
        this.validationCode="";
        this.orderNumber="";
        this.shopName="";
        this.sourceTypeName="";
        this.amount=new BigDecimal(0);
        this.totalAmount=new BigDecimal(0);
        this.refundAmount=new BigDecimal(0);
    }


}
