package com.jkm.hss.merchant.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-12-14 12:04
 */
@Data
public class OrderRecordAndMerchant {
    /**
     * 主键id
     */
    protected long id;
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
     * 订单状态
     */
    private String orderMessage;

    /**
     * 交易类型
     */
    private int tradeType;

    /**
     *商品名称
     */
    private String productName;
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
     *商户名称
     */
    private String subName;

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
     *交易单号
     */
    private String outTradeNo;

    /**
     *结算状态 0,已经算 1.未结算
     */
    private int settleStatus;

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
     * 结算周期
     */
    private String settlePeriod;

    /**
     * 创建时间
     * datetime
     */
    protected Date createTime;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 所属上级（所属代理商）
     */
    private String proxyName;

    /**
     * 修改时间
     * timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
     */
    protected Date updateTime;

    /**
     * 预留手机号
     */
    private String reserveMobile;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 地址
     */
    private String address;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String identity;


    /**
     * 银行卡名称
     */
    private String bankName;

    /**
     * 结算卡
     */
    private String bankNo;

    /**
     * 注册手机号
     */
    private String mobile;
    /**
     * 加密手机号
     */
    private String mdMobile;
    /**
     * 银行卡简写
     */
    private String bankNoShort;
}
