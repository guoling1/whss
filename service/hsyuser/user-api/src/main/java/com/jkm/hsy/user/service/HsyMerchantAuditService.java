package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.HsyMerchantAuditRequest;
import com.jkm.hsy.user.entity.HsyMerchantAuditResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 */
public interface HsyMerchantAuditService {

    /**
     * 查询商户列表
     */
    List<HsyMerchantAuditResponse> getMerchant(HsyMerchantAuditRequest hsyMerchantAuditRequest);

    /**
     * 查询商户详情
     */
    HsyMerchantAuditResponse getDetails(Long id);

    /**
     * 审核通过
     */
    void auditPass(HsyMerchantAuditRequest hsyMerchantAuditRequest);

    /**
     * 审核不通过
     */
    void auditNotPass(HsyMerchantAuditRequest hsyMerchantAuditRequest);

    /**
     * 查询条总数
     * @param hsyMerchantAuditRequest
     * @return
     */
    int getCount(HsyMerchantAuditRequest hsyMerchantAuditRequest);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    HsyMerchantAuditResponse selectById(Long id);



    /**
     * 更新账户id
     * @param accountID
     */
    void updateAccount(Long accountID, Long id);


    void stepChange(Long uid);

    Long getUid(Long id);
}