package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.helper.request.RequestMerchantInfo;

/**
 * Created by zhangbin on 2016/11/23.
 */
public interface MerchantInfoCheckRecordService {

    /**
     * 更新商户审核状态
     * @param requestMerchantInfo
     */
    int update(RequestMerchantInfo requestMerchantInfo);

    /**
     * 插入审核记录
     * @param requestMerchantInfo
     */
    void save(RequestMerchantInfo requestMerchantInfo);

    /**
     * 审核失败改变商户状态
     * @param requestMerchantInfo
     * @return
     */
    int updateStatus(RequestMerchantInfo requestMerchantInfo);

    /**
     * 查询商户状态
     * @param
     * @return
     */
    int getStauts(long merchantId);

    void deletAccount(long accountId);

    void deletIl(long res);

    /**
     * 查询审核描述
     * @param merchantId
     * @return
     */
    String selectById(long merchantId);

    /**
     * 查询审核记录表id
     * @param merchantId
     * @return
     */
    int getId(long merchantId);
}
