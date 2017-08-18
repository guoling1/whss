package com.jkm.api.service;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by yulong.zhang on 2017/8/18.
 */
public interface SettleCallbackService {

    /**
     * 结算回调用户
     *
     * @param orderId
     * @param businessOrderId
     * @return
     */
    Pair<Integer, String> settleCallback(long orderId, long businessOrderId);
}
