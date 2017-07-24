package com.jkm.hss.bill.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.bill.enums.EnumOrderStatus;
import com.jkm.hss.bill.enums.EnumWithdrawOrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yuxiang on 2017-07-24.
 *
 * {@link com.jkm.hss.bill.enums.EnumWithdrawOrderStatus}
 */
@Data
public class WithdrawOrder extends BaseEntity{

    private String appId;

    /**
     * 提现交易单号
     */
    private String orderNo;

    /**
     * 提现流水号
     */
    private String sn;

    /**
     * 结算单id
     */
    private long settlementOrderId;

    /**
     * 提现单对应的交易金额
     */
    private BigDecimal tradeAmount;

    /**
     * 提现金额
     */
    private BigDecimal withdrawAmount;

    /**
     * 提现用户类型
     *
     * {@link com.jkm.hss.bill.enums.EnumWithdrawUserType}
     */
    private int withdrawUserType;

    /**
     * 提现用户编号
     */
    private String withdrawUserNo;

    /**
     * 提现用户编号
     */
    private String withdrawUserName;

    /**
     * 提现用户资金编号
     */
    private long withdrawUserAccountId;

    /**
     * 提现用户使用通道
     *
     * {@link com.jkm.hss.product.enums.EnumUpperChannel}
     */
    private int upperChannel;

    /**
     * 提现用户s申请时间
     */
    private Date applyTime;

    /**
     * 提现用户s成功时间
     */
    private Date applySuccessTime;

    /**
     * 提现用户手续费
     */
    private BigDecimal poundage;

    /**
     * 提现单对应的 交易单 s
     */
    private String tradeOrders;

    /**
     *  银行名字
     */
    private String bankName;

    /**
     *  银行卡号
     */
    private String bankCardNo;

    private String remarks;

    /**
     * 是否提现中
     *
     * @return
     */
    public boolean isWithDrawing() {
        return EnumWithdrawOrderStatus.WITHDRAWING.getCode() == this.status || EnumWithdrawOrderStatus.WITHDRAW_WAIT.getCode() == this.status;
    }

    /**
     * 是否提现成功
     *
     * @return
     */
    public boolean isWithdrawSuccess() {
        return EnumWithdrawOrderStatus.WITHDRAW_SUCCESS.getCode() == this.status;
    }

    public boolean isWithDrawComplete() {
        return EnumWithdrawOrderStatus.WITHDRAW_SUCCESS.getCode() == this.status ||
                EnumWithdrawOrderStatus.WITHDRAW_FAIL.getCode()== this.status;
    }
}
