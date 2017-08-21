package com.jkm.hss.bill.service;

import com.google.common.base.Optional;
import com.jkm.hss.bill.entity.BusinessOrder;

/**
 * Created by yulong.zhang on 2017/5/18.
 */
public interface BusinessOrderService {

    /**
     * 插入
     *
     * @param businessOrder
     */
    void add(BusinessOrder businessOrder);

    /**
     * 更新
     *
     * @param businessOrder
     * @return
     */
    int update(BusinessOrder businessOrder);

    /**
     * 更新订单号
     *
     * @param orderNo
     * @param id
     */
    int updateOrderNoById(String orderNo, long id);

    /**
     * 更新备注
     *
     * @param remark
     * @param businessOrderNo
     * @return
     */
    int updateRemarkByOrderNo(String remark, String businessOrderNo);

    /**
     * 更新备注
     *
     * @param status
     * @param orderNo
     * @return
     */
    int updateStatusByOrderNo(int status, String orderNo);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<BusinessOrder> getById(long id);

    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    Optional<BusinessOrder> getByIdWithLock(long id);

    /**
     * 按订单号查询
     *
     * @param orderNo
     * @param merchantId
     * @return
     */
    Optional<BusinessOrder> getByOrderNoAndMerchantId(String orderNo, long merchantId);
}
