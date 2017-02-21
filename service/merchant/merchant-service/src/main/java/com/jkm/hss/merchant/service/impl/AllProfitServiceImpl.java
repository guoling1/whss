package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.account.enums.EnumSplitBusinessType;
import com.jkm.hss.merchant.dao.AllProfitDao;
import com.jkm.hss.merchant.helper.request.CompanyPrifitRequest;
import com.jkm.hss.merchant.helper.response.CompanyProfitResponse;
import com.jkm.hss.merchant.service.AllProfitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangbin on 2017/2/19.
 */
@Slf4j
@Service
public class AllProfitServiceImpl implements AllProfitService {

    @Autowired
    private AllProfitDao allProfitDao;

    @Override
    public List<CompanyProfitResponse> selectCompanyProfit(CompanyPrifitRequest req) {

        List<CompanyProfitResponse> list = allProfitDao.selectCompanyProfit(req);
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

    @Override
    public int selectCompanyProfitCount(CompanyPrifitRequest req) {
        return allProfitDao.selectCompanyProfitCount(req);
    }

    @Override
    public List<CompanyProfitResponse> selectOneProfit(CompanyPrifitRequest req) {
        List<CompanyProfitResponse> list = allProfitDao.selectOneProfit(req);
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

    @Override
    public int selectOneProfitCount(CompanyPrifitRequest req) {
        return allProfitDao.selectOneProfitCount(req);
    }

    @Override
    public List<CompanyProfitResponse> selectTwoProfit(CompanyPrifitRequest req) {
        List<CompanyProfitResponse> list = allProfitDao.selectTwoProfit(req);
        return list;
    }

    @Override
    public int selectTwoProfitCount(CompanyPrifitRequest req) {
        return allProfitDao.selectTwoProfitCount(req);
    }

    @Override
    public List<CompanyProfitResponse> selectCompanyProfitDetails(CompanyPrifitRequest req) {
        List<CompanyProfitResponse> list = allProfitDao.selectCompanyProfitDetails(req);
        return list;
    }

    @Override
    public List<CompanyProfitResponse> selectOneProfitDetails(CompanyPrifitRequest req) {
        List<CompanyProfitResponse> list = allProfitDao.selectOneProfitDetails(req);
        return list;
    }

    @Override
    public List<CompanyProfitResponse> selectTwoProfitDetails(CompanyPrifitRequest req) {
        List<CompanyProfitResponse> list = allProfitDao.selectTwoProfitDetails(req);
        return list;
    }

    @Override
    public int selectCompanyProfitDetailsCount(CompanyPrifitRequest req) {
        return allProfitDao.selectCompanyProfitDetailsCount(req);
    }

    @Override
    public int selectOneProfitDetailsCount(CompanyPrifitRequest req) {
        return allProfitDao.selectOneProfitDetailsCount(req);
    }
}
