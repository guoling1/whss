package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.util.DateTimeUtil;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.dao.SettleAccountFlowDao;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.helper.selectresponse.SettleAccountFlowStatistics;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.account.sevice.SettleAccountFlowService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
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
     * @param id
     * @param settlementRecordId
     * @return
     */
    @Override
    @Transactional
    public int updateSettlementRecordIdById(final long id, final long settlementRecordId) {
        return this.settleAccountFlowDao.updateSettlementRecordIdById(id, settlementRecordId);
    }

    /**
     * {@inheritDoc}
     *
     * @param settleAuditRecordId  结算审核记录id
     * @param settlementRecordId  结算单id
     * @return
     */
    @Override
    @Transactional
    public int updateSettlementRecordIdBySettleAuditRecordId(final long settleAuditRecordId, final long settlementRecordId) {
        return this.settleAccountFlowDao.updateSettlementRecordIdBySettleAuditRecordId(settleAuditRecordId, settlementRecordId);
    }

    /**
     * {@inheritDoc}
     *
     * @param settleDate
     * @param accountId
     * @param settleAuditRecordId
     * @return
     */
    @Override
    @Transactional
    public int updateSettleAuditRecordIdBySettleDateAndAccountId(final Date settleDate, final long accountId, final long settleAuditRecordId) {
        return this.settleAccountFlowDao.updateSettleAuditRecordIdBySettleDateAndAccountId(settleDate, accountId, settleAuditRecordId);
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
     * @param accountUserType  账户类型（商户，代理商，公司）
     */
    @Override
    @Transactional
    public long addSettleAccountFlow(long accountId, String orderNo, BigDecimal changeAmount, String remark,
                                     EnumAccountFlowType type, String appId, Date tradeDate, Date settleDate, int accountUserType) {
        //此时的account已经是可用余额改变的结果
        final Account account = this.accountService.getByIdWithLock(accountId).get();
        final SettleAccountFlow settleAccountFlow = new SettleAccountFlow();
        settleAccountFlow.setFlowNo(this.getSettleAccountFlowNo());
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
        settleAccountFlow.setAppId(appId);
        settleAccountFlow.setTradeDate(tradeDate);
        settleAccountFlow.setSettleDate(settleDate);
        settleAccountFlow.setAccountUserType(accountUserType);
        this.settleAccountFlowDao.insert(settleAccountFlow);
        return settleAccountFlow.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param settleDate
     * @return
     */
    @Override
    public List<SettleAccountFlowStatistics> statisticsYesterdayFlow(final Date settleDate) {
        return this.settleAccountFlowDao.statisticsYesterdayFlow(settleDate);
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
    public List<SettleAccountFlow> getByOrderNo(final String orderNo) {
        return this.settleAccountFlowDao.selectByOrderNo(orderNo);
    }

    /**
     * {@inheritDoc}
     *
     * @param settlementRecordId
     * @return
     */
    @Override
    public List<SettleAccountFlow> getBySettlementRecordId(final long settlementRecordId) {
        return this.settleAccountFlowDao.selectBySettlementRecordId(settlementRecordId);
    }

    /**
     * {@inheritDoc}
     *
     * @param settleDate
     * @return
     */
    @Override
    public int getYesterdayDecreaseFlowCount(final Date settleDate) {
        return this.settleAccountFlowDao.selectYesterdayDecreaseFlowCount(settleDate);
    }

    /**
     * {@inheritDoc}
     *
     * @param flowNo
     * @return
     */
    @Override
    public boolean checkExistByFlowNo(final String flowNo) {
        final int count = this.settleAccountFlowDao.selectCountByFlowNo(flowNo);
        return count == 0;
    }

    /**
     * {@inheritDoc}
     *
     * @param recordId
     * @return
     */
    @Override
    public List<String> getOrderNoByAuditRecordId(final long recordId) {
        return this.settleAccountFlowDao.selectOrderNosByRecordId(recordId);
    }

    /**
     * 获取流水号
     *
     * @return
     */
    private String getSettleAccountFlowNo() {
        final String flowNo = SnGenerator.generateFlowSn();
        final boolean check = this.checkExistByFlowNo(flowNo);
        if (!check) {
            return this.getSettleAccountFlowNo();
        }
        return flowNo;
    }
}
