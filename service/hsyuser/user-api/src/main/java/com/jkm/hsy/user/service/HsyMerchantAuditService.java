package com.jkm.hsy.user.service;

<<<<<<< HEAD
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


=======
/**
 * Created by Administrator on 2017/1/20.
 */
public interface HsyMerchantAuditService {
>>>>>>> 13dff6f12898143d5ecd03b03ec8a12767c4f91e
}
