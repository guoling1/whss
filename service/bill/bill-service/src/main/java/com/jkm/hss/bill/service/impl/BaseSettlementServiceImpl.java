package com.jkm.hss.bill.service.impl;

import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.account.sevice.SettleAccountFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2017/5/17.
 */
@Slf4j
@Service
public class BaseSettlementServiceImpl implements BaseSettlementService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private SettleAccountFlowService settleAccountFlowService;

    /**
     * {@inheritDoc}
     *
     * @param settleAccountFlow
     * @param account
     * @param remark
     */
    @Override
    @Transactional
    public long pendingSettleAccountFlowOutAccount(SettleAccountFlow settleAccountFlow, Account account, String remark) {
        this.accountService.decreaseTotalAmount(account.getId(), settleAccountFlow.getIncomeAmount());
        this.accountService.decreaseSettleAmount(account.getId(), settleAccountFlow.getIncomeAmount());
        final SettleAccountFlow decreaseSettleAccountFlow = new SettleAccountFlow();
        decreaseSettleAccountFlow.setFlowNo("");
        decreaseSettleAccountFlow.setAccountId(settleAccountFlow.getAccountId());
        decreaseSettleAccountFlow.setAccountUserType(settleAccountFlow.getAccountUserType());
        decreaseSettleAccountFlow.setOrderNo(settleAccountFlow.getOrderNo());
        decreaseSettleAccountFlow.setRefundOrderNo(settleAccountFlow.getRefundOrderNo());
        decreaseSettleAccountFlow.setOutAmount(settleAccountFlow.getIncomeAmount());
        decreaseSettleAccountFlow.setIncomeAmount(new BigDecimal("0.00"));
        decreaseSettleAccountFlow.setBeforeAmount(account.getDueSettleAmount());
        decreaseSettleAccountFlow.setAfterAmount(account.getDueSettleAmount().subtract(settleAccountFlow.getIncomeAmount()));
        decreaseSettleAccountFlow.setAppId(settleAccountFlow.getAppId());
        decreaseSettleAccountFlow.setTradeDate(settleAccountFlow.getTradeDate());
        decreaseSettleAccountFlow.setSettleDate(settleAccountFlow.getSettleDate());
        decreaseSettleAccountFlow.setChangeTime(new Date());
        decreaseSettleAccountFlow.setType(EnumAccountFlowType.DECREASE.getId());
        decreaseSettleAccountFlow.setRemark(remark);
        this.settleAccountFlowService.add(decreaseSettleAccountFlow);
        this.settleAccountFlowService.updateStatus(settleAccountFlow.getId(), EnumBoolean.TRUE.getCode());
        return decreaseSettleAccountFlow.getId();
    }
}
