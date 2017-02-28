package com.jkm.hss.product.servcie;

import com.jkm.hss.product.helper.response.PaymentChannelRequest;

/**
 * Created by zhangbin on 2017/2/27.
 */
public interface PaymentChannelService {

    /**
     * 添加通道
     * @param request
     */
    void addPaymentChannel(PaymentChannelRequest request);
}
