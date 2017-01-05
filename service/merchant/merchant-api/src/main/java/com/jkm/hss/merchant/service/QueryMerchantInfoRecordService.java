package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.LogResponse;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;

import java.text.ParseException;
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
    List<MerchantInfoResponse> getAll(MerchantInfoResponse merchantInfo) throws ParseException;

    /**
     * 查询是几级代理商
     * @param dealerId
     * @return
     */
    List<MerchantInfoResponse> getLevel(long dealerId);

    /**
     * level=1查询代理商名字
     * @param level
     * @param dealerId
     * @return
     */
    List<MerchantInfoResponse> getResults(int level,long dealerId);

    /**
     * 查询一级代理商名称
     * @param firstLevelDealerId
     * @return
     */
    List<MerchantInfoResponse> getFirstLevel(long firstLevelDealerId);

    /**
     * 审核记录
     * @param merchantInfo
     * @return
     */
    List<LogResponse> getLog(MerchantInfoResponse merchantInfo) throws ParseException;
}
