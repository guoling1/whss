package com.jkm.hss.bill.service;

import com.google.common.base.Optional;
import com.jkm.hss.bill.entity.RefundOrder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/5/4.
 */
public interface RefundOrderService {
    /**
     * 插入
     *
     * @param refundOrder
     */
    void add(RefundOrder refundOrder);

    /**
     * 更新
     *
     * @param refundOrder
     * @return
     */
    int update(RefundOrder refundOrder);

    /**
     * 更新状态
     *
     * @param id
     * @param status
     */
    int updateStatus(long id, int status);

    /**
     * 按ID查询
     *
     * @param id
     * @return
     */
    Optional<RefundOrder> getById(long id);

    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    Optional<RefundOrder> getByIdWithLock(long id);

    /**
     * 按退款单号查询
     *
     * @param orderNo
     * @return
     */
    Optional<RefundOrder> getByOrderNo(String orderNo);

    /**
     * 按退款流水号查询
     *
     * @param sn
     * @return
     */
    Optional<RefundOrder> getBySn(String sn);

    /**
     * 按支付交易单id查询
     *
     * @param payOrderId
     * @return
     */
    List<RefundOrder> getByPayOrderId(long payOrderId);

    /**
     * 按支付交易单id查询未退款失败的个数
     *
     * @param payOrderId
     * @return
     */
    int getUnRefundErrorCountByPayOrderId(long payOrderId);

    /**
     * 按支付交易单号查询
     *
     * @param payOrderNo
     * @return
     */
    List<RefundOrder> getByPayOrderNo(String payOrderNo);

    /**
     * 按支付交易单id查询已经退款/退款中 金额
     *
     * @param payOrderId
     * @return
     */
    BigDecimal getRefundedAmount(long payOrderId);
}
