package com.jkm.hss.account.helper.selectresponse;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/2/24.
 */
@Data
public class SettleAccountFlowStatistics {

    /**
     * 账户类型
     *
     * {@link com.jkm.hss.account.enums.EnumAccountUserType}
     */
    private int accountUserType;

    /**
     * 账户id
     */
    private long accountId;

    /**
     * 交易笔数
     */
    private int count;

    /**
     * 交易总额
     */
    private BigDecimal amount;
}
