package com.jkm.hss.merchant.service;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.enums.EnumEnterNet;
import com.jkm.hss.merchant.helper.request.MerchantChannelRateRequest;

import java.util.List;
import java.util.Set;

/**
 * Created by xingliujie on 2017/2/27.
 */
public interface MerchantChannelRateService {
    /**
     * 费率初始化
     * @param merchantChannelRate
     * @return
     */
    int initMerchantChannelRate(MerchantChannelRate merchantChannelRate);

    /**
     *根据商户编码、通道标示、产品编码查询商户费用
     * @param merchantChannelRateRequest
     * @return
     */
    Optional<MerchantChannelRate> selectByChannelTypeSignAndProductIdAndMerchantId(MerchantChannelRateRequest merchantChannelRateRequest);

    /**
     * 查询入网中商户的信息
     * @return
     */
    List<Long> selectIngMerchantInfo();

    /**
     * 更新商户入网状态
     * @param merchantId
     * @param enumEnterNet
     */
    void updateEnterNetStatus(long merchantId, EnumEnterNet enumEnterNet);

    /**
     *  更新商户入网状态
     * @param signIdList
     */
    int batchCheck(List<Integer> signIdList);

}
