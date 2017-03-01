package com.jkm.hss.merchant.dao;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.helper.request.MerchantChannelRateRequest;
import com.jkm.hss.merchant.helper.request.MerchantEnterInRequest;
import com.jkm.hss.merchant.helper.request.MerchantGetRateRequest;
import com.jkm.hss.merchant.helper.request.MerchantUpgradeRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
    void updateEnterNetStatus(@Param("merchantId") long merchantId, @Param("id") int id, @Param("msg") String msg);

    /**
     *  更新商户入网状态
     * @param signIdList
     */
    int batchCheck(@Param("signIdList") List<Integer> signIdList,@Param("enterNet") int enterNet,@Param("merchantId") long merchantId,@Param("remarks") String remarks);

    /**
     * 查询
     * @param merchantId
     * @return
     */
    List<MerchantChannelRate> selectByMerchantId(@Param("merchantId") long merchantId);
    /**
     *根据支付方式名字、通道标示、产品编码查询商户费用
     * @param merchantGetRateRequest
     * @return
     */
    List<MerchantChannelRate> selectByThirdCompanyAndProductIdAndMerchantId(MerchantGetRateRequest merchantGetRateRequest);

    /**
     *根据三方公司名字、通道标示、产品编码查询商户费用
     * @param merchantEnterInRequest
     * @return
     */
    List<MerchantChannelRate> selectByChannelCompanyAndProductIdAndMerchantId(MerchantEnterInRequest merchantEnterInRequest);

    /**
     * 升级降费率
     * @param merchantUpgradeRequest
     */
    void toUpgrade(MerchantUpgradeRequest merchantUpgradeRequest);

}
