package com.jkm.hsy.user.service.impl;

import com.jkm.hsy.user.dao.HsyMerchantAuditDao;
import com.jkm.hsy.user.entity.HsyMerchantAuditRequest;
import com.jkm.hsy.user.entity.HsyMerchantAuditResponse;
import com.jkm.hsy.user.service.HsyMerchantAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 */
@Service
public class HsyMerchantAuditServiceImpl implements HsyMerchantAuditService {

    @Autowired
    private HsyMerchantAuditDao hsyMerchantAuditDao;

    @Override
    public List<HsyMerchantAuditResponse> getMerchant(HsyMerchantAuditRequest hsyMerchantAuditRequest) {

        List<HsyMerchantAuditResponse> list = hsyMerchantAuditDao.getMerchant(hsyMerchantAuditRequest);
        return list;
    }

    @Override
    public HsyMerchantAuditResponse getDetails(HsyMerchantAuditRequest hsyMerchantAuditRequest) {

        HsyMerchantAuditResponse res = hsyMerchantAuditDao.getDetails(hsyMerchantAuditRequest);
        return res;
    }

    @Override
    public void auditPass(HsyMerchantAuditRequest hsyMerchantAuditRequest) {
        hsyMerchantAuditDao.updateAuditPass(hsyMerchantAuditRequest);
    }

    @Override
    public void auditNotPass(HsyMerchantAuditRequest hsyMerchantAuditRequest) {
        hsyMerchantAuditDao.updateAuditNotPass(hsyMerchantAuditRequest);
    }

    @Override
    public int getCount(HsyMerchantAuditRequest hsyMerchantAuditRequest) {
        int count = hsyMerchantAuditDao.getCount(hsyMerchantAuditRequest);
        return count;
    }
}
