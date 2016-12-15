package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.QueryMerchantInfoRecordDao;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.QueryMerchantInfoRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<MerchantInfoResponse> getAll(MerchantInfoResponse merchantInfo) {
        List<MerchantInfoResponse> list = this.queryMerchantInfoRecordDao.getAll(merchantInfo);
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                if (list.get(i).getMobile()!=null&&"".equals(list.get(i).getMobile())){
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
    public List<MerchantInfoResponse> getResults(long firstLevelDealerId) {
        List<MerchantInfoResponse> res = this.queryMerchantInfoRecordDao.getResults(firstLevelDealerId);
        return res;

    }
}
