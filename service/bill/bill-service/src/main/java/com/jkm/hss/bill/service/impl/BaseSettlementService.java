package com.jkm.hss.bill.service.impl;

import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.SettleAccountFlow;

/**
 * Created by yulong.zhang on 2017/5/17.
 */
public interface BaseSettlementService {
    /**
     * 待结算流水出账
     *
     * @param settleAccountFlow
     * @param account
     * @param remark
     */
    long pendingSettleAccountFlowOutAccount(SettleAccountFlow settleAccountFlow, Account account, String remark);
}
