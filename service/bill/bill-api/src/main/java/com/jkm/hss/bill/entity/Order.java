package com.jkm.hss.bill.entity;

import com.jkm.base.common.entity.BaseEntity;
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
 * {@link com.jkm.hss.bill.enums.EnumOrderStatus}
 */
@Data
public class Order extends BaseEntity {

    /**
     * 业务线
     *
     * {@link com.jkm.hss.account.enums.EnumAppType}
     */
    private String appId;

    /**
     * 业务类型
     *
     * {@link com.jkm.hss.bill.enums.EnumServiceType}
     */
    private int serviceType;

    /**
     * 如果是提现单 ，此值表示 对应支付单的id
     */
    private long payOrderId;

    /**
     * 业务订单号（下游）
     */
    private String businessOrderNo;

    /**
     * 交易订单号
     */
    private String orderNo;

    /**
     * 流水号
     */
    private String sn;

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
     * 付款人(accountId)
     */
    private long payer;

    /**
     * 收款人(accountId)
     */
    private long payee;

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
     * 对账状态(1:已对账， 0未对账)
     *
     * {@link com.jkm.base.common.enums.EnumBoolean}
     */
    private int checkedStatus;

    /**
     * 结算状态
     *
     * {@link com.jkm.hss.bill.enums.EnumSettleStatus}
     */
    private int settleStatus;

    /**
     * 结算时间(预计结算时间)
     */
    private Date settleTime;

    /**
     * 结算类型
     *
     * {@link com.jkm.hss.product.enums.EnumBalanceTimeType}
     */
    private String settleType;

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
     * 是否待支付
     *
     * @return
     */
    public boolean isDuePay() {
        return EnumOrderStatus.DUE_PAY.getId() == this.status;
    }

    /**
     * 是否支付中
     *
     * @return
     */
    public boolean isPaying() {
        return EnumOrderStatus.PAYING.getId() == this.status;
    }

    /**
     * 是否待支付成功
     *
     * @return
     */
    public boolean isPaySuccess() {
        return EnumOrderStatus.PAY_SUCCESS.getId() == this.status;
    }

    /**
     * 是否提现中
     *
     * @return
     */
    public boolean isWithDrawing() {
        return EnumOrderStatus.WITHDRAWING.getId() == this.status;
    }

    /**
     * 是否提现成功
     *
     * @return
     */
    public boolean isWithdrawSuccess() {
        return EnumOrderStatus.WITHDRAW_SUCCESS.getId() == this.status;
    }

    /**
     * 是否是待结算
     *
     * @return
     */
    public boolean isDueSettle() {
        return EnumSettleStatus.DUE_SETTLE.getId() == this.settleStatus;
    }

    /**
     * 是否是结算中
     *
     * @return
     */
    public boolean isSettleing() {
        return EnumSettleStatus.SETTLE_ING.getId() == this.settleStatus;
    }

    /**
     * 是否是已结算
     *
     * @return
     */
    public boolean isSettled() {
        return EnumSettleStatus.SETTLED.getId() == this.settleStatus;
    }

}
