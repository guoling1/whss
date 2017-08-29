package com.jkm.api.service;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by yulong.zhang on 2017/8/17.
 */
public interface PayCallbackService {

    /**
     * 支付回调
     *
     * @param orderId
     * @param businessOrderId
     * @param isSuccess 1成功
     * @param msg
     */
    Pair<Integer, String> payCallback(long orderId, long businessOrderId, int isSuccess, String msg);

}
