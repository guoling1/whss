package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.account.dao.AccountFlowDao;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.AccountFlow;
import com.jkm.hss.account.entity.SplitAccountRecord;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.sevice.AccountFlowService;
import com.jkm.hss.account.sevice.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
            accountFlow.setIncomeAmount(new BigDecimal("0.00"));
            accountFlow.setBeforeAmount(account.getAvailable().add(changeAmount));
            accountFlow.setAfterAmount(account.getAvailable());
        }
        if (EnumAccountFlowType.INCREASE.getId() == type.getId()) {
            accountFlow.setOutAmount(new BigDecimal("0.00"));
            accountFlow.setIncomeAmount(changeAmount);
            accountFlow.setBeforeAmount(account.getAvailable().subtract(changeAmount));
            accountFlow.setAfterAmount(account.getAvailable());
        }
        accountFlow.setChangeTime(new Date());
        accountFlow.setRemark(remark);
        this.accountFlowDao.insert(accountFlow);
    }

    /**
     * {@inheritDoc}
     * @param pageNo
     * @param pageSize
     * @param flowSn
     * @param type
     * @param beginDate
     * @param endDate
     * @return
     */
    @Override
    public PageModel<AccountFlow> selectByParam(int pageNo, int pageSize,long accountId, String flowSn, int type, String beginDate, String endDate) {

       PageModel<AccountFlow> pageModel = new PageModel<>(pageNo, pageSize);

        Date beginTime = null;
        Date endTime = null;
        if (beginDate != null && !beginDate.equals("")){
            beginTime = DateFormatUtil.parse(beginDate + " 00:00:00", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
            endTime = DateFormatUtil.parse(endDate + " 23:59:59", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        List<AccountFlow> list =
                this.accountFlowDao.selectByParam( pageModel.getFirstIndex(), pageSize, accountId, flowSn, type, beginTime, endTime);
        long count = this.accountFlowDao.selectCountByParam(accountId, flowSn, type, beginTime, endTime);

        pageModel.setCount(count);
        pageModel.setRecords(list);
        return pageModel;
    }
}
