package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.*;

import java.text.ParseException;
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

    long getUid(Long id);

    AppAuUser getAccId(Long id);

    /**
     * hsy待审核商户列表
     * @param hsyMerchantAuditRequest
     * @return
     */
    List<HsyMerchantAuditResponse> getCheckPending(HsyMerchantAuditRequest hsyMerchantAuditRequest);

    /**
     * hsy待审核商户总数
     * @param hsyMerchantAuditRequest
     * @return
     */
    int getCheckPendingCount(HsyMerchantAuditRequest hsyMerchantAuditRequest);

    /**
     * 查询代理商下的商户（hsy）
     * @param request
     * @return
     */
    List<HsyQueryMerchantResponse> hsyMerchantList(HsyQueryMerchantRequest request);

    /**
     * 查询代理商下的商户总数（hsy）
     * @param request
     * @return
     */
    int hsyMerchantListCount(HsyQueryMerchantRequest request);

    /**
     * 查询二级代理商下的商户（hsy）
     * @param request
     * @return
     */
    List<HsyQueryMerchantResponse> hsyMerchantSecondList(HsyQueryMerchantRequest request);

    /**
     * 查询二级代理商下的商户总数（hsy）
     * @param request
     * @return
     */
    int hsyMerchantSecondListCount(HsyQueryMerchantRequest request);


    void saveLog(String username, Long id, String checkErrorInfo,int stat);

    /**
     * 查日志
     * @param id
     * @return
     */
    List<HsyMerchantInfoCheckRecord> getLog(Long id);

    /**
     * hsy导出
     * @param hsyMerchantAuditRequest
     * @param baseUrl
     * @return
     */
    String downLoadHsyMerchant(HsyMerchantAuditRequest hsyMerchantAuditRequest, String baseUrl) throws ParseException;


}