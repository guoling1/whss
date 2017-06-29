package com.jkm.hss.bill.service;

import com.google.common.base.Optional;
import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hsy.user.entity.AppParam;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by wayne on 17/5/17.
 * 好收银订单处理
 */
public interface HSYOrderService {
    /**
     * 下单
     * @param hsyOrder
     */
    void insert(HsyOrder hsyOrder);

    /**
     * 更新
     * @param hsyOrder
     * @return
     */
    int update(HsyOrder hsyOrder);

    /**
     * 更新订单金额和状态
     *
     * @param id
     * @param amount
     * @param status
     */
    void updateAmountAndStatus(long id, BigDecimal amount, int status);

    /**
     * 按id查询
     *
     * @param hsyOrderId
     * @return
     */
    Optional<HsyOrder> getById(long hsyOrderId);

    /**
     * 加锁按id查询
     *
     * @param hsyOrderId
     * @return
     */
    Optional<HsyOrder> getByIdWithLock(long hsyOrderId);

    /**
     * 根据交易订单号查询hsy订单
     * @param orderNo
     * @return
     */
    Optional<HsyOrder> selectByOrderNo(String orderNo);

    /**
     * 根据交易订单ID查询hsy订单
     * @param orderId
     * @return
     */
    Optional<HsyOrder> selectByOrderId(long orderId);

    /**
     * 订单列表及统计
     * @param dataParam
     * @param appParam
     * @return
     */
    String orderListst(String dataParam, AppParam appParam);

    /**
     * 订单详情
     * @param dataParam
     * @param appParam
     * @return
     */
    String appOrderDetail(String dataParam, AppParam appParam);

    /**
     * 按商户编号和时间查询订单
     *
     * @param merchantNo
     * @param startTime
     * @param endTime
     * @return
     */
    List<HsyOrder> getByMerchantNoAndTime(String merchantNo, Date startTime, Date endTime);
}
