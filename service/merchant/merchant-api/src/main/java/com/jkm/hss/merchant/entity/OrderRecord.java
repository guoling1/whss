package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhangbin on 2016/11/22.
 */
@Data
public class OrderRecord extends BaseEntity {

    /**
     *订单号
     */
    private String orderId;

    /**
     *商户号
     */
    private long merchantId;
    /**
     * 商户类型(0商户 1经销商)
     */
    private int merchantType;

    /**
     *商品名称
     */
    private String productName;

    /**
     *商户名称
     */
    private String subName;

    /**
     *101.微信扫码
     102.支付宝扫码
     103.银联
     */
    private Integer payChannel;

    /**
     *支付金额(元)
     */
    private BigDecimal totalFee;

    /**
     *实际所得
     */
    private BigDecimal realFee;

    /**
     *服务费（金开门收商户的钱）
     */
    private BigDecimal serviceFee;

    /**
     *通道费（支付平台收金开门的钱）
     */
    private BigDecimal channelFee;

    /**
     *支付结果
     * N-待支付/待提现
     * H-处理中
     * S-成功
     * F-失败
     * V-支付异常
     * W-提现等待中
     * E-提现异常
     */
    private String payResult;

    /**
     *D0,T0,T1
     */
    private String settlePeriod;

    /**
     *结算状态 0,已经算 1.未结算
     */
    private int settleStatus;

    /**
     *交易单号
     */
    private String outTradeNo;

    /**
     *支付参数
     */
    private String payParams;

    /**
     *返回参数
     */
    private String resultParams;

    /**
     *错误信息
     */
    private String errorMessage;

    /**
     *支付描述
     */
    private String body;

    /**
     *支付完成时间
     */
    private Date payTime;

    /**
     *0.支付 1.提现
     */
    private int tradeType;

}
