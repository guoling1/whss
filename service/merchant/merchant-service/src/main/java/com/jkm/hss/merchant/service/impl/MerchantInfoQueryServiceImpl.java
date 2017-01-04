package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.dao.MerchantInfoDao;
import com.jkm.hss.merchant.dao.MerchantInfoQueryDao;
import com.jkm.hss.merchant.entity.MerchantInfo;
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

    @Autowired
    private MerchantInfoDao merchantInfoDao;

    @Override
    public Optional<MerchantInfo> selectById(long id) {
        Optional<MerchantInfo> optionalSelectId = this.merchantInfoQueryDao.selectById(id);
        return optionalSelectId;
    }

    @Override
    public Optional<MerchantInfo> selectByStatus(int status) {
        Optional<MerchantInfo> optionalSelectStatus = this.merchantInfoQueryDao.selectByStatus(status);
        return optionalSelectStatus;
    }

    @Override
    public Optional<MerchantInfo> selectByDealerId(long dealerId) {
        Optional<MerchantInfo> optionalSelectDealerId = this.merchantInfoQueryDao.selectByDealerId(dealerId);
        return optionalSelectDealerId;
    }

    @Override
    public List<MerchantInfoResponse> getAll(MerchantInfoResponse merchantInfoResponse) {

        List<MerchantInfoResponse> list = this.merchantInfoQueryDao.getAll(merchantInfoResponse);

        return list;
    }

    @Override
    public List<MerchantInfoResponse> selectByName(int pageNo, int pageSize, String merchantName) {

        List<MerchantInfoResponse> list = this.merchantInfoQueryDao.selectByName(pageNo,pageSize,merchantName);
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
    public List<MerchantInfoResponse> getCount() {

        List<MerchantInfoResponse> list = this.merchantInfoQueryDao.getCount();

        return list;
    }

    @Override
    public List<MerchantInfoResponse> getRecord(MerchantInfoResponse merchantInfoResponse) {

        List<MerchantInfoResponse> list = this.merchantInfoQueryDao.getRecord(merchantInfoResponse);
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
}
