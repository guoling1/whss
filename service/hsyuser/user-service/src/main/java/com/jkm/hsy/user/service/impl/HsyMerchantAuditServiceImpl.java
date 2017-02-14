package com.jkm.hsy.user.service.impl;

import com.jkm.hsy.user.constant.IndustryCodeType;
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
    public HsyMerchantAuditResponse getDetails(Long id) {

        HsyMerchantAuditResponse res = hsyMerchantAuditDao.getDetails(id);
        String districtCode = res.getDistrictCode();
        HsyMerchantAuditResponse ret = hsyMerchantAuditDao.getCode(districtCode);
        if (!ret.getParentCode().equals("0")){
            HsyMerchantAuditResponse reu = hsyMerchantAuditDao.getCity(ret.getParentCode());
            res.setDistrictCode(reu.getAName()+ret.getAName());
        }
        int industryCode = Integer.parseInt(res.getIndustryCode());
        if (industryCode==1000){
            res.setIndustryCode(IndustryCodeType.CATERING.industryCodeValue);
        }
        if (industryCode==1001){
            res.setIndustryCode(IndustryCodeType.SUPERMARKET.industryCodeValue);
        }
        if (industryCode==1002){
            res.setIndustryCode(IndustryCodeType.LIFE_SERVICES.industryCodeValue);
        }
        if (industryCode==1003){
            res.setIndustryCode(IndustryCodeType.SHOPPING.industryCodeValue);
        }
        if (industryCode==1004){
            res.setIndustryCode(IndustryCodeType.BEAUTY.industryCodeValue);
        }
        if (industryCode==1005){
            res.setIndustryCode(IndustryCodeType.EXERCISE.industryCodeValue);
        }
        if (industryCode==1006){
            res.setIndustryCode(IndustryCodeType.HOTEL.industryCodeValue);
        }


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

    @Override
    public HsyMerchantAuditResponse selectById(Long id) {
        HsyMerchantAuditResponse res=hsyMerchantAuditDao.selectById(id);
        return res;
    }

    @Override
    public void updateAccount(Long accountID, Long id) {
        hsyMerchantAuditDao.updateAccount(accountID,id);
    }
}
