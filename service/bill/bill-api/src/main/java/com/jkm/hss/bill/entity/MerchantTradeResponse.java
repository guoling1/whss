package com.jkm.hss.bill.entity;

import com.jkm.hss.bill.enums.EnumOrderStatus;
import com.jkm.hss.bill.enums.EnumSettleStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 交易订单
 *
 * tb_order
 *
 * {@link EnumOrderStatus}
 */
@Data
public class MerchantTradeResponse {

    /**
     * 商户id
     */
    private long id;

    /**
     * 身份证号
     */
    private String identity;

    /**
     * 交易日期
     */
    private Date createTime;

    /**
     * 一级代理商id
     */
    private long firstLevelDealerId;

    /**
     * 一级代理商名
     */
    private String proxyName;

    /**
     * 二级代理商名
     */
    private String proxyName1;

    /**
     * 代理商id
     */
    private long dealerId;

    /**
     * 状态
     * tinyint
     */
    protected int status;

    /**
     * 如果是提现单 ，此值表示 对应支付单的id
     */
    private long payOrderId;

    /**
     * 交易订单号
     */
    private String orderNo;

    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;

    /**
     * 实付金额（未来可能有红包）
     */
    private BigDecimal realPayAmount;

    /**
     * 交易类型
     *
     * {@link com.jkm.hss.bill.enums.EnumTradeType}
     */
    private int tradeType;

    /**
     * 付款人
     */
    private long payer;

    /**
     * 收款人(账户id)
     */
    private long payee;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 付款账户（支付宝，微信，银行）目前只有银行卡
     *
     */
    private String payAccount;

    /**
     * 付款账户类型(目前为空)
     */
    private String payAccountType;

    /**
     * 支付方式
     *
     * {@link com.jkm.hss.bill.enums.EnumPaymentType}
     */
    private String payType;

    /**
     * 付款成功时间（交易成功）
     */
    private Date paySuccessTime;

    /**
     * 手续费
     */
    private BigDecimal poundage;

    /**
     * 支付费率（只有支付时，有费率，提现没有）
     */
    private BigDecimal payRate;

    /**
     * 商品名字（好收收店铺名）
     */
    private String goodsName;

    /**
     * 商品描述（好收收店铺名）
     */
    private String goodsDescribe;

    /**
     * 结算状态
     *
     * {@link EnumSettleStatus}
     */
    private int settleStatus;

    /**
     * 结算时间(预计结算时间)
     */
    private Date settleTime;

    /**
     * 成功结算时间
     */
    private Date successSettleTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 支付渠道标识（101， 102， 103）
     *
     * {@link com.jkm.hss.product.enums.EnumPayChannelSign}
     */
    private int payChannelSign;

    /**
     * 绑卡姓名
     */
    private String name;

    /**
     * 注册手机号
     */
    private String mobile;

    /**
     * 银行卡号
     */
    private String bankNo;

    /**
     * 银行预留手机号
     */
    private String reserveMobile;

    /**
     * 注册时间
     */
    private Date createTimes;

}
