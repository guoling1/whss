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
     * 审核记录
     * @param merchantInfo
     * @return
     */
    List<LogResponse> getLog(MerchantInfoResponse merchantInfo) throws ParseException;
}
