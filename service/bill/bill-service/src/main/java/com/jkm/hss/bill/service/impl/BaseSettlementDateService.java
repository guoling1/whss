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
     * @param appId
     * @param tradeDate
     * @param settleType
     * @param upperChannel
     * @return
     */
    Date getSettlementDate(String appId, Date tradeDate, String settleType, EnumUpperChannel upperChannel);
}
