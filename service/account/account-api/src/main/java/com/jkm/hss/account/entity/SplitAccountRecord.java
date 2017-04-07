package com.jkm.hss.account.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 一笔订单的分账记录
 *
 * tb_split_account_record
 *
 */
@Data
public class SplitAccountRecord extends BaseEntity {

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
     * 分账方用户类型
     *
     * {@link com.jkm.hss.account.enums.EnumSplitAccountUserType}
     */
    private int accountUserType;

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
    private BigDecimal splitTotalAmount;

    /**
     * 分账流水号（唯一）
     */
    private String splitSn;

    /**
     * 出款账户id(hss的手续费账户，内部账户)
     */
    private long outMoneyAccountId;

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
    private BigDecimal splitAmount;

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
}
