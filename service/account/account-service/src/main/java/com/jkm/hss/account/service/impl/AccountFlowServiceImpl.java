package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.account.dao.AccountFlowDao;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.AccountFlow;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.sevice.AccountFlowService;
import com.jkm.hss.account.sevice.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Slf4j
@Service
public class AccountFlowServiceImpl implements AccountFlowService {

    @Autowired
    private AccountFlowDao accountFlowDao;
    @Autowired
    private AccountService accountService;

    /**
     * {@inheritDoc}
     *
     * @param accountFlow
     */
    @Override
    public void add(final AccountFlow accountFlow) {
        this.accountFlowDao.insert(accountFlow);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<AccountFlow> getById(final long id) {
        return Optional.fromNullable(this.accountFlowDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @param accountId
     * @param type
     * @return
     */
    @Override
    public Optional<AccountFlow> getByOrderNoAndAccountIdAndType(String orderNo, long accountId, int type) {
        return Optional.fromNullable(this.accountFlowDao.selectByOrderNoAndAccountIdAndType(orderNo, accountId, type));
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @param orderNo  交易订单号
     * @param changeAmount  变动金额
     * @param remark  备注
     */
    @Override
    public void addAccountFlow(final long accountId, final String orderNo, final BigDecimal changeAmount,
                               final String remark, EnumAccountFlowType type) {
        //此时的account已经是可用余额改变的结果
        final Account account = this.accountService.getByIdWithLock(accountId).get();
        final AccountFlow accountFlow = new AccountFlow();
        accountFlow.setAccountId(account.getId());
        accountFlow.setOrderNo(orderNo);
        accountFlow.setType(type.getId());
        if (EnumAccountFlowType.DECREASE.getId() == type.getId()) {
            accountFlow.setOutAmount(changeAmount);
            accountFlow.setBeforeAmount(account.getAvailable().add(changeAmount));
            accountFlow.setAfterAmount(account.getAvailable());
        }
        if (EnumAccountFlowType.INCREASE.getId() == type.getId()) {
            accountFlow.setIncomeAmount(changeAmount);
            accountFlow.setBeforeAmount(account.getAvailable().subtract(changeAmount));
            accountFlow.setAfterAmount(account.getAvailable());
        }
        accountFlow.setChangeTime(new Date());
        accountFlow.setRemark(remark);
        this.accountFlowDao.insert(accountFlow);
    }
}
