package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.dao.SplitAccountRecordDao;
import com.jkm.hss.account.entity.ProfitCountRequest;
import com.jkm.hss.account.entity.ProfitCountRespons;
import com.jkm.hss.account.entity.ProfitDetailCountRespons;
import com.jkm.hss.account.entity.SplitAccountRecord;
import com.jkm.hss.account.enums.EnumSplitBusinessType;
import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.account.sevice.SplitAccountRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    public void addPaySplitAccountRecord(final String splitBusinessType, final String orderNo, final String orderNo1, final BigDecimal tradeAmount,
                                         final BigDecimal poundage, final Triple<Long, BigDecimal, BigDecimal> triple,
                                         final String receiptMoneyUserName, final String remark, final int accountUserType, final String settleType) {

        final SplitAccountRecord splitAccountRecord = new SplitAccountRecord();
        splitAccountRecord.setSettleType(settleType);
        splitAccountRecord.setBusinessType(splitBusinessType);
        splitAccountRecord.setOrderNo(orderNo);
        splitAccountRecord.setSplitOrderNo(orderNo1);
        splitAccountRecord.setTotalAmount(tradeAmount);
        splitAccountRecord.setSplitSn(this.getSplitSn());
        splitAccountRecord.setOutMoneyAccountId(AccountConstants.POUNDAGE_ACCOUNT_ID);
        splitAccountRecord.setReceiptMoneyAccountId(triple.getLeft());
        splitAccountRecord.setReceiptMoneyUserName(receiptMoneyUserName);
        splitAccountRecord.setSplitAmount(triple.getMiddle());
        splitAccountRecord.setSplitTotalAmount(poundage);
        splitAccountRecord.setSplitRate(triple.getRight());
        splitAccountRecord.setRemark(remark);
        splitAccountRecord.setSplitDate(new Date());
        splitAccountRecord.setAccountUserType(accountUserType);
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
    public void addMerchantUpgradePaySplitAccountRecord(final String splitBusinessType, final String orderNo, final String orderNo1, final BigDecimal tradeAmount,
                                         final BigDecimal poundage, final Triple<Long, BigDecimal, String> triple,
                                         final String receiptMoneyUserName, final String remark, final int accountUserType) {

        final SplitAccountRecord splitAccountRecord = new SplitAccountRecord();
        splitAccountRecord.setBusinessType(splitBusinessType);
        splitAccountRecord.setOrderNo(orderNo);
        splitAccountRecord.setSplitOrderNo(orderNo1);
        splitAccountRecord.setTotalAmount(tradeAmount);
        splitAccountRecord.setSplitSn(this.getSplitSn());
        splitAccountRecord.setOutMoneyAccountId(AccountConstants.POUNDAGE_ACCOUNT_ID);
        splitAccountRecord.setReceiptMoneyAccountId(triple.getLeft());
        splitAccountRecord.setReceiptMoneyUserName(receiptMoneyUserName);
        splitAccountRecord.setSplitAmount(triple.getMiddle());
        splitAccountRecord.setSplitTotalAmount(poundage);
        splitAccountRecord.setSplitDate(new Date());
        splitAccountRecord.setRemark(remark);
        splitAccountRecord.setAccountUserType(accountUserType);
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
    public void addWithdrawSplitAccountRecord(final String splitBusinessType, final String orderNo, final String orderNo1, final BigDecimal tradeAmount,
                                              final BigDecimal poundage, final Triple<Long, BigDecimal, String> triple,
                                              final String receiptMoneyUserName, final String remark, final int accountUserType) {
        final SplitAccountRecord splitAccountRecord = new SplitAccountRecord();
        splitAccountRecord.setBusinessType(splitBusinessType);
        splitAccountRecord.setOrderNo(orderNo);
        splitAccountRecord.setSplitOrderNo(orderNo1);
        splitAccountRecord.setTotalAmount(tradeAmount);
        splitAccountRecord.setSplitSn(this.getSplitSn());
        splitAccountRecord.setOutMoneyAccountId(AccountConstants.POUNDAGE_ACCOUNT_ID);
        splitAccountRecord.setReceiptMoneyAccountId(triple.getLeft());
        splitAccountRecord.setReceiptMoneyUserName(receiptMoneyUserName);
        splitAccountRecord.setSplitAmount(triple.getMiddle());
        splitAccountRecord.setSplitTotalAmount(poundage);
        splitAccountRecord.setRemark(remark);
        splitAccountRecord.setSplitDate(new Date());
        splitAccountRecord.setAccountUserType(accountUserType);
        this.add(splitAccountRecord);
    }

    /**
     * {@inheritDoc}
     * @param pageNo
     * @param pageSize
     * @param orderNo
     * @param businessType
     * @param beginDate
     * @param endDate
     * @return
     */
    @Override
    public PageModel<SplitAccountRecord> selectByParam(int pageNo, int pageSize,long accountId, String orderNo, String businessType, String beginDate, String endDate) {

        PageModel<SplitAccountRecord> pageModel = new PageModel<>(pageNo, pageSize);



        Date beginTime = null;
        Date endTime = null;
        if (beginDate != null && !beginDate.equals("")){
            beginTime = DateFormatUtil.parse(beginDate + " 00:00:00", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
            endTime = DateFormatUtil.parse(endDate + " 23:59:59", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        List<SplitAccountRecord> list =
                this.splitAccountRecordDao.selectByParam( pageModel.getFirstIndex(), pageSize, accountId, orderNo, businessType, beginTime, endTime);
        long count = this.splitAccountRecordDao.selectCountByParam(accountId, orderNo, businessType, beginTime, endTime);

        pageModel.setCount(count);
        pageModel.setRecords(list);
        return pageModel;
    }

    /**
     * {@inheritDoc}
     *
     * @param splitSn
     * @return
     */
    @Override
    public boolean checkExistBySplitSn(final String splitSn) {
        final int count = this.splitAccountRecordDao.selectCountBySplitSn(splitSn);
        return count == 0;
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @return
     */
    @Override
    public List<SplitAccountRecord> getByOrderNo(final String orderNo) {
        return this.splitAccountRecordDao.selectByOrderNo(orderNo);
    }

    @Override
    public List<ProfitCountRespons> getProfit(ProfitCountRequest request) {
        ProfitCountRequest request1=selectTime(request);
        List<ProfitCountRespons> list = this.splitAccountRecordDao.getProfit(request1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (list!=null){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getSplitDate()!=null&&!list.get(i).getSplitDate().equals("")){
                    String dates = sdf.format(list.get(i).getSplitDate());
                    list.get(i).setSplitDates(dates);
                }
                if (list.get(i).getBusinessType().equals("hssPay")){
                    list.get(i).setBusinessType(EnumSplitBusinessType.HSSPAY.getValue());
                }
                if (list.get(i).getBusinessType().equals("hssWithdraw")){
                    list.get(i).setBusinessType(EnumSplitBusinessType.HSSWITHDRAW.getValue());
                }
                if (list.get(i).getBusinessType().equals("hssUpgrade")){
                    list.get(i).setBusinessType(EnumSplitBusinessType.HSSPROMOTE.getValue());
                }
                if (list.get(i).getBusinessType().equals("hsyPay")){
                    list.get(i).setBusinessType(EnumSplitBusinessType.HSYPAY.getValue());
                }
            }
        }
        return list;
    }

    @Override
    public int getProfitCount(ProfitCountRequest request) {
        return this.splitAccountRecordDao.getProfitCount(request);
    }

    @Override
    public List<ProfitDetailCountRespons> getCountDetails(ProfitCountRequest req) {
        ProfitCountRequest request1=selectTime(req);

        List<ProfitDetailCountRespons> list = this.splitAccountRecordDao.getCountDetails(request1);
        return list;
    }

    @Override
    public int getCountDetailsNo(ProfitCountRequest req) {
        return this.splitAccountRecordDao.getCountDetailsNo(req);
    }


    private ProfitCountRequest selectTime(ProfitCountRequest req) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt= new Date();

        Date dts= null;
        try {
            String d = sdf.format(dt);
            dt = sdf.parse(d);
            req.setSplitDate(dt);

            String ds = sdf.format(req.getSplitDate());
            dts = sdf.parse(ds);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dts);
            rightNow.add(Calendar.DATE, 1);
            req.setSplitDate1(rightNow.getTime());
            req.setSplitDate(dts);

        } catch (ParseException e) {
            log.debug("时间转换异常");
            e.printStackTrace();
        }
        return req;
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
