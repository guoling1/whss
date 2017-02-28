package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.dao.MerchantChannelRateDao;
import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.enums.EnumEnterNet;
import com.jkm.hss.merchant.helper.request.MerchantChannelRateRequest;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by xingliujie on 2017/2/27.
 */
@Slf4j
@Service
public class MerchantChannelRateServiceImpl implements MerchantChannelRateService {
    @Autowired
    private MerchantChannelRateDao merchantChannelRateDao;

    /**
     * 费率初始化
     *
     * @param merchantChannelRate
     * @return
     */
    @Override
    public int initMerchantChannelRate(MerchantChannelRate merchantChannelRate) {
        return merchantChannelRateDao.initMerchantChannelRate(merchantChannelRate);
    }

    /**
     * 根据商户编码、通道标示、产品编码查询商户费用
     *
     * @param merchantChannelRateRequest
     * @return
     */
    @Override
    public Optional<MerchantChannelRate> selectByChannelTypeSignAndProductIdAndMerchantId(MerchantChannelRateRequest merchantChannelRateRequest) {
        return Optional.fromNullable(merchantChannelRateDao.selectByChannelTypeSignAndProductIdAndMerchantId(merchantChannelRateRequest));
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Set<MerchantChannelRate> selectIngMerchantInfo() {
        return this.merchantChannelRateDao.selectIngMerchantInfo(EnumEnterNet.ENTING.getId());
    }
}
