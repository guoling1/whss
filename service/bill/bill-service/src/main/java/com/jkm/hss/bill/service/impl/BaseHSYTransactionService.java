package com.jkm.hss.bill.service.impl;

import com.jkm.hss.bill.entity.HsyOrder;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/6/12.
 */
public interface BaseHSYTransactionService {

    /**
     * 下单实现
     *
     * @param hsyOrder
     * @param amount
     * @return
     */
    Triple<Integer, String, String> placeOrderImpl(HsyOrder hsyOrder, BigDecimal amount);
}
