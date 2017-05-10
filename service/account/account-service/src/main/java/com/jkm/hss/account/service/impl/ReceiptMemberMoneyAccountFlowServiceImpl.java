package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.account.dao.ReceiptMemberMoneyAccountFlowDao;
import com.jkm.hss.account.entity.ReceiptMemberMoneyAccountFlow;
import com.jkm.hss.account.sevice.ReceiptMemberMoneyAccountFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yulong.zhang on 2017/5/10.
 */
@Slf4j
@Service
public class ReceiptMemberMoneyAccountFlowServiceImpl implements ReceiptMemberMoneyAccountFlowService {

    @Autowired
    private ReceiptMemberMoneyAccountFlowDao receiptMemberMoneyAccountFlowDao;
    /**
     * {@inheritDoc}
     *
     * @param receiptMemberMoneyAccountFlow
     */
    @Override
    @Transactional
    public void add(final ReceiptMemberMoneyAccountFlow receiptMemberMoneyAccountFlow) {
        this.receiptMemberMoneyAccountFlowDao.insert(receiptMemberMoneyAccountFlow);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<ReceiptMemberMoneyAccountFlow> getById(final long id) {
        return Optional.fromNullable(this.receiptMemberMoneyAccountFlowDao.selectById(id));
    }
}
