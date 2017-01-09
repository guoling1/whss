package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.MerchantInfoQueryDao;
import com.jkm.hss.merchant.entity.MerchantInfoRequest;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import com.jkm.hss.merchant.service.MerchantInfoQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangbin on 2016/11/27.
 */
@Slf4j
@Service
public class MerchantInfoQueryServiceImpl implements MerchantInfoQueryService {

    @Autowired
    private MerchantInfoQueryDao merchantInfoQueryDao;


    @Override
    public List<MerchantInfoResponse> getAll(MerchantInfoRequest req) {

        List<MerchantInfoResponse> list = this.merchantInfoQueryDao.getAll(req);
        return list;
    }

    @Override
    public int getCount(MerchantInfoRequest req) {
//        Map<String,Object> map = new HashMap<String,Object>();
//        map.put("merchantName",merchantInfoResponse.getMerchantName());
//        map.put("status",merchantInfoResponse.getStatus());
//        map.put("offset",merchantInfoResponse.getOffset());
        int count = merchantInfoQueryDao.getCount(req);
        return count;
    }

    @Override
    public List<MerchantInfoResponse> getRecord(MerchantInfoRequest req) {

        List<MerchantInfoResponse> list = this.merchantInfoQueryDao.getRecord(req);
        if (list.size()>0){
            for (int i=0;list.size()>i;i++){
                if (list.get(i).getLevel()==1){
                    list.get(i).setProxyName(list.get(i).getProxyName());
                }
                if (list.get(i).getLevel()==2){
                    MerchantInfoResponse res = merchantInfoQueryDao.getProxyName(list.get(i).getFirstLevelDealerId());
                    list.get(i).setProxyName1(res.getProxyName());
                }
            }
        }
        return list;
    }

    @Override
    public int getCountRecord(MerchantInfoRequest req) {
        int count = this.merchantInfoQueryDao.getCountRecord(req);

        return count;
    }


}
