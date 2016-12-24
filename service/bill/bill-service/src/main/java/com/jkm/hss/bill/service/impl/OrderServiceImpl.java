package com.jkm.hss.bill.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.bill.dao.OrderDao;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    /**
     * {@inheritDoc}
     *
     * @param order
     */
    @Override
    @Transactional
    public void add(final Order order) {
        this.orderDao.insert(order);
    }

    /**
     * {@inheritDoc}
     *
     * @param order
     * @return
     */
    @Override
    public int update(final Order order) {
        return this.orderDao.update(order);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    @Transactional
    public int updateStatus(final long id, final int status, final String remark) {
        return this.orderDao.updateStatus(id, status, remark);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    @Transactional
    public int updateSettleStatus(final long id, final int status) {
        return this.orderDao.updateSettleStatus(id, status);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param status
     * @param successSettleTime
     * @return
     */
    @Override
    @Transactional
    public int markSettleSuccess(final long id, final int status, final Date successSettleTime) {
        return this.orderDao.markSettleSuccess(id, status, successSettleTime);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Order> getById(final long id) {
        return Optional.fromNullable(this.orderDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Order> getByIdWithLock(final long id) {
        return Optional.fromNullable(this.orderDao.selectByIdWithLock(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @return
     */
    @Override
    public Optional<Order> getByOrderNo(final String orderNo) {
        return Optional.fromNullable(this.orderDao.selectByOrderNo(orderNo));
    }
}
