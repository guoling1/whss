package com.jkm.hss.account.service.impl;

import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.dao.SplitAccountRefundRecordDao;
import com.jkm.hss.account.entity.SplitAccountRefundRecord;
import com.jkm.hss.account.sevice.SplitAccountRefundRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/5/4.
 */
@Slf4j
@Service
public class SplitAccountRefundRecordServiceImpl implements SplitAccountRefundRecordService{

    @Autowired
    private SplitAccountRefundRecordDao splitAccountRefundRecordDao;

    @Override
    @Transactional
    public void add(final SplitAccountRefundRecord splitAccountRefundRecord) {
        splitAccountRefundRecord.setSplitSn(this.getSplitSn());
        this.splitAccountRefundRecordDao.insert(splitAccountRefundRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @param refundOrderNo
     * @return
     */
    @Override
    public List<SplitAccountRefundRecord> getByRefundOrderNo(final String refundOrderNo) {
        return this.splitAccountRefundRecordDao.selectByRefundOrderNo(refundOrderNo);
    }

    /**
     * {@inheritDoc}
     *
     * @param payOrderNo
     * @return
     */
    @Override
    public List<SplitAccountRefundRecord> getByPayOrderNo(final String payOrderNo) {
        return this.splitAccountRefundRecordDao.selectByPayOrderNo(payOrderNo);
    }

    /**
     * {@inheritDoc}
     *
     * @param splitSn
     * @return
     */
    @Override
    public boolean checkExistBySplitSn(final String splitSn) {
        final int count = this.splitAccountRefundRecordDao.selectCountBySplitSn(splitSn);
        return count == 0;
    }

    /**
     * 获取分账单流水号
     *
     * @return
     */
    private String getSplitSn() {
        final String splitSn = SnGenerator.generateFlowSn();
        final boolean check = this.checkExistBySplitSn(splitSn);
        if (!check) {
            return this.getSplitSn();
        }
        return splitSn;
    }
}
