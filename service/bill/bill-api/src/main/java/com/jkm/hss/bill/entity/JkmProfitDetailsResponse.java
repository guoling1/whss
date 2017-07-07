package com.jkm.hss.bill.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 分润明细
 */


@Data
public class JkmProfitDetailsResponse {

    private int payChannelSign;

    /**
     * 结算周期
     */
    private String settleType;

    /**
     * 账户id
     */
    private long ids;

    /**
     * 代理商级别
     */
    private int level;

    /**
     * 分润方类型
     */
    private String profitType;

    /**
     * 分润方类型
     */
    private int accountUserType;

    /**
     * yu
     * 业务类型
     * {@link com.jkm.hss.account.enums.EnumSplitBusinessType}
     */
    private String businessType;

    /**
     * 交易订单号
     */
    private String orderNo;

    /**
     * 分账单号（为了支持多次分账， 现在可以和交易单号保持一致）
     */
    private String splitOrderNo;

    /**
     * 交易总额
     */
    private BigDecimal totalAmount;

    /**
     * 分账总额
     */
    private String splitTotalAmount;

    /**
     * 分账流水号（唯一）
     */
    private String splitSn;

    /**
     * 出款账户id(hss的手续费账户，内部账户)
     */
    private long outMoneyAccountId;

    /**
     * 出款账户名称
     */
    private String outMoneyAccountName;

    /**
     * 收款账户id
     */
    private long receiptMoneyAccountId;

    /**
     * 收款用户名
     */
    private String receiptMoneyUserName;

    /**
     * 分账所得金额
     */
    private String splitAmount;

    /**
     * 分账费率
     */
    private BigDecimal splitRate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 分账日期
     */
    private Date splitDate;

    private String splitDates;
}
