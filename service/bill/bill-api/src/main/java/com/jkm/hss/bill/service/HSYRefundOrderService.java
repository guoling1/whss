package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.HsyRefundOrder;

/**
 * Created by wayne on 17/5/20.
 */
public interface HSYRefundOrderService {
    void insert(HsyRefundOrder hsyRefundOrder);
    int update(HsyRefundOrder hsyRefundOrder);
}
