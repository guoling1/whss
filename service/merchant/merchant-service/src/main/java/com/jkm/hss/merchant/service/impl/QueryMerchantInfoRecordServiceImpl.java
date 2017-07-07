package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.QueryMerchantInfoRecordDao;
import com.jkm.hss.merchant.entity.*;
import com.jkm.hss.merchant.enums.EnumSource;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.QueryMerchantInfoRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangbin on 2016/12/2.
 */
@Slf4j
@Service
public class QueryMerchantInfoRecordServiceImpl implements QueryMerchantInfoRecordService {

    @Autowired
    private QueryMerchantInfoRecordDao queryMerchantInfoRecordDao;

    @Override
    public List<MerchantInfoResponse> getAll(MerchantInfoResponse merchantInfo) throws ParseException {
        List<MerchantInfoResponse> list = this.queryMerchantInfoRecordDao.getAll(merchantInfo);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                if (list.get(i).getFirstMerchantId()>0){
                    MerchantInfoResponse response = queryMerchantInfoRecordDao.selectProxyNameYq(list.get(i).getFirstMerchantId());
                    if (response.getFirstDealerId()>0){
                        MerchantInfoResponse responseTJ = queryMerchantInfoRecordDao.selectProxyNameTJ(list.get(i).getFirstDealerId());
                        list.get(i).setProxyNameYq(responseTJ.getProxyName());
                    }
                    list.get(i).setRecommenderCode(response.getMarkCode());
                    list.get(i).setRecommenderName(response.getMerchantName());
                    list.get(i).setRecommenderPhone(response.getMobile());
                }
                if (list.get(i).getSecondMerchantId()>0){
                    MerchantInfoResponse response = queryMerchantInfoRecordDao.selectProxyNameYq1(list.get(i).getSecondMerchantId());
                    if (list.get(i).getSecondDealerId()>0){
                        MerchantInfoResponse responseTJ = queryMerchantInfoRecordDao.selectProxyNameTJ1(list.get(i).getSecondDealerId());
                        list.get(i).setProxyNameYq1(responseTJ.getProxyName());
                    }
                    list.get(i).setRecommenderCode(response.getMarkCode());
                    list.get(i).setRecommenderName(response.getMerchantName());
                    list.get(i).setRecommenderPhone(response.getMobile());
                }
                if (list.get(i).getMobile()!=null&&!"".equals(list.get(i).getMobile())){
                    list.get(i).setMobile(MerchantSupport.decryptMobile(list.get(i).getMobile()));
                }
                if(list.get(i).getReserveMobile()!=null&&!"".equals(list.get(i).getReserveMobile())){
                    list.get(i).setReserveMobile(MerchantSupport.decryptMobile(list.get(i).getReserveMobile()));
                }
                if(list.get(i).getIdentity()!=null&&!"".equals(list.get(i).getIdentity())){
                    list.get(i).setIdentity(MerchantSupport.decryptIdentity(list.get(i).getIdentity()));
                }
                if(list.get(i).getBankNo()!=null&&!"".equals(list.get(i).getBankNo())){
                    list.get(i).setBankNo(MerchantSupport.decryptBankCard(list.get(i).getBankNo()));
                }
                if (list.get(i).getAuthenticationTime()!=null&&!"".equals(list.get(i).getAuthenticationTime())){
                    String AuthenticationTime = list.get(i).getAuthenticationTime();
                    Date date =formatter.parse(AuthenticationTime);
                    String authenticationTime = formatter.format(date);
                    list.get(i).setAuthenticationTime(authenticationTime);
                }
                if (list.get(i).getCheckedTime()!=null&&!"".equals(list.get(i).getCheckedTime())){
                    String CheckedTime = list.get(i).getCheckedTime();
                    Date date =formatter.parse(CheckedTime);
                    String checkedTime = formatter.format(date);
                    list.get(i).setCheckedTime(checkedTime);
                }
                if (list.get(i).getSource()==0){
                    list.get(i).setRegistered(EnumSource.SCAN.getValue());
                }
                if (list.get(i).getSource()==1){
                    list.get(i).setRegistered(EnumSource.RECOMMEND.getValue());
                }
                if (list.get(i).getSource()==2){
                    list.get(i).setRegistered(EnumSource.DEALERRECOMMEND.getValue());
                }
                if (list.get(i).getIsAuthen()!=null&&!list.get(i).getIsAuthen().equals("")){
                    if (list.get(i).getIsAuthen().equals("1")){
                        list.get(i).setIsAuthen("认证通过");
                    }else {
                        list.get(i).setIsAuthen("认证未通过");
                    }
                }else {
                    list.get(i).setIsAuthen("认证未通过");
                }


            }
        }
        return list;
    }


    @Override
    public List<LogResponse> getLog(MerchantInfoResponse merchantInfo) throws ParseException {
        List<LogResponse> res = this.queryMerchantInfoRecordDao.getLog(merchantInfo);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (res.size()>0){
            for (int i=0;res.size()>i;i++){
                if (res.get(i).getCreateTime()!=null&&!"".equals(res.get(i).getCreateTime())){
                    String CreateTime = res.get(i).getCreateTime();
                    Date date =formatter.parse(CreateTime);
                    String createTime = formatter.format(date);
                    res.get(i).setCreateTime(createTime);
                }
            }
        }
        return res;
    }

    @Override
    public ReferralResponse getRefInformation(long id) {
        ReferralResponse refInformation = this.queryMerchantInfoRecordDao.getRefInformation(id);
        return refInformation;
    }

    @Override
    public SettleResponse getSettle(long id) {
        SettleResponse lst = this.queryMerchantInfoRecordDao.getSettle(id);
        return lst;
    }

    @Override
    public MerchantInfoResponse getrecommenderInfo(long id) {
        MerchantInfoResponse response = this.queryMerchantInfoRecordDao.getrecommenderInfo(id);
        return response;
    }

    @Override
    public void saveNo(SaveLineNoRequest req) {
        this.queryMerchantInfoRecordDao.saveNo(req);
    }

    @Override
    public void saveNo1(SaveLineNoRequest req) {
        this.queryMerchantInfoRecordDao.saveNo1(req);
    }

    @Override
    public int getStatus(long id) {
        return this.queryMerchantInfoRecordDao.getStatus(id);
    }

    @Override
    public int getAccountId(long id) {
        return this.queryMerchantInfoRecordDao.getAccountId(id);
    }
}
