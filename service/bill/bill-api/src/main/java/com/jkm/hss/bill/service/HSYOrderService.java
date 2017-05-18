package com.jkm.hss.bill.service;

import com.google.common.base.Optional;
import com.jkm.hss.bill.entity.HsyOrder;
import org.apache.ibatis.annotations.Param;

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

    Optional<HsyOrder> selectByOrderNo(String orderNo);
}
