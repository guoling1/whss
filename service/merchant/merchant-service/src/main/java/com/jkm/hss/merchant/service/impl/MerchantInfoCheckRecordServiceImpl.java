package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.MerchantInfoCheckRecordDao;
import com.jkm.hss.merchant.helper.request.RequestMerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoCheckRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhangbin on 2016/11/23.
 */
@Slf4j
@Service
public class MerchantInfoCheckRecordServiceImpl implements MerchantInfoCheckRecordService {

    @Autowired
    private MerchantInfoCheckRecordDao merchantInfoCheckRecordDao;

    @Override
    public int update(RequestMerchantInfo requestMerchantInfo) {
        return this.merchantInfoCheckRecordDao.update(requestMerchantInfo);
    }


    @Override
    public void save(RequestMerchantInfo requestMerchantInfo) {
        this.merchantInfoCheckRecordDao.insert(requestMerchantInfo);
    }

    @Override
    @Transactional
    public int updateStatus(RequestMerchantInfo requestMerchantInfo) {
        return this.merchantInfoCheckRecordDao.updateStatus(requestMerchantInfo);
    }

    @Override
    public int getStauts(long merchantId) {
        return this.merchantInfoCheckRecordDao.getStauts(merchantId);
    }

    @Override
    public void deletAccount(long accountId) {
        this.merchantInfoCheckRecordDao.deletAccount(accountId);
    }

    @Override
    public void deletIl(long res) {
        this.merchantInfoCheckRecordDao.deletIl(res);
    }

    @Override
    public String selectById(long merchantId) {
        return this.merchantInfoCheckRecordDao.selectById(merchantId);
    }

    @Override
    public int getId(long merchantId) {
        return this.merchantInfoCheckRecordDao.getId(merchantId);
    }


}
