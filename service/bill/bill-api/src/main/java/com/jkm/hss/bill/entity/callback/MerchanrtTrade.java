package com.jkm.hss.bill.entity.callback;

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
public class MerchanrtTrade {

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
     * 收款人(商户id)
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
     * 是否待支付
     *
     * @return
     */
    public boolean isDuePay() {
        return EnumOrderStatus.DUE_PAY.getId() == this.status;
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
