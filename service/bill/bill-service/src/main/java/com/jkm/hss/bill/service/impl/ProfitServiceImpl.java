package com.jkm.hss.bill.service.impl;

import com.jkm.hss.bill.dao.ProfitDao;
import com.jkm.hss.bill.entity.JkmProfitDetailsResponse;
import com.jkm.hss.bill.enums.EnumBusinessType;
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
        if (list!=null){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getBusinessType().equals("hssPay")){
                    list.get(i).setBusinessType(EnumBusinessType.HSSPAY.getValue());
                }
                if (list.get(i).getBusinessType().equals("hssWithdraw")){
                    list.get(i).setBusinessType(EnumBusinessType.HSSWITHDRAW.getValue());
                }
                if (list.get(i).getBusinessType().equals("hssUpgrade")){
                    list.get(i).setBusinessType(EnumBusinessType.HSSPROMOTE.getValue());
                }
                if (list.get(i).getBusinessType().equals("hsyPay")){
                    list.get(i).setBusinessType(EnumBusinessType.HSYPAY.getValue());
                }
            }
        }
        return list;
    }

    @Override
    public int selectProfitDetailsCount(ProfitDetailsRequest req) {
        return  profitDao.selectProfitDetailsCount(req);
    }
}
