package com.jkm.hss.bill.service.impl;

import com.jkm.hss.bill.dao.ShareProfitDao;
import com.jkm.hss.bill.entity.MerchantTradeResponse;
import com.jkm.hss.bill.service.ShareProfitService;
import com.jkm.hss.merchant.helper.request.OrderTradeRequest;
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
    public List<MerchantTradeResponse> selectAccountList(OrderTradeRequest req) {
        List<MerchantTradeResponse> list = shareProfitDao.selectAccountList(req);
        return list;
    }

    @Override
    public long selectAccountListCount(OrderTradeRequest req) {
        return shareProfitDao.selectAccountListCount(req);
    }
}
