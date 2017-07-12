package com.jkm.hss.account.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2017/5/4.
 *
 * 分润退款记录
 *
 * tb_split_account_refund_record
 */
@Data
public class SplitAccountRefundRecord extends BaseEntity {

    /**
     * 支付交易单号
     */
    private String payOrderNo;

    /**
     * 退款批次号
     */
    private String batchNo;

    /**
     * 退款订单号
     */
    private String refundOrderNo;

    /**
     * 分账方用户类型
     *
     * {@link com.jkm.hss.account.enums.EnumSplitAccountUserType}
     */
    private int accountUserType;

    /**
     * 原分账流水号
     */
    private String originalSplitSn;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 分润金额
     */
    private BigDecimal splitAmount;

    /**
     * 分账总额
     */
    private BigDecimal splitTotalAmount;

    /**
     * 分账退款流水号（唯一）
     */
    private String splitSn;

    /**
     * 出款账户id
     */
    private long outMoneyAccountId;

    /**
     * 出款用户名
     */
    private String outMoneyUserName;

    /**
     * 收款账户id(hss的手续费账户，内部账户)
     */
    private long receiptMoneyAccountId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 退款时间
     */
    private Date refundTime;

}
