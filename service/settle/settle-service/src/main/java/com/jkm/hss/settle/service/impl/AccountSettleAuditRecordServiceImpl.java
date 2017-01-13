package com.jkm.hss.settle.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.sevice.SettleAccountFlowService;
import com.jkm.hss.settle.dao.AccountSettleAuditRecordDao;
import com.jkm.hss.settle.entity.AccountSettleAuditRecord;
import com.jkm.hss.settle.service.AccountSettleAuditRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/1/12.
 */
@Slf4j
@Service
public class AccountSettleAuditRecordServiceImpl implements AccountSettleAuditRecordService {

    @Autowired
    private SettleAccountFlowService settleAccountFlowService;
    @Autowired
    private AccountSettleAuditRecordDao accountSettleAuditRecordDao;
    /**
     * {@inheritDoc}
     *
     * @param record
     */
    @Override
    public void add(final AccountSettleAuditRecord record) {
        this.accountSettleAuditRecordDao.insert(record);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public int updateAccountCheckStatus(final long id, final int status) {
        return this.accountSettleAuditRecordDao.updateAccountCheckStatus(id, status);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public int updateSettleStatus(final long id, final int status) {
        return this.accountSettleAuditRecordDao.updateSettleStatus(id, status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleT1SettleTask() {
        final List<Date> tradeDateList = new ArrayList<>();
        final DateTime now = DateTime.now();
        tradeDateList.add(now.minusDays(1).toDate());
        Preconditions.checkState(!(6 == now.dayOfWeek().get() || 7 == now.dayOfWeek().get()), "T1结算定时任务，在非法的日期启动");
        if (1 == now.dayOfWeek().get()) {
            tradeDateList.add(now.minusDays(2).toDate());
            tradeDateList.add(now.minusDays(3).toDate());
        }
        final List<SettleAccountFlow> settleAccountFlows = this.settleAccountFlowService.getLastWordDayRecord(tradeDateList);
        if (!CollectionUtils.isEmpty(settleAccountFlows)) {

        }
    }
}
