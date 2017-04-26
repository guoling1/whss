package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.LogResponse;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import com.jkm.hss.merchant.entity.ReferralResponse;
import com.jkm.hss.merchant.entity.SettleResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhangbin on 2016/12/2.
 */
@Repository
public interface QueryMerchantInfoRecordDao {

    /**
     * 查询审核详情
     * @param merchantInfo
     * @return
     */
    List<MerchantInfoResponse> getAll(MerchantInfoResponse merchantInfo);

    /**
     * 审核记录
     * @param merchantInfo
     * @return
     */
    List<LogResponse> getLog(MerchantInfoResponse merchantInfo);

    /**
     * 查询推荐信息
     * @param id
     * @return
     */
    ReferralResponse getRefInformation(@Param("id") long id);

    /**
     * 查询商户费率
     * @param id
     * @return
     */
    SettleResponse getSettle(@Param("id") long id);

    /**
     * 查询推荐所属1代理商名称
     * @param firstMerchantId
     * @return
     */
    MerchantInfoResponse selectProxyNameYq(@Param("firstMerchantId") long firstMerchantId);

    /**
     * 查询推荐所属2代理商名称
     * @param secondMerchantId
     * @return
     */
    MerchantInfoResponse selectProxyNameYq1(@Param("secondMerchantId") long secondMerchantId);

    /**
     * 查询推荐信息
     * @param id
     * @return
     */
    MerchantInfoResponse getrecommenderInfo(@Param("id") long id);

    /**
     * 查询推荐所属1代理商名称
     * @param firstDealerId
     * @return
     */
    MerchantInfoResponse selectProxyNameTJ(@Param("firstDealerId") long firstDealerId);

    /**
     * 查询推荐所属2代理商名称
     * @param secondDealerId
     * @return
     */
    MerchantInfoResponse selectProxyNameTJ1(@Param("secondDealerId") long secondDealerId);
}
