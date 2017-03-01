package com.jkm.hss.bill.service.impl;

import com.jkm.hss.account.enums.EnumSplitBusinessType;
import com.jkm.hss.bill.dao.ProfitDao;
import com.jkm.hss.bill.entity.JkmProfitDetailsResponse;
import com.jkm.hss.bill.service.ProfitService;
import com.jkm.hss.merchant.helper.request.ProfitDetailsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        ProfitDetailsRequest request =selectTime(req);
        List<JkmProfitDetailsResponse> list = profitDao.selectProfitDetails(request);
        if (list!=null){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getBusinessType().equals("hssPay")){
                    list.get(i).setBusinessType(EnumSplitBusinessType.HSSPAY.getValue());
                }
                if (list.get(i).getBusinessType().equals("hssWithdraw")){
                    list.get(i).setBusinessType(EnumSplitBusinessType.HSSWITHDRAW.getValue());
                }
                if (list.get(i).getBusinessType().equals("hssUpgrade")){
                    list.get(i).setBusinessType(EnumSplitBusinessType.HSSPROMOTE.getValue());
                }
                if (list.get(i).getBusinessType().equals("hsyPay")){
                    list.get(i).setBusinessType(EnumSplitBusinessType.HSYPAY.getValue());
                }
            }
        }
        return list;
    }

    private ProfitDetailsRequest selectTime(ProfitDetailsRequest req) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt= new Date();

        try {
            String d = sdf.format(dt);
            dt = sdf.parse(d);
            req.setSplitDate(dt);
        } catch (ParseException e) {
            log.debug("时间转换异常");
            e.printStackTrace();
        }
        return req;
    }

    @Override
    public int selectProfitDetailsCount(ProfitDetailsRequest req) {
        ProfitDetailsRequest request =selectTime(req);
        return  profitDao.selectProfitDetailsCount(request);
    }
}
