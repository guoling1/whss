package com.jkm.hsy.user.service.impl;

import com.jkm.hsy.user.constant.IndustryCodeType;
import com.jkm.hsy.user.dao.HsyMerchantAuditDao;
import com.jkm.hsy.user.entity.AppAuUser;
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
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.size()>0){
                    if (list.get(i).getStatus()==1){
                        list.get(i).setStat("审核已通过");
                    }
                    if (list.get(i).getStatus()==2){
                        list.get(i).setStat("待审核");
                    }
                    if (list.get(i).getStatus()==3){
                        list.get(i).setStat("审核未通过");
                    }
                    if (list.get(i).getStatus()==4){
                        list.get(i).setStat("商户已注册");
                    }
                }
                String districtCode =list.get(i).getDistrictCode();
                if (districtCode!=null&&!districtCode.equals("")){
                    HsyMerchantAuditResponse ret = hsyMerchantAuditDao.getCode(districtCode);

                    if (!ret.getParentCode().equals("0")){
                        HsyMerchantAuditResponse reu = hsyMerchantAuditDao.getCity(ret.getParentCode());
                        if (!reu.getParentCode().equals("0")){
                            HsyMerchantAuditResponse reu1 = hsyMerchantAuditDao.getCityOnly(reu.getParentCode());
                            list.get(i).setDistrictCode(reu1.getAName()+reu.getAName()+ret.getAName());
                        }else {
                            list.get(i).setDistrictCode(reu.getAName()+ret.getAName());
                        }
                    }
                    if(ret.getParentCode().equals("0")){
                        HsyMerchantAuditResponse reu = hsyMerchantAuditDao.getCityOnly(ret.getCode());
                        list.get(i).setDistrictCode(reu.getAName());
                    }
                }
                int industryCode = Integer.parseInt(list.get(i).getIndustryCode());
                if (industryCode==1000){
                    list.get(i).setIndustryCode(IndustryCodeType.CATERING.industryCodeValue);
                }
                if (industryCode==1001){
                    list.get(i).setIndustryCode(IndustryCodeType.SUPERMARKET.industryCodeValue);
                }
                if (industryCode==1002){
                    list.get(i).setIndustryCode(IndustryCodeType.LIFE_SERVICES.industryCodeValue);
                }
                if (industryCode==1003){
                    list.get(i).setIndustryCode(IndustryCodeType.SHOPPING.industryCodeValue);
                }
                if (industryCode==1004){
                    list.get(i).setIndustryCode(IndustryCodeType.BEAUTY.industryCodeValue);
                }
                if (industryCode==1005){
                    list.get(i).setIndustryCode(IndustryCodeType.EXERCISE.industryCodeValue);
                }
                if (industryCode==1006){
                    list.get(i).setIndustryCode(IndustryCodeType.HOTEL.industryCodeValue);
                }
            }
        }
        return list;
    }

    @Override
    public HsyMerchantAuditResponse getDetails(Long id) {

        HsyMerchantAuditResponse res = hsyMerchantAuditDao.getDetails(id);
        if (res!=null){
            if (res.getStatus()==1){
                res.setStat("审核已通过");
            }
            if (res.getStatus()==2){
                res.setStat("待审核");
            }
            if (res.getStatus()==3){
                res.setStat("审核未通过");
            }
            if (res.getStatus()==4){
                res.setStat("商户已注册");
            }
        }
        String districtCode = res.getDistrictCode();
        if (districtCode!=null&&!districtCode.equals("")){
            HsyMerchantAuditResponse ret = hsyMerchantAuditDao.getCode(districtCode);
            if (!ret.getParentCode().equals("0")){
                HsyMerchantAuditResponse reu = hsyMerchantAuditDao.getCity(ret.getParentCode());
                if (!reu.getParentCode().equals("0")){
                    HsyMerchantAuditResponse reu1 = hsyMerchantAuditDao.getCityOnly(reu.getParentCode());
                    res.setDistrictCode(reu1.getAName()+reu.getAName()+ret.getAName());
                }else {
                    res.setDistrictCode(reu.getAName()+ret.getAName());
                }

            }
            if(ret.getParentCode().equals("0")){
                HsyMerchantAuditResponse reu = hsyMerchantAuditDao.getCity(ret.getParentCode());
                res.setDistrictCode(reu.getAName());
            }
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

    @Override
    public void stepChange(int uid) {
        hsyMerchantAuditDao.stepChange(uid);
    }

    @Override
    public int getUid(Long id) {
        return hsyMerchantAuditDao.getUid(id);
    }

    @Override
    public AppAuUser getAccId(Long id) {
        AppAuUser res=hsyMerchantAuditDao.getAccId(id);
        return res;
    }

    @Override
    public List<HsyMerchantAuditResponse> getCheckPending(HsyMerchantAuditRequest hsyMerchantAuditRequest) {
        List<HsyMerchantAuditResponse> list = hsyMerchantAuditDao.getCheckPending(hsyMerchantAuditRequest);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.size()>0){
                    if (list.get(i).getStatus()==2){
                        list.get(i).setStat("待审核");
                    }
                }
                String districtCode =list.get(i).getDistrictCode();
                if (districtCode!=null&&!districtCode.equals("")){
                    HsyMerchantAuditResponse ret = hsyMerchantAuditDao.getCode(districtCode);

                    if (!ret.getParentCode().equals("0")){
                        HsyMerchantAuditResponse reu = hsyMerchantAuditDao.getCity(ret.getParentCode());
                        if (!reu.getParentCode().equals("0")){
                            HsyMerchantAuditResponse reu1 = hsyMerchantAuditDao.getCityOnly(reu.getParentCode());
                            list.get(i).setDistrictCode(reu1.getAName()+reu.getAName()+ret.getAName());
                        }else {
                            list.get(i).setDistrictCode(reu.getAName()+ret.getAName());
                        }
                    }
                    if(ret.getParentCode().equals("0")){
                        HsyMerchantAuditResponse reu = hsyMerchantAuditDao.getCityOnly(ret.getCode());
                        list.get(i).setDistrictCode(reu.getAName());
                    }
                }
                int industryCode = Integer.parseInt(list.get(i).getIndustryCode());
                if (industryCode==1000){
                    list.get(i).setIndustryCode(IndustryCodeType.CATERING.industryCodeValue);
                }
                if (industryCode==1001){
                    list.get(i).setIndustryCode(IndustryCodeType.SUPERMARKET.industryCodeValue);
                }
                if (industryCode==1002){
                    list.get(i).setIndustryCode(IndustryCodeType.LIFE_SERVICES.industryCodeValue);
                }
                if (industryCode==1003){
                    list.get(i).setIndustryCode(IndustryCodeType.SHOPPING.industryCodeValue);
                }
                if (industryCode==1004){
                    list.get(i).setIndustryCode(IndustryCodeType.BEAUTY.industryCodeValue);
                }
                if (industryCode==1005){
                    list.get(i).setIndustryCode(IndustryCodeType.EXERCISE.industryCodeValue);
                }
                if (industryCode==1006){
                    list.get(i).setIndustryCode(IndustryCodeType.HOTEL.industryCodeValue);
                }
            }
        }
        return list;
    }

    @Override
    public int getCheckPendingCount(HsyMerchantAuditRequest hsyMerchantAuditRequest) {
        int count = hsyMerchantAuditDao.getCheckPendingCount(hsyMerchantAuditRequest);
        return count;
    }


}
