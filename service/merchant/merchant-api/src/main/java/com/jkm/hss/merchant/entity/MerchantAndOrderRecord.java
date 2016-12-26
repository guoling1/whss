package com.jkm.hss.merchant.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Thinkpad on 2016/12/20.
 */
@Data
public class MerchantAndOrderRecord{

    /**
     * 导出地址
     */
    private String url;

    /**
     * 主键id
     */
    private long id;
    /**
     *订单号
     */
    private String orderId;

    /**
     *商户号
     */
    private long merchantId;

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
     *结算状态 0,已经算 1.未结算
     */
    private int settleStatus;

    /**
     *交易单号
     */
    private String outTradeNo;

    /**
     *错误信息
     */
    private String errorMessage;

    /**
     * 查询条件：电话
     */
    private String bankNo;

    /**
     * 查询条件：电话
     */
    private String mobile;

    /**
     * 银行预留手机号
     */
    private String reserveMobile;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 所属上级（所属代理商）
     */
    private String proxyName;

    /**
     * 费率
     */
//    private

    /**
     * 创建时间
     * datetime
     */
    private Date createTime;
    /**
     * 订单状态
     */
    private String orderMessage;

    /**
     * 交易类型
     */
    private int tradeType;

    /**
     * 商户类型
     */
    private int merchantType;

    /**
     * 结算周期
     */
    private String settlePeriod;

    /**
     * 交易完成时间
     */
    private Date payTime;
}
