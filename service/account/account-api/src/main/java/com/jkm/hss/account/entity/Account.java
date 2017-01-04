package com.jkm.hss.account.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 账户
 *
 * tb_account
 */
@Data
public class Account extends BaseEntity {

    /**
     * 用户名字
     */
    private String userName;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 可用余额
     */
    private BigDecimal available;

    /**
     * 冻结金额
     */
    private BigDecimal frozenAmount;

    /**
     * 待结算金额
     */
    private BigDecimal dueSettleAmount;
}