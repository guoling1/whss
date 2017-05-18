package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.HsyOrder;

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
}
