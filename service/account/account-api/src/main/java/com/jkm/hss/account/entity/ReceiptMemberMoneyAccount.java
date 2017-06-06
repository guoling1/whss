package com.jkm.hss.account.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/5/10.
 *
 * 商户收会员款账户
 *
 * tb_receipt_member_money_account
 */
@Data
public class ReceiptMemberMoneyAccount extends BaseEntity {

    /**
     * 充值总金额
     */
    private BigDecimal rechargeTotalAmount;

    /**
     * 收入总金额
     */
    private BigDecimal incomeToTalAmount;
}
