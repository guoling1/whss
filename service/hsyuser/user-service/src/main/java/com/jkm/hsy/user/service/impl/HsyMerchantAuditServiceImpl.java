package com.jkm.hsy.user.service.impl;

import com.jkm.hsy.user.dao.HsyMerchantAuditDao;
import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.entity.HsyMerchantAuditRequest;
import com.jkm.hsy.user.service.HsyMerchantAuditService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 */
public class HsyMerchantAuditServiceImpl implements HsyMerchantAuditService {

    @Autowired
    private HsyMerchantAuditDao hsyMerchantAuditDao;

    @Override
    public List<AppBizShop> getMerchant(HsyMerchantAuditRequest hsyMerchantAuditRequest) {

        List<AppBizShop> list = hsyMerchantAuditDao.getMerchant(hsyMerchantAuditRequest);
        return list;
    }

    @Override
    public AppBizShop getDetails(HsyMerchantAuditRequest hsyMerchantAuditRequest) {

        AppBizShop res = hsyMerchantAuditDao.getDetails(hsyMerchantAuditRequest);
        return res;
    }
}
