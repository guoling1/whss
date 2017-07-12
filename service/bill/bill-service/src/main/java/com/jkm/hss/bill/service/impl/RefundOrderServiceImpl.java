package com.jkm.hss.bill.service.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.bill.dao.RefundOrderDao;
import com.jkm.hss.bill.entity.RefundOrder;
import com.jkm.hss.bill.service.RefundOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/5/4.
 */
@Slf4j
@Service
public class RefundOrderServiceImpl implements RefundOrderService {

    @Autowired
    private RefundOrderDao refundOrderDao;
    /**
     * {@inheritDoc}
     *
     * @param refundOrder
     */
    @Override
    @Transactional
    public void add(final RefundOrder refundOrder) {
        this.refundOrderDao.insert(refundOrder);
    }

    /**
     * {@inheritDoc]}
     *
     * @param refundOrder
     * @return
     */
    @Override
    @Transactional
    public int update(final RefundOrder refundOrder) {
        return this.refundOrderDao.update(refundOrder);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param status
     */
    @Override
    @Transactional
    public int updateStatus(final long id, final int status) {
       return this.refundOrderDao.updateStatus(id, status);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<RefundOrder> getById(final long id) {
        return Optional.fromNullable(this.refundOrderDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<RefundOrder> getByIdWithLock(final long id) {
        return Optional.fromNullable(this.refundOrderDao.selectByIdWithLock(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @return
     */
    @Override
    public Optional<RefundOrder> getByOrderNo(final String orderNo) {
        return Optional.fromNullable(this.refundOrderDao.selectByOrderNo(orderNo));
    }

    /**
     * {@inheritDoc}
     *
     * @param sn
     * @return
     */
    @Override
    public Optional<RefundOrder> getBySn(final String sn) {
        return Optional.fromNullable(this.refundOrderDao.selectBySn(sn));
    }

    /**
     * {@inheritDoc}
     *
     * @param payOrderId
     * @return
     */
    @Override
    public List<RefundOrder> getByPayOrderId(final long payOrderId) {
        return this.refundOrderDao.selectByPayOrderId(payOrderId);
    }

    /**
     * {@inheritDoc}
     *
     * @param payOrderId
     * @return
     */
    @Override
    public int getUnRefundErrorCountByPayOrderId(final long payOrderId) {
        return this.refundOrderDao.selectUnRefundErrorCountByPayOrderId(payOrderId);
    }

    /**
     * {@inheritDoc}
     *
     * @param payOrderNo
     * @return
     */
    @Override
    public List<RefundOrder> getByPayOrderNo(final String payOrderNo) {
        return this.refundOrderDao.selectByPayOrderNo(payOrderNo);
    }

    /**
     * {@inheritDoc}
     *
     * @param payOrderId
     * @return
     */
    @Override
    public BigDecimal getRefundedAmount(final long payOrderId) {
        return this.refundOrderDao.selectRefundedAmount(payOrderId);
    }
}
