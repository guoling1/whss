package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.MerchantInfoResponse;

import java.util.List;

/**
 * Created by zhangbin on 2016/12/2.
 */
public interface QueryMerchantInfoRecordService {

    /**
     * 查询商户详情
     * @param merchantInfo
     * @return
     */
    List<MerchantInfoResponse> getAll(MerchantInfoResponse merchantInfo);

    List<MerchantInfoResponse> getLevel(long dealerId);

    List<MerchantInfoResponse> getResults(long firstLevelDealerId);

}
