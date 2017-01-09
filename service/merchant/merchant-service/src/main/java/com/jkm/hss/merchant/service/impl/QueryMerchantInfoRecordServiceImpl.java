package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.QueryMerchantInfoRecordDao;
import com.jkm.hss.merchant.entity.LogResponse;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
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

            }
        }
        return list;
    }

    @Override
    public List<MerchantInfoResponse> getLevel(long dealerId) {
        List<MerchantInfoResponse> res = this.queryMerchantInfoRecordDao.getLevel(dealerId);
        return res;
    }

    @Override
    public List<MerchantInfoResponse> getResults(int level,long dealerId) {
        List<MerchantInfoResponse> res = this.queryMerchantInfoRecordDao.getResults(level,dealerId);
        return res;

    }

    @Override
    public List<MerchantInfoResponse> getFirstLevel(long firstLevelDealerId) {
        List<MerchantInfoResponse> res = this.queryMerchantInfoRecordDao.getFirstLevel(firstLevelDealerId);
        return res;
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
}
