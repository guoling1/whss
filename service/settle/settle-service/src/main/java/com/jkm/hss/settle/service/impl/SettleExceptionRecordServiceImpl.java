package com.jkm.hss.settle.service.impl;

import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.bill.entity.WithdrawOrder;
import com.jkm.hss.bill.enums.EnumWithdrawOrderStatus;
import com.jkm.hss.bill.helper.requestparam.QuerySettlementRecordParams;
import com.jkm.hss.bill.service.WithdrawOrderService;
import com.jkm.hss.settle.dao.SettleExceptionRecordDao;
import com.jkm.hss.settle.entity.SettleExceptionRecord;
import com.jkm.hss.settle.service.SettleExceptionRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yuxiang on 2017-07-21.
 */
@Service
@Slf4j
public class SettleExceptionRecordServiceImpl implements SettleExceptionRecordService {

    @Autowired
    private SettleExceptionRecordDao settleExceptionRecordDao;
    @Autowired
    private WithdrawOrderService withdrawOrderService;

    @Override
    public void insert(SettleExceptionRecord settleExceptionRecord) {

        this.settleExceptionRecordDao.insert(settleExceptionRecord);
    }

    @Override
    public void update(SettleExceptionRecord settleExceptionRecord) {
        this.settleExceptionRecordDao.update(settleExceptionRecord);
    }

    @Override
    public PageModel<SettleExceptionRecord> listSettlementRecordByParam(QuerySettlementRecordParams querySettlementRecordParams) {
        final PageModel<SettleExceptionRecord> result = new PageModel<>(querySettlementRecordParams.getPageNo(), querySettlementRecordParams.getPageSize());
        querySettlementRecordParams.setOffset(result.getFirstIndex());
        querySettlementRecordParams.setCount(result.getPageSize());
        querySettlementRecordParams.setStartDate(querySettlementRecordParams.getStartDate() + " 00:00:00");
        querySettlementRecordParams.setEndDate(querySettlementRecordParams.getEndDate() + " 23:59:59");
        final int count = this.settleExceptionRecordDao.selectCountByParam(querySettlementRecordParams);
        final List<SettleExceptionRecord> records = this.settleExceptionRecordDao.selectByParam(querySettlementRecordParams);
        result.setCount(count);
        result.setRecords(records);
        return result;
    }

    @Override
    public SettleExceptionRecord selectByIdWithLock(long id) {
        return this.settleExceptionRecordDao.selectByIdWithLock(id);
    }

    @Transactional
    @Override
    public Pair<Integer, String> handleRecord(long settleExceptionId) {
        SettleExceptionRecord settleExceptionRecord =
                this.selectByIdWithLock(settleExceptionId);
        //校验结算单对应的提现状态
        final WithdrawOrder withdrawOrder = this.withdrawOrderService.selectByIdWithlock(settleExceptionRecord.getWithdrawOrderId());
        if (withdrawOrder.isWithDrawComplete()){
            //对挂起订单进行结算

        }
        return Pair.of(-1, "处理失败,存在未处理的提现订单");
    }
}
