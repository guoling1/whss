package com.jkm.api.service;

import com.jkm.api.helper.requestparam.ConfirmQuickPayRequest;
import com.jkm.api.helper.requestparam.PreQuickPayRequest;
import com.jkm.api.helper.responseparam.ConfirmQuickPayResponse;

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

    /**
     * 确认支付
     *
     * @param request
     */
    void confirmQuickPay(ConfirmQuickPayRequest request, ConfirmQuickPayResponse response);
}
