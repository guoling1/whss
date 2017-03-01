package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.helper.request.MerchantChannelRateRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by xingliujie on 2017/2/27.
 */
@Repository
public interface MerchantChannelRateDao {
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
    MerchantChannelRate selectByChannelTypeSignAndProductIdAndMerchantId(MerchantChannelRateRequest merchantChannelRateRequest);

    /**
     * 查询入网中的商户信息
     * @return
     */
    List<Long> selectIngMerchantInfo(@Param("entId") int entId);

    /**
     *  更新商户入网状态
     * @param merchantId
     * @param id
     */
    void updateEnterNetStatus(@Param("merchantId") long merchantId, @Param("id") int id);

    /**
     *  更新商户入网状态
     * @param signIdList
     */
    int batchCheck(@Param("signIdList") List<Integer> signIdList);
}
