package com.jkm.api.service.impl;

import com.google.common.base.Optional;
import com.jkm.api.helper.requestparam.PreQuickPayRequest;
import com.jkm.api.service.QuickPayService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yulong.zhang on 2017/8/15.
 */
@Slf4j
@Service
public class QuickPayServiceImpl implements QuickPayService {

    @Autowired
    private MerchantInfoService merchantInfoService;

    /**
     * {@inheritDoc}
     *
     * @param request
     */
    @Override
    public void preQuickPay(final PreQuickPayRequest request) {
        final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.getByMarkCode(request.getMerchantNo());



    }
}
