package com.jkm.hss.bill.service.impl;

import com.jkm.hss.account.enums.EnumSplitAccountUserType;
import com.jkm.hss.account.enums.EnumSplitBusinessType;
import com.jkm.hss.bill.dao.ProfitDao;
import com.jkm.hss.bill.entity.JkmProfitDetailsResponse;
import com.jkm.hss.bill.entity.ProfitResponse;
import com.jkm.hss.bill.service.ProfitService;
import com.jkm.hss.merchant.entity.ProfitDetailsRequest;
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
                if (list.get(i).getOutMoneyAccountId()==1){
                    list.get(i).setOutMoneyAccountName("好收收手续费账户");
                }
                if (list.get(i).getOutMoneyAccountId()==2){
                    list.get(i).setOutMoneyAccountName("金开门收费账户");
                }
                if (list.get(i).getOutMoneyAccountId()==11){
                    list.get(i).setOutMoneyAccountName("代理商提现手续费账户");
                }
                if (list.get(i).getOutMoneyAccountId()==48){
                    list.get(i).setOutMoneyAccountName("通道账户");
                }
                if (list.get(i).getOutMoneyAccountId()==49){
                    list.get(i).setOutMoneyAccountName("产品账户");
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

    @Override
    public JkmProfitDetailsResponse profitAmount(ProfitDetailsRequest req) {
        ProfitDetailsRequest request =selectTime(req);
        JkmProfitDetailsResponse res = profitDao.profitAmount(request);
        return res;
    }

    @Override
    public List<ProfitResponse> getInfo(String businessOrderNo) {
        List<ProfitResponse> list = this.profitDao.getInfo(businessOrderNo);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getAccountUserType()==1){
                    list.get(i).setAccountUserTypes(EnumSplitAccountUserType.JKM.getValue());
                }
                if (list.get(i).getAccountUserType()==2){
                    list.get(i).setAccountUserTypes(EnumSplitAccountUserType.MERCHANT.getValue());
                }
                if (list.get(i).getAccountUserType()==3){
                    list.get(i).setAccountUserTypes(EnumSplitAccountUserType.FIRST_DEALER.getValue());
                }
                if (list.get(i).getAccountUserType()==4){
                    list.get(i).setAccountUserTypes(EnumSplitAccountUserType.SECOND_DEALER.getValue());
                }
                if (list.get(i).getSplitDate()!=null){
                    String dates = sdf.format(list.get(i).getSplitDate());
                    list.get(i).setSplitDates(dates);
                }
            }
        }
        return list;
    }
}
