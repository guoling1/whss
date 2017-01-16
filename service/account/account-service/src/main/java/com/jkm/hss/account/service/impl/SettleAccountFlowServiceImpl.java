package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.account.dao.SettleAccountFlowDao;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.account.sevice.SettleAccountFlowService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Slf4j
@Service
public class SettleAccountFlowServiceImpl implements SettleAccountFlowService {

    @Autowired
    private SettleAccountFlowDao settleAccountFlowDao;
    @Autowired
    private AccountService accountService;

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
     * @param orderNos
     * @param settleAuditRecordId
     * @return
     */
    @Override
    public int updateSettleAuditRecordIdByOrderNos(final List<String> orderNos, final long settleAuditRecordId) {
        if (CollectionUtils.isEmpty(orderNos)) {
            return 0;
        }
        return this.settleAccountFlowDao.updateSettleAuditRecordIdByOrderNos(orderNos, settleAuditRecordId);
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
     *{@inheritDoc}
     *
     * @param accountId
     * @param orderNo  交易订单号
     * @param changeAmount  变动金额
     * @param remark  备注
     * @param type 变更方向
     */
    @Override
    @Transactional
    public void addSettleAccountFlow(long accountId, String orderNo, BigDecimal changeAmount, String remark, EnumAccountFlowType type) {
        //此时的account已经是可用余额改变的结果
        final Account account = this.accountService.getByIdWithLock(accountId).get();
        final SettleAccountFlow settleAccountFlow = new SettleAccountFlow();
        settleAccountFlow.setAccountId(account.getId());
        settleAccountFlow.setOrderNo(orderNo);
        settleAccountFlow.setType(type.getId());
        if (EnumAccountFlowType.DECREASE.getId() == type.getId()) {
            settleAccountFlow.setOutAmount(changeAmount);
            settleAccountFlow.setIncomeAmount(new BigDecimal("0.00"));
            settleAccountFlow.setBeforeAmount(account.getDueSettleAmount().add(changeAmount));
            settleAccountFlow.setAfterAmount(account.getDueSettleAmount());
        }
        if (EnumAccountFlowType.INCREASE.getId() == type.getId()) {
            settleAccountFlow.setOutAmount(new BigDecimal("0.00"));
            settleAccountFlow.setIncomeAmount(changeAmount);
            settleAccountFlow.setBeforeAmount(account.getDueSettleAmount().subtract(changeAmount));
            settleAccountFlow.setAfterAmount(account.getDueSettleAmount());
        }
        settleAccountFlow.setChangeTime(new Date());
        settleAccountFlow.setRemark(remark);
        this.settleAccountFlowDao.insert(settleAccountFlow);
    }

    /**
     * {@inheritDoc}
     *
     * @param tradeDateList
     * @return
     */
    @Override
    public List<SettleAccountFlow> getMerchantLastWordDayRecord(final List<Date> tradeDateList) {
        return this.settleAccountFlowDao.selectMerchantLastWordDayRecord(tradeDateList);
    }

    /**
     * {@inheritDoc}
     *
     * @param recordId
     * @return
     */
    @Override
    public List<SettleAccountFlow> getByAuditRecordId(final long recordId) {
        return this.settleAccountFlowDao.selectByAuditRecordId(recordId);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @return
     */
    @Override
    @Transactional
    public List<SettleAccountFlow> getDealerOrCompanyFlowByOrderNo(final String orderNo) {
        return this.settleAccountFlowDao.selectDealerOrCompanyFlowByOrderNo(orderNo);
    }
}
