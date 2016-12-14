package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.AccountInfo;
import com.jkm.hss.merchant.helper.request.RequestMerchantInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangbin on 2016/11/23.
 */
@Repository
public interface MerchantInfoCheckRecordDao {

    /**
     * 更新
     * @param requestMerchantInfo
     * @return
     */
    int update(RequestMerchantInfo requestMerchantInfo);

    /**
     * 新增账户
     * @param accountInfo
     */
    void add(AccountInfo accountInfo);

    /**
     * 插入审核记录
     * @param requestMerchantInfo
     */
    void insert(RequestMerchantInfo requestMerchantInfo);

    /**
     * 审核失败改变商户状态
     * @param requestMerchantInfo
     * @return
     */
    int updateStatus(RequestMerchantInfo requestMerchantInfo);

    /**
     * 查询商户状态
     * @param i
     * @return
     */
    int getStauts(@Param("merchantId") long merchantId);

    void deletAccount(@Param("accountId") long accountId);

    void deletIl(@Param("res") long res);

    /**
     * 查询审核描述
     * @param merchantId
     * @return
     */
    String selectById(@Param("merchantId") long merchantId);

    /**
     * 查询审核记录表id
     * @param merchantId
     * @return
     */
    int getId(@Param("merchantId") long merchantId);
}
