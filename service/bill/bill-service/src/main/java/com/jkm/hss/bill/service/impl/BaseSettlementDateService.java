package com.jkm.hss.bill.service.impl;

import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.product.enums.EnumUpperChannel;

import java.util.Date;

/**
 * Created by yulong.zhang on 2017/5/12.
 */
public interface BaseSettlementDateService {

    /**
     * 获取结算时间
     *
     * @param order
     * @param upperChannel
     * @return
     */
    Date getSettlementDate(Order order, EnumUpperChannel upperChannel);
}
