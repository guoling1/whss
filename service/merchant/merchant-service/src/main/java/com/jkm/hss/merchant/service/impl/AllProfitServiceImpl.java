package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.account.enums.EnumSplitBusinessType;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.dao.AllProfitDao;
import com.jkm.hss.merchant.helper.request.CompanyPrifitRequest;
import com.jkm.hss.merchant.helper.response.CompanyProfitResponse;
import com.jkm.hss.merchant.service.AllProfitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangbin on 2017/2/19.
 */
@Slf4j
@Service
public class AllProfitServiceImpl implements AllProfitService {

    @Autowired
    private AllProfitDao allProfitDao;

    @Autowired
    private DealerService dealerService;

    @Override
    public List<CompanyProfitResponse> selectCompanyProfit(CompanyPrifitRequest req) throws ParseException {

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
    public List<CompanyProfitResponse> selectCompanyProfitCount(CompanyPrifitRequest req) {
        List<CompanyProfitResponse> list = allProfitDao.selectCompanyProfitCount(req);
        return list;
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
    public List<CompanyProfitResponse> selectOneProfitCount(CompanyPrifitRequest req) {
        List<CompanyProfitResponse> list = allProfitDao.selectOneProfitCount(req);
        return list;
    }

    @Override
    public List<CompanyProfitResponse> selectTwoProfit(CompanyPrifitRequest req) {
        List<CompanyProfitResponse> list = allProfitDao.selectTwoProfit(req);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getLevel()==2){
                    String  proxy = dealerService.selectProxyName(list.get(i).getFirstLevelDealerId());
                    list.get(i).setProxyName(proxy);
                }
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
    public int selectTwoProfitCount(CompanyPrifitRequest req) {
        return allProfitDao.selectTwoProfitCount(req);
    }

    private CompanyPrifitRequest getTime(CompanyPrifitRequest req) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date dt= null;
        try {
            String d = sdf.format(req.getSplitDate());
            dt = sdf.parse(d);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setSplitDate1(rightNow.getTime());
            req.setSplitDate(dt);

        } catch (ParseException e) {
            log.debug("时间转换异常");
        }
        return req;
    }




    @Override
    public List<CompanyProfitResponse> selectCompanyProfitDetails(CompanyPrifitRequest req) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String d = sdf.format(req.getSplitDate());
//        Date dt=sdf.parse(d);
//        Calendar rightNow = Calendar.getInstance();
//        rightNow.setTime(dt);
//        rightNow.add(Calendar.DATE, 1);
//        req.setSplitDate1(rightNow.getTime());
//        req.setSplitDate(dt);
        final CompanyPrifitRequest request =getTime(req);
        if (request.getBusinessType().equals("好收收- 收款")){
            request.setBusinessType(EnumSplitBusinessType.HSSPAY.getId());
        }
        if (request.getBusinessType().equals("好收收-提现")){
            request.setBusinessType(EnumSplitBusinessType.HSSWITHDRAW.getId());
        }
        if (request.getBusinessType().equals("好收收-升级费")){
            request.setBusinessType(EnumSplitBusinessType.HSSPROMOTE.getId());
        }
        if (request.getBusinessType().equals("好收银-收款")){
            request.setBusinessType(EnumSplitBusinessType.HSYPAY.getId());
        }
        List<CompanyProfitResponse> list = allProfitDao.selectCompanyProfitDetails(request);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getLevel()==2){
                    String  proxy = dealerService.selectProxyName(list.get(i).getFirstLevelDealerId());
                    list.get(i).setProxyName(proxy);
                }
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
    public List<CompanyProfitResponse> selectOneProfitDetails(CompanyPrifitRequest req) {
        final CompanyPrifitRequest request =getTime(req);
        List<CompanyProfitResponse> list = allProfitDao.selectOneProfitDetails(request);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getLevel()==2){
                    String  proxy = dealerService.selectProxyName(list.get(i).getFirstLevelDealerId());
                    list.get(i).setProxyName(proxy);
                }
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
    public List<CompanyProfitResponse> selectTwoProfitDetails(CompanyPrifitRequest req) {
        final CompanyPrifitRequest request =getTime(req);
        List<CompanyProfitResponse> list = allProfitDao.selectTwoProfitDetails(request);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getLevel()==2){
                    String  proxy = dealerService.selectProxyName(list.get(i).getFirstLevelDealerId());
                    list.get(i).setProxyName(proxy);
                }
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
    public List<CompanyProfitResponse> selectCompanyProfitDetailsCount(CompanyPrifitRequest req) {
        final CompanyPrifitRequest request =getTime(req);
        List<CompanyProfitResponse> list = allProfitDao.selectCompanyProfitDetailsCount(request);
        return list;
    }

    @Override
    public int selectOneProfitDetailsCount(CompanyPrifitRequest req) {
        return allProfitDao.selectOneProfitDetailsCount(req);
    }

    @Override
    public int selectTwoProfitDetailsCount(CompanyPrifitRequest req) {
        return allProfitDao.selectTwoProfitDetailsCount(req);
    }

    @Override
    public List<CompanyProfitResponse> selectTwoAll(CompanyPrifitRequest req) {
        List<CompanyProfitResponse> list = allProfitDao.selectTwoAll(req);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getLevel()==2){
                    String  proxy = dealerService.selectProxyName(list.get(i).getFirstLevelDealerId());
                    list.get(i).setProxyName(proxy);
                }
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
}
