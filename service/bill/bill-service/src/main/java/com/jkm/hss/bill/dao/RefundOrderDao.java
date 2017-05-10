package com.jkm.hss.bill.dao;

import com.jkm.hss.bill.entity.RefundOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/5/4.
 */
@Repository
public interface RefundOrderDao {

    /**
     * 插入
     *
     * @param refundOrder
     */
    void insert(RefundOrder refundOrder);

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
     * @return
     */
    int updateStatus(@Param("id") long id, @Param("status") int status);

    /**
     * 按ID查询
     *
     * @param id
     * @return
     */
    RefundOrder selectById(@Param("id") long id);

    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    RefundOrder selectByIdWithLock(@Param("id") long id);

    /**
     * 按退款单号查询
     *
     * @param orderNo
     * @return
     */
    RefundOrder selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 按退款流水号查询
     *
     * @param sn
     * @return
     */
    RefundOrder selectBySn(@Param("sn") String sn);

    /**
     * 按支付交易单id查询
     *
     * @param payOrderId
     * @return
     */
    List<RefundOrder> selectByPayOrderId(@Param("payOrderId") long payOrderId);

    /**
     * 按支付交易单号查询
     *
     * @param payOrderNo
     * @return
     */
    List<RefundOrder> selectByPayOrderNo(@Param("payOrderNo") String payOrderNo);

    /**
     * 按支付交易单的id查询已经退款/退款中的 金额
     *
     * @param payOrderId
     * @return
     */
    BigDecimal selectRefundedAmount(@Param("payOrderId") long payOrderId);
}
