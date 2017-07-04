package com.jkm.hss.product.servcie.impl;

import com.jkm.hss.product.dao.PaymentChannelDao;
import com.jkm.hss.product.helper.response.PaymentChannelRequest;
import com.jkm.hss.product.servcie.PaymentChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangbin on 2017/2/27.
 */
@Slf4j
@Service
public class PaymentChannelServiceImpl implements PaymentChannelService {

    @Autowired
    private PaymentChannelDao paymentChannelDao;

    @Override
    public void addPaymentChannel(PaymentChannelRequest request) {
        this.paymentChannelDao.addPaymentChannel(request);
    }
}
