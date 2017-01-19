package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.entity.HsyMerchantAuditRequest;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 */
public interface HsyMerchantAuditService {

    /**
     * 查询商户列表
     */
    List<AppBizShop> getMerchant(HsyMerchantAuditRequest hsyMerchantAuditRequest);

    /**
     * 查询商户详情
     */
    AppBizShop getDetails(HsyMerchantAuditRequest hsyMerchantAuditRequest);
}
