package com.jkm.api.service;

import com.jkm.api.helper.requestparam.PreQuickPayRequest;

/**
 * Created by yulong.zhang on 2017/8/15.
 */
public interface QuickPayService {

    /**
     * 快捷预下单
     *
     * @param request
     * @return
     */
    String preQuickPay(PreQuickPayRequest request);
}
