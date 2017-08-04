package com.jkm.hsy.user.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jkm.base.common.entity.ExcelSheetVO;
import com.jkm.base.common.util.ExcelUtil;
import com.jkm.hss.merchant.enums.EnumSettlePeriodType;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hsy.user.constant.IndustryCodeType;
import com.jkm.hsy.user.constant.WithdrawStatus;
import com.jkm.hsy.user.dao.HsyMerchantAuditDao;
import com.jkm.hsy.user.dao.UserChannelPolicyDao;
import com.jkm.hsy.user.entity.*;
import com.jkm.hsy.user.service.HsyMerchantAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/19.
 */
@Slf4j
@Service
public class HsyMerchantAuditServiceImpl implements HsyMerchantAuditService {

    @Autowired
    private HsyMerchantAuditDao hsyMerchantAuditDao;
    @Autowired
    private UserChannelPolicyDao UserChannelPolicyDao;
    @Override
    public List<HsyMerchantAuditResponse> getMerchant(HsyMerchantAuditRequest hsyMerchantAuditRequest) {

        List<HsyMerchantAuditResponse> list = hsyMerchantAuditDao.getMerchant(hsyMerchantAuditRequest);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.size()>0){
                    list.get(i).setStat(WithdrawStatus.of(list.get(i).getWithDrawStatus()).getValue());
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
        if (res.getLicenceStartDate()!=null&&res.getLicenceEndDate()!=null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            final String start = format.format(res.getLicenceStartDate());
            final String end = format.format(res.getLicenceEndDate());
            res.setStartEndDate(start+" "+"-"+" "+end);
        }
        if(res.getHxbStatus()==null||"".equals(res.getHxbStatus())){
            res.setHxbStatus(0);
        }else{
            res.setHxbStatus(res.getHxbStatus());
        }
        if(res.getHxbOpenProduct()==null||"".equals(res.getHxbOpenProduct())){
            res.setHxbOpenProduct(0);
        }else{
            res.setHxbOpenProduct(res.getHxbOpenProduct());
        }
        if (res!=null){
            res.setStat(WithdrawStatus.of(res.getStatus()).getValue());
        }
        if (res.getBranchDistrictCode()!=null){
            HsyMerchantAuditResponse resul = hsyMerchantAuditDao.getResul(res.getBranchDistrictCode());
            res.setANames1(resul.getANames());
            res.setCodes1(resul.getCodes());
            if (!"0".equals(resul.getParentCode())){
                HsyMerchantAuditResponse result = hsyMerchantAuditDao.getResult1(resul.getParentCode());
                res.setANames(result.getANames1());
                res.setCodes(result.getCodes1());
                if (!"0".equals(result.getParentCode())){
                    res.setANames1(result.getANames1());
                    res.setCodes1(result.getCodes1());
                    if (!"0".equals(result.getParentCode())){
                        HsyMerchantAuditResponse result1 = hsyMerchantAuditDao.getResult(result.getParentCode());
                        res.setANames(result1.getANames());
                        res.setCodes(result1.getCodes());
                    }
                }
            }

        }
        String districtCode = res.getDistrictCode();
        res.setDistrictCodes(res.getDistrictCode());
        if (!"".equals(districtCode)&&districtCode!=null){
            HsyMerchantAuditResponse ret = hsyMerchantAuditDao.getCode(districtCode);
            if (!("0").equals(ret.getParentCode())){
                HsyMerchantAuditResponse reu = hsyMerchantAuditDao.getCity(ret.getParentCode());
                if (!("0").equals(ret.getParentCode())){
                    HsyMerchantAuditResponse reu1 = hsyMerchantAuditDao.getCityOnly(reu.getParentCode());
                    res.setDistrictCode(reu.getAName()+ret.getAName());
                    if (reu1!=null) {
                        res.setDistrictCode(reu1.getAName() + reu.getAName() + ret.getAName());
                    }
                }else {
                    res.setDistrictCode(reu.getAName()+ret.getAName());
                }

            }else{
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String auditTime = format.format(date);
        hsyMerchantAuditRequest.setAuditTime(auditTime);
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
    public void stepChange(Long uid) {
        hsyMerchantAuditDao.stepChange(uid);
    }

    @Override
    public long getUid(Long id) {
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
                    if (list.get(i).getWithDrawStatus()==3){
                        list.get(i).setStat("提交结算待审核");
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

    @Override
    public List<HsyQueryMerchantResponse> hsyMerchantList(HsyQueryMerchantRequest request) {
        List<HsyQueryMerchantResponse> list = this.hsyMerchantAuditDao.hsyMerchantList(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getCreateTime()!=null){
                    String dates = sdf.format(list.get(i).getCreateTime());
                    list.get(i).setCreateTimes(dates);
                }

                if (list.get(i).getStatus()==1){
                    list.get(i).setStatusValue("审核已通过");
                }
                if (list.get(i).getStatus()==2){
                    list.get(i).setStatusValue("待审核");
                }
                if (list.get(i).getStatus()==3){
                    list.get(i).setStatusValue("审核未通过");
                }
                if (list.get(i).getStatus()==4){
                    list.get(i).setStatusValue("商户已注册");
                }

                String districtCode =list.get(i).getDistrictCode();
                if (districtCode!=null&&!districtCode.equals("")){
                    HsyMerchantAuditResponse ret = hsyMerchantAuditDao.getCode(districtCode);

                    if (!ret.getParentCode().equals("0")){
                        HsyMerchantAuditResponse reu = hsyMerchantAuditDao.getCity(ret.getParentCode());
                        if (!reu.getParentCode().equals("0")){
                            HsyMerchantAuditResponse reu1 = hsyMerchantAuditDao.getCityOnly(reu.getParentCode());
                            list.get(i).setProvince(reu1.getAName()+reu.getAName()+ret.getAName());
                        }else {
                            list.get(i).setProvince(reu.getAName()+ret.getAName());
                        }
                    }
                    if(ret.getParentCode().equals("0")){
                        HsyMerchantAuditResponse reu = hsyMerchantAuditDao.getCityOnly(ret.getCode());
                        list.get(i).setProvince(reu.getAName());
                    }
                }
                int industryCode = Integer.parseInt(list.get(i).getIndustryCode());
                if (industryCode==1000){
                    list.get(i).setIndustry(IndustryCodeType.CATERING.industryCodeValue);
                }
                if (industryCode==1001){
                    list.get(i).setIndustry(IndustryCodeType.SUPERMARKET.industryCodeValue);
                }
                if (industryCode==1002){
                    list.get(i).setIndustry(IndustryCodeType.LIFE_SERVICES.industryCodeValue);
                }
                if (industryCode==1003){
                    list.get(i).setIndustry(IndustryCodeType.SHOPPING.industryCodeValue);
                }
                if (industryCode==1004){
                    list.get(i).setIndustry(IndustryCodeType.BEAUTY.industryCodeValue);
                }
                if (industryCode==1005){
                    list.get(i).setIndustry(IndustryCodeType.EXERCISE.industryCodeValue);
                }
                if (industryCode==1006){
                    list.get(i).setIndustry(IndustryCodeType.HOTEL.industryCodeValue);
                }
            }
        }
        return list;
    }

    @Override
    public int hsyMerchantListCount(HsyQueryMerchantRequest request) {
        return this.hsyMerchantAuditDao.hsyMerchantListCount(request);
    }

    @Override
    public List<HsyQueryMerchantResponse> hsyMerchantSecondList(HsyQueryMerchantRequest request) {
        List<HsyQueryMerchantResponse> list = this.hsyMerchantAuditDao.hsyMerchantSecondList(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getCreateTime()!=null){
                    String dates = sdf.format(list.get(i).getCreateTime());
                    list.get(i).setCreateTimes(dates);
                }

                if (list.get(i).getStatus()==1){
                    list.get(i).setStatusValue("审核已通过");
                }
                if (list.get(i).getStatus()==2){
                    list.get(i).setStatusValue("待审核");
                }
                if (list.get(i).getStatus()==3){
                    list.get(i).setStatusValue("审核未通过");
                }
                if (list.get(i).getStatus()==4){
                    list.get(i).setStatusValue("商户已注册");
                }

                String districtCode =list.get(i).getDistrictCode();
                if (districtCode!=null&&!districtCode.equals("")){
                    HsyMerchantAuditResponse ret = hsyMerchantAuditDao.getCode(districtCode);

                    if (!ret.getParentCode().equals("0")){
                        HsyMerchantAuditResponse reu = hsyMerchantAuditDao.getCity(ret.getParentCode());
                        if (!reu.getParentCode().equals("0")){
                            HsyMerchantAuditResponse reu1 = hsyMerchantAuditDao.getCityOnly(reu.getParentCode());
                            list.get(i).setProvince(reu1.getAName()+reu.getAName()+ret.getAName());
                        }else {
                            list.get(i).setProvince(reu.getAName()+ret.getAName());
                        }
                    }
                    if(ret.getParentCode().equals("0")){
                        HsyMerchantAuditResponse reu = hsyMerchantAuditDao.getCityOnly(ret.getCode());
                        list.get(i).setProvince(reu.getAName());
                    }
                }
                int industryCode = Integer.parseInt(list.get(i).getIndustryCode());
                if (industryCode==1000){
                    list.get(i).setIndustry(IndustryCodeType.CATERING.industryCodeValue);
                }
                if (industryCode==1001){
                    list.get(i).setIndustry(IndustryCodeType.SUPERMARKET.industryCodeValue);
                }
                if (industryCode==1002){
                    list.get(i).setIndustry(IndustryCodeType.LIFE_SERVICES.industryCodeValue);
                }
                if (industryCode==1003){
                    list.get(i).setIndustry(IndustryCodeType.SHOPPING.industryCodeValue);
                }
                if (industryCode==1004){
                    list.get(i).setIndustry(IndustryCodeType.BEAUTY.industryCodeValue);
                }
                if (industryCode==1005){
                    list.get(i).setIndustry(IndustryCodeType.EXERCISE.industryCodeValue);
                }
                if (industryCode==1006){
                    list.get(i).setIndustry(IndustryCodeType.HOTEL.industryCodeValue);
                }
            }
        }
        return list;
    }

    @Override
    public int hsyMerchantSecondListCount(HsyQueryMerchantRequest request) {
        return this.hsyMerchantAuditDao.hsyMerchantSecondListCount(request);
    }

    @Override
    public void saveLog(String username, Long id, String checkErrorInfo,int withDrawStatus) {
        this.hsyMerchantAuditDao.saveLog(username,id,checkErrorInfo,withDrawStatus);
    }

    @Override
    public List<HsyMerchantInfoCheckRecord> getLog(Long id) {
        List<HsyMerchantInfoCheckRecord> list = this.hsyMerchantAuditDao.getLog(id);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getCreateTime()!=null&&!list.get(i).getCreateTime().equals("")){
                    String dates = format.format(list.get(i).getCreateTime());
                    list.get(i).setCreateTimes(dates);
                }
                if (list.get(i).getStatus()==0){
                    list.get(i).setStat("审核成功");
                }
                if (list.get(i).getStatus()==1){
                    list.get(i).setStat("审核失败");
                }
            }
        }
        return list;
    }

    public List<HsyMerchantAuditResponse> hsyMerchant(HsyMerchantAuditRequest hsyMerchantAuditRequest) throws ParseException {
        List<HsyMerchantAuditResponse> list = hsyMerchantAuditDao.hsyMerchant(hsyMerchantAuditRequest);
        this.addActCode(list);
        System.out.print("----------------------");
        System.out.print(new Date());
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.size()>0){
                    list.get(i).setStat(WithdrawStatus.of(list.get(i).getWithDrawStatus()).getValue());
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

    private void addActCode(List<HsyMerchantAuditResponse> list) {
        List<Long>  userIds = Lists.transform(list, new Function<HsyMerchantAuditResponse, Long>() {
            @Override
            public Long apply(HsyMerchantAuditResponse input) {
                return input.getUid();
            }
        });
        final List<UserChannelPolicy> userChannelPolicies1 = this.UserChannelPolicyDao.selectByUserIdsAndChannelSignAndType(userIds, EnumPayChannelSign.SYJ_WECHAT.getId(), EnumSettlePeriodType.T1.getId());
        final Map<Long, UserChannelPolicy> map1 = Maps.uniqueIndex(userChannelPolicies1, new Function<UserChannelPolicy, Long>() {
            @Override
            public Long apply(UserChannelPolicy input) {
                return input.getUserId();
            }
        });
        final List<UserChannelPolicy> userChannelPolicies2 = this.UserChannelPolicyDao.selectByUserIdsAndChannelSignAndType(userIds, EnumPayChannelSign.SYJ_ALIPAY.getId(), EnumSettlePeriodType.T1.getId());
        final Map<Long, UserChannelPolicy> map2 = Maps.uniqueIndex(userChannelPolicies2, new Function<UserChannelPolicy, Long>() {
            @Override
            public Long apply(UserChannelPolicy input) {
                return input.getUserId();
            }
        });
        final List<UserChannelPolicy> userChannelPolicies3 = this.UserChannelPolicyDao.selectByUserIdsAndChannelSignAndType(userIds, EnumPayChannelSign.XMMS_WECHAT_T1.getId(), EnumSettlePeriodType.T1.getId());
        final Map<Long, UserChannelPolicy> map3 = Maps.uniqueIndex(userChannelPolicies3, new Function<UserChannelPolicy, Long>() {
            @Override
            public Long apply(UserChannelPolicy input) {
                return input.getUserId();
            }
        });
        final List<UserChannelPolicy> userChannelPolicies4 = this.UserChannelPolicyDao.selectByUserIdsAndChannelSignAndType(userIds, EnumPayChannelSign.XMMS_ALIPAY_T1.getId(), EnumSettlePeriodType.T1.getId());
        final Map<Long, UserChannelPolicy> map4 = Maps.uniqueIndex(userChannelPolicies4, new Function<UserChannelPolicy, Long>() {
            @Override
            public Long apply(UserChannelPolicy input) {
                return input.getUserId();
            }
        });
        for (HsyMerchantAuditResponse response : list){
            //hx
            response.setHxWx(map1.get(response.getUid()) == null ?  "" : map1.get(response.getUid()).getExchannelEventCode());
            response.setMsWx(map3.get(response.getUid()) == null ?  "" : map3.get(response.getUid()).getExchannelEventCode());
            response.setHxZfb(map2.get(response.getUid()) == null ?  "" : map2.get(response.getUid()).getExchannelEventCode());
            response.setMsZfb(map4.get(response.getUid()) == null ?  "" : map4.get(response.getUid()).getExchannelEventCode());
        }

    }

    @Override
    public String downLoadHsyMerchant(HsyMerchantAuditRequest hsyMerchantAuditRequest, String baseUrl) throws ParseException {
        final String tempDir = this.getTempDir();
        final File excelFile = new File(tempDir + File.separator + ".xls");
        final ExcelSheetVO excelSheet = generateCodeExcelSheet(hsyMerchantAuditRequest,baseUrl);
        final List<ExcelSheetVO> excelSheets = new ArrayList<>();
        excelSheets.add(excelSheet);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(excelFile);
            ExcelUtil.exportExcel(excelSheets, fileOutputStream);
            return excelFile.getAbsolutePath();
        } catch (final Exception e) {
            log.error("download hsyMerchant record error", e);
            e.printStackTrace();
        }  finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (final IOException e) {
                    log.error("close fileOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    @Override
    public void changeMobile(Long uid,String changePhone) {
        this.hsyMerchantAuditDao.changeMobile(uid,changePhone);
    }

    @Override
    public HsyMerchantAuditResponse getCellphon(Long id) {
        HsyMerchantAuditResponse req = this.hsyMerchantAuditDao.getCellphon(id);
        return req;
    }

    @Override
    public void updatePhone(String changePhone, Long uid) {
        this.hsyMerchantAuditDao.updatePhone(changePhone,uid);
    }

    @Override
    public void updateModifyInfo(HsyMerchantAuditRequest hsyMerchantAuditRequest) {
        this.hsyMerchantAuditDao.updateModifyInfo(hsyMerchantAuditRequest);
    }

    @Override
    public int getStatuts(Long id) {
        return this.hsyMerchantAuditDao.getStatuts(id);
    }

    @Override
    public List<ShopInfoResponse> getShopInfo(Long id) {
        List<ShopInfoResponse> list = this.hsyMerchantAuditDao.getShopInfo(id);
        if (list.size()>0){
            for (int i=0;i<list.size();i++) {
                String districtCode = list.get(i).getDistrictCode();
                if (districtCode != null && !districtCode.equals("")) {
                    HsyMerchantAuditResponse ret = hsyMerchantAuditDao.getCode(districtCode);

                    if (!ret.getParentCode().equals("0")) {
                        HsyMerchantAuditResponse reu = hsyMerchantAuditDao.getCity(ret.getParentCode());
                        if (!reu.getParentCode().equals("0")) {
                            HsyMerchantAuditResponse reu1 = hsyMerchantAuditDao.getCityOnly(reu.getParentCode());
                            list.get(i).setDistrictCode(reu1.getAName() + reu.getAName() + ret.getAName());
                        } else {
                            list.get(i).setDistrictCode(reu.getAName() + ret.getAName());
                        }
                    }
                    if (ret.getParentCode().equals("0")) {
                        HsyMerchantAuditResponse reu = hsyMerchantAuditDao.getCityOnly(ret.getCode());
                        list.get(i).setDistrictCode(reu.getAName());
                    }
                }
            }
        }
        return list;
    }

    /**
     * 修改商户d0提现
     *
     * @param userD0WithdrawReques
     */
    @Override
    public int modifyD0withdraw(UserD0WithdrawRequest userD0WithdrawReques) {
        return this.hsyMerchantAuditDao.modifyD0withdraw(userD0WithdrawReques);
    }

    /**
     * 获取临时路径
     *
     * @return
     */
    public static String getTempDir() {
        final String dir = System.getProperty("java.io.tmpdir") + "hss" + File.separator + "hsyMerchant" + File.separator + "record";
        final File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    private ExcelSheetVO generateCodeExcelSheet(HsyMerchantAuditRequest hsyMerchantAuditRequest, String baseUrl) throws ParseException {
        List<HsyMerchantAuditResponse> list = hsyMerchant(hsyMerchantAuditRequest);
        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<List<String>> datas = new ArrayList<List<String>>();
        final ArrayList<String> heads = new ArrayList<>();
        excelSheetVO.setName("hsyMerchant");
        heads.add("商户编号");
        heads.add("商户全称");
        heads.add("商户简称");
        heads.add("店铺数量");
        heads.add("所属分公司");
        heads.add("所属一级代理");
        heads.add("所属二级代理");
        heads.add("报单员");
        heads.add("姓名");
        heads.add("注册时间");
        heads.add("审核时间");
        heads.add("注册手机号");
        heads.add("省市");
        heads.add("行业");
        heads.add("状态");
        heads.add("华夏微信");
        heads.add("民生微信");
        heads.add("华夏支付宝");
        heads.add("民生支付宝");
        datas.add(heads);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                ArrayList<String> columns = new ArrayList<>();
                columns.add(list.get(i).getGlobalID());
                columns.add(list.get(i).getName());
                columns.add(list.get(i).getShortName());
                columns.add(list.get(i).getShopNo());
                columns.add(list.get(i).getDealerBelong());
                columns.add(list.get(i).getProxyName());
                columns.add(list.get(i).getProxyName1());
                columns.add(list.get(i).getUsername());
                columns.add(list.get(i).getRealname());
                if (list.get(i).getCreateTime()!= null && !"".equals(list.get(i).getCreateTime())){
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String st = df.format(list.get(i).getCreateTime());
                    columns.add(st);

                }else {
                    columns.add("");
                }
                if (list.get(i).getAuditTime()!= null && !"".equals(list.get(i).getAuditTime())){
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String st = df.format(list.get(i).getAuditTime());
                    columns.add(st);

                }else {
                    columns.add("");
                }
                columns.add(list.get(i).getCellphone());
                columns.add(list.get(i).getDistrictCode());
                columns.add(list.get(i).getIndustryCode());
                columns.add(list.get(i).getStat());
                columns.add(list.get(i).getHxWx());
                columns.add(list.get(i).getMsWx());
                columns.add(list.get(i).getHxZfb());
                columns.add(list.get(i).getMsZfb());
                datas.add(columns);
            }
        }
        excelSheetVO.setDatas(datas);
        return excelSheetVO;
    }


}
