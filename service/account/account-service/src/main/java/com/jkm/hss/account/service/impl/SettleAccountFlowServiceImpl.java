package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.account.dao.SettleAccountFlowDao;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.sevice.SettleAccountFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Slf4j
@Service
public class SettleAccountFlowServiceImpl implements SettleAccountFlowService {

    private SettleAccountFlowDao settleAccountFlowDao;

    /**
     * {@inheritDoc}
     *
     * @param accountFlow
     */
    @Override
    public void add(final SettleAccountFlow accountFlow) {
        this.settleAccountFlowDao.insert(accountFlow);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<SettleAccountFlow> getById(final long id) {
        return Optional.fromNullable(this.settleAccountFlowDao.selectById(id));
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
    public Optional<SettleAccountFlow> getByOrderNoAndAccountIdAndType(final String orderNo, final long accountId, final int type) {
        return Optional.fromNullable(this.settleAccountFlowDao.selectByOrderNoAndAccountIdAndType(orderNo, accountId, type));
    }

    /**
     *
     * @param account
     * @param orderNo  交易订单号
     * @param changeAmount  变动金额
     * @param remark  备注
     * @param type 变更方向
     */
    @Override
    public void addSettleAccountFlow(Account account, String orderNo, BigDecimal changeAmount, String remark, EnumAccountFlowType type) {
        final SettleAccountFlow settleAccountFlow = new SettleAccountFlow();
        settleAccountFlow.setAccountId(account.getId());
        settleAccountFlow.setOrderNo(orderNo);
        settleAccountFlow.setBeforeAmount(account.getAvailable());
        settleAccountFlow.setType(type.getId());
        if (EnumAccountFlowType.DECREASE.getId() == type.getId()) {
            settleAccountFlow.setOutAmount(changeAmount);
        }
        if (EnumAccountFlowType.INCREASE.getId() == type.getId()) {
            settleAccountFlow.setIncomeAmount(changeAmount);
        }
        settleAccountFlow.setAfterAmount(account.getAvailable().add(changeAmount));
        settleAccountFlow.setChangeTime(new Date());
        settleAccountFlow.setRemark(remark);
        this.settleAccountFlowDao.insert(settleAccountFlow);
    }
}
