package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.dao.SplitAccountRecordDao;
import com.jkm.hss.account.entity.SplitAccountRecord;
import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.account.sevice.SplitAccountRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Slf4j
@Service
public class SplitAccountRecordServiceImpl implements SplitAccountRecordService {

    @Autowired
    private SplitAccountRecordDao splitAccountRecordDao;

    /**
     * {@inheritDoc}
     *
     * @param record
     */
    @Override
    public void add(final SplitAccountRecord record) {
        this.splitAccountRecordDao.insert(record);
    }

    /**
     * {@inheritDoc}
     *
     * @param record
     * @return
     */
    @Override
    public int update(final SplitAccountRecord record) {
        return this.splitAccountRecordDao.update(record);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<SplitAccountRecord> getById(final long id) {
        return Optional.fromNullable(this.splitAccountRecordDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @param orderNo1  分账单号（目前和交易订单号一致）
     * @param tradeAmount  交易总额
     * @param poundage   手续费
     * @param triple  <accountId, 分账金额， 分账费率>
     * @param receiptMoneyUserName  收钱人
     * @param remark
     */
    @Override
    @Transactional
    public void addPaySplitAccountRecord(final String orderNo, final String orderNo1, final BigDecimal tradeAmount,
                                         final BigDecimal poundage, final Triple<Long, BigDecimal, BigDecimal> triple,
                                         final String receiptMoneyUserName, final String remark) {

        final SplitAccountRecord splitAccountRecord = new SplitAccountRecord();
        splitAccountRecord.setOrderNo(orderNo);
        splitAccountRecord.setSplitOrderNo(orderNo1);
        splitAccountRecord.setTotalAmount(tradeAmount);
        splitAccountRecord.setSplitSn(SnGenerator.generate());
        splitAccountRecord.setOutMoneyAccountId(AccountConstants.POUNDAGE_ACCOUNT_ID);
        splitAccountRecord.setReceiptMoneyAccountId(triple.getLeft());
        splitAccountRecord.setReceiptMoneyUserName(receiptMoneyUserName);
        splitAccountRecord.setSplitAmount(triple.getMiddle());
        splitAccountRecord.setSplitTotalAmount(poundage);
        splitAccountRecord.setSplitRate(triple.getRight());
        splitAccountRecord.setRemark(remark);
        splitAccountRecord.setSplitDate(new Date());
        this.add(splitAccountRecord);
    }


    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @param orderNo1  分账单号（目前和交易订单号一致）
     * @param tradeAmount  交易总额
     * @param poundage   手续费
     * @param triple  <accountId, 分账金额， 分账费率>
     * @param receiptMoneyUserName  收钱人
     * @param remark
     */
    @Override
    @Transactional
    public void addMerchantUpgradePaySplitAccountRecord(final String orderNo, final String orderNo1, final BigDecimal tradeAmount,
                                         final BigDecimal poundage, final Triple<Long, BigDecimal, String> triple,
                                         final String receiptMoneyUserName, final String remark) {

        final SplitAccountRecord splitAccountRecord = new SplitAccountRecord();
        splitAccountRecord.setOrderNo(orderNo);
        splitAccountRecord.setSplitOrderNo(orderNo1);
        splitAccountRecord.setTotalAmount(tradeAmount);
        splitAccountRecord.setSplitSn(SnGenerator.generate());
        splitAccountRecord.setOutMoneyAccountId(AccountConstants.POUNDAGE_ACCOUNT_ID);
        splitAccountRecord.setReceiptMoneyAccountId(triple.getLeft());
        splitAccountRecord.setReceiptMoneyUserName(receiptMoneyUserName);
        splitAccountRecord.setSplitAmount(triple.getMiddle());
        splitAccountRecord.setSplitTotalAmount(poundage);
        splitAccountRecord.setSplitDate(new Date());
        splitAccountRecord.setRemark(remark);
        this.add(splitAccountRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @param orderNo1  分账单号（目前和交易订单号一致）
     * @param tradeAmount  交易总额
     * @param poundage   手续费
     * @param triple  <accountId, 分账金额， 分账费率>
     * @param receiptMoneyUserName  收钱人
     * @param remark
     */
    @Override
    @Transactional
    public void addWithdrawSplitAccountRecord(final String orderNo, final String orderNo1, final BigDecimal tradeAmount,
                                              final BigDecimal poundage, final Triple<Long, BigDecimal, String> triple,
                                              final String receiptMoneyUserName, final String remark) {
        final SplitAccountRecord splitAccountRecord = new SplitAccountRecord();
        splitAccountRecord.setOrderNo(orderNo);
        splitAccountRecord.setSplitOrderNo(orderNo1);
        splitAccountRecord.setTotalAmount(tradeAmount);
        splitAccountRecord.setSplitSn(SnGenerator.generate());
        splitAccountRecord.setOutMoneyAccountId(AccountConstants.POUNDAGE_ACCOUNT_ID);
        splitAccountRecord.setReceiptMoneyAccountId(triple.getLeft());
        splitAccountRecord.setReceiptMoneyUserName(receiptMoneyUserName);
        splitAccountRecord.setSplitAmount(triple.getMiddle());
        splitAccountRecord.setSplitTotalAmount(poundage);
        splitAccountRecord.setRemark(remark);
        splitAccountRecord.setSplitDate(new Date());
        this.add(splitAccountRecord);
    }
}
