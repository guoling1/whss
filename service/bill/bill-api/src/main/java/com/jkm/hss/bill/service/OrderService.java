package com.jkm.hss.bill.service;

import com.google.common.base.Optional;
import com.jkm.hss.bill.entity.Order;

import java.util.Date;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
public interface OrderService {

    /**
     * 插入
     *
     * @param order
     */
    void add(Order order);

    /**
     * 更新
     *
     * @param order
     * @return
     */
    int update(Order order);

    /**
     * 更新交易状态
     *
     * @param id
     * @param status
     */
    int updateStatus(long id, int status, String remark);

    /**
     * 更新结算状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateSettleStatus(long id, int status);

    /**
     * 结算成功
     *
     * @param id
     * @return
     */
    int markSettleSuccess(long id, int status, Date successSettleTime);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<Order> getById(long id);

    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    Optional<Order> getByIdWithLock(long id);

    /**
     * 按交易订单号查询
     *
     * @param orderNo
     * @return
     */
    Optional<Order> getByOrderNo(String orderNo);
}
