package com.jkm.hss.account.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 可用余额变动 记录
 *
 * tb_account_flow
 *
 */
@Data
public class AccountFlow extends BaseEntity {

    /**
     * 账户id
     */
    private long accountId;

    /**
     * 业务号（交易号）
     */
    private String orderNo;

    /**
     * 发生前余额
     */
    private BigDecimal beforeAmount;

    /**
     * 发生后余额
     */
    private BigDecimal afterAmount;

    /**
     * 支出金额
     */
    private BigDecimal outAmount;

    /**
     * 收入金额
     */
    private BigDecimal incomeAmount;

    /**
     * 发生时间
     */
    private Date changeTime;

    /**
     * 类型
     *
     * {@link EnumAccountFlowType}
     */
    private int type;

    /**
     * 备注
     */
    private String remark;
}
