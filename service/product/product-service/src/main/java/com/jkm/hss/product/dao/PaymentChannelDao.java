package com.jkm.hss.product.dao;

import com.jkm.hss.product.helper.response.PaymentChannelRequest;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangbin on 2017/2/27.
 */
@Repository
public interface PaymentChannelDao {

    /**
     * 添加通道
     * @param request
     */
    void addPaymentChannel(PaymentChannelRequest request);
}
