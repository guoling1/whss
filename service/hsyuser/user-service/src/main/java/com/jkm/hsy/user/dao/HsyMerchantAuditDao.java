package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.entity.HsyMerchantAuditRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 */
@Repository
public interface HsyMerchantAuditDao {

    /**
     * 查询商户列表
     */
    List<AppBizShop> getMerchant(HsyMerchantAuditRequest hsyMerchantAuditRequest);

    /**
     * 查询商户详情
     */
    AppBizShop getDetails(HsyMerchantAuditRequest hsyMerchantAuditRequest);
}
