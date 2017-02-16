package com.jkm.hss.bill.service.impl;

import com.jkm.hss.bill.dao.ShareProfitDao;
import com.jkm.hss.bill.entity.JkmProfitDetailsResponse;
import com.jkm.hss.bill.entity.JkmProfitResponse;
import com.jkm.hss.bill.service.ShareProfitService;
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
    public List<JkmProfitResponse> selectAccountList(JkmProfitRequest req) {
        List<JkmProfitResponse> list = shareProfitDao.selectAccountList(req);
        return list;
    }

    @Override
    public int selectAccountListCount(JkmProfitRequest req) {
        return shareProfitDao.selectAccountListCount(req);
    }

    @Override
    public List<JkmProfitDetailsResponse> selectProfitDetails() {
        List<JkmProfitDetailsResponse> list = shareProfitDao.selectProfitDetails();
        return list;
    }
}
