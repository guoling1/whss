package com.jkm.hss.bill.service.impl;

import com.jkm.hss.account.entity.Account;
import com.jkm.hss.bill.dao.ShareProfitDao;
import com.jkm.hss.bill.entity.AccountDetailsResponse;
import com.jkm.hss.bill.service.ShareProfitService;
import com.jkm.hss.merchant.entity.ProfitDetailsRequest;
import com.jkm.hss.merchant.helper.request.JkmProfitRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangbin on 2017/2/13.
 */
@Slf4j
@Service
public class ShareProfitServiceImpl implements ShareProfitService {

    @Autowired
    private ShareProfitDao shareProfitDao;



    @Override
    public List<Account> selectAccountList(JkmProfitRequest req) {
        List<Account> list = shareProfitDao.selectAccountList(req);
        return list;
    }

    @Override
    public int selectAccountListCount(JkmProfitRequest req) {
        return shareProfitDao.selectAccountListCount(req);
    }

    @Override
    public List<AccountDetailsResponse> selectAccountDetails(ProfitDetailsRequest req) {
        List<AccountDetailsResponse> list = shareProfitDao.selectAccountDetails(req);
        return list;
    }

    @Override
    public int selectAccountDetailsCount(ProfitDetailsRequest req) {
        return shareProfitDao.selectAccountDetailsCount(req);
    }

    @Override
    public Account getChannel() {
        Account result = this.shareProfitDao.getChannel();
        return result;
    }

    @Override
    public List<AccountDetailsResponse> selectChannelDetails(ProfitDetailsRequest req) {
        List<AccountDetailsResponse> list = this.shareProfitDao.selectChannelDetails(req);
        return list;
    }

    @Override
    public int selectChannelDetailsCount(ProfitDetailsRequest req) {
        return this.shareProfitDao.selectChannelDetailsCount(req);
    }


}
