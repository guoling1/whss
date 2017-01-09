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
        int count = merchantInfoQueryDao.getCount(req);
        return count;
    }

    @Override
    public List<MerchantInfoResponse> getRecord(MerchantInfoRequest req) {

        List<MerchantInfoResponse> list = this.merchantInfoQueryDao.getRecord(req);
        return list;
    }

    @Override
    public int getCountRecord(MerchantInfoRequest req) {
        int count = this.merchantInfoQueryDao.getCountRecord(req);

        return count;
    }


}
