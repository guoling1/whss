package com.jkm.hss.bill.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.bill.dao.BusinessOrderDao;
import com.jkm.hss.bill.entity.BusinessOrder;
import com.jkm.hss.bill.service.BusinessOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yulong.zhang on 2017/5/18.
 */
@Slf4j
@Service
public class BusinessOrderServiceImpl implements BusinessOrderService {

    @Autowired
    private BusinessOrderDao businessOrderDao;

    /**
     * {@inheritDoc}
     *
     * @param businessOrder
     */
    @Override
    @Transactional
    public void add(final BusinessOrder businessOrder) {
        this.businessOrderDao.insert(businessOrder);
    }

    /**
     * {@inheritDoc}
     *
     * @param businessOrder
     * @return
     */
    @Override
    @Transactional
    public int update(final BusinessOrder businessOrder) {
        return this.businessOrderDao.update(businessOrder);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @param id
     */
    @Override
    @Transactional
    public int updateOrderNoById(final String orderNo, final long id) {
        return this.businessOrderDao.updateOrderNoById(orderNo, id);
    }

    /**
     * {@inheritDoc}
     *
     * @param remark
     * @param orderNo
     * @return
     */
    @Override
    @Transactional
    public int updateRemarkByOrderNo(final String remark, final String orderNo) {
        return this.businessOrderDao.updateRemarkByOrderNo(remark, orderNo);
    }

    /**
     * {@inheritDoc}
     *
     * @param status
     * @param orderNo
     * @return
     */
    @Override
    @Transactional
    public int updateStatusByOrderNo(final int status, final String orderNo) {
        return this.businessOrderDao.updateStatusByOrderNo(status, orderNo);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<BusinessOrder> getById(final long id) {
        return Optional.fromNullable(this.businessOrderDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<BusinessOrder> getByIdWithLock(final long id) {
        return Optional.fromNullable(this.businessOrderDao.selectByIdWithLock(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @return
     */
    @Override
    public Optional<BusinessOrder> getByOrderNo(final String orderNo) {
        return Optional.fromNullable(this.businessOrderDao.selectByOrderNo(orderNo));
    }
}
