package com.jkm.hss.bill.service.impl;

import com.jkm.hss.bill.dao.ProfitDao;
import com.jkm.hss.bill.entity.JkmProfitDetailsResponse;
import com.jkm.hss.bill.service.ProfitService;
import com.jkm.hss.merchant.helper.request.ProfitDetailsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangbin on 2017/2/13.
 */
@Slf4j
@Service
public class ProfitServiceImpl implements ProfitService {

    @Autowired
    private ProfitDao profitDao;

    @Override
    public List<JkmProfitDetailsResponse> selectProfitDetails(ProfitDetailsRequest req) {
        List<JkmProfitDetailsResponse> list = profitDao.selectProfitDetails(req);
        return list;
    }

    @Override
    public int selectProfitDetailsCount(ProfitDetailsRequest req) {
        return  profitDao.selectProfitDetailsCount(req);
    }
}
