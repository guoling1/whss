package com.jkm.hss.dealer.service.impl;
import com.google.common.base.Optional;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.dealer.entity.STDealerHistory;
import com.jkm.hss.dealer.entity.STDealerRecord;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.helper.requestparam.DealerReportRequest;
import com.jkm.hss.dealer.helper.response.DealerRegCheck;
import com.jkm.hss.dealer.helper.response.DealerReport;
import com.jkm.hss.dealer.helper.response.HomeReportResponse;
import com.jkm.hss.dealer.service.ReportService;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jkm.hss.dealer.dao.ReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.admin.enums.EnumQRCodeSysType;
/**
 * Created by shitou on 17/4/28.
 */
@Service
public class ReportServiceImpl implements ReportService{

    @Autowired
    private ReportDao reportDao;
    @Autowired
    private AccountService accountService;

    @Override
   public HomeReportResponse getHomeReport(final long dealerId,final long acountid,final int level, final String startTime, final String endTime) {

        //明日日期
        Date nowD = new Date();
        //TODO计算后一天日期， 此功能后期结构优化调整
        Calendar cl = Calendar.getInstance();
        cl.setTime(nowD);
        int day = cl.get(Calendar.DATE);
        cl.set(Calendar.DATE, day+1);
        String tmDate=DateFormatUtil.format(cl.getTime(), "yyyy-MM-dd")+" 00:00:00";
        //
        String endTimeHis=DateFormatUtil.format(nowD, "yyyy-MM-dd");
        String endDateHis=endTimeHis+" 00:00:00";
        //

        String startDate=startTime+" 00:00:00";
        String endDate=endTime+" 00:00:00";
        //HSS昨日统计
        STDealerRecord hssstDealerRecord=reportDao.getstdealerrecord(dealerId,startTime,EnumQRCodeSysType.HSS.getId());
        //HSY昨日统计
        STDealerRecord hsystDealerRecord=reportDao.getstdealerrecord(dealerId,startTime,EnumQRCodeSysType.HSY.getId());

        //===============================================
        //历史统计
        List<STDealerHistory> stDealerHistoryList=reportDao.getdealerhistory(dealerId);
        STDealerHistory hssstDealerHistory=null;//HSS历史统计
        STDealerHistory hsystDealerHistory=null;//HSY历史统计
        if(stDealerHistoryList!=null){
            for(STDealerHistory dealerHistory:stDealerHistoryList){
                if(dealerHistory.getSystype().equals(EnumQRCodeSysType.HSS.getId())){
                    hssstDealerHistory=dealerHistory;
                }
                if(dealerHistory.getSystype().equals(EnumQRCodeSysType.HSY.getId())){
                    hsystDealerHistory=dealerHistory;
                }
            }
        }
        if(hssstDealerHistory==null){
            hssstDealerHistory=new STDealerHistory();
            hssstDealerHistory.setSystype(EnumQRCodeSysType.HSS.getId());
            hssstDealerHistory.setDealerId(dealerId);
        }
        if(hssstDealerHistory!=null){
            if(hssstDealerHistory.getRecordDay()==null||
                    !DateFormatUtil.format(hssstDealerHistory.getRecordDay(), "yyyy-MM-dd").equals(endTimeHis)){
                hssstDealerHistory.setRecordDay(DateFormatUtil.parse(endTimeHis,DateFormatUtil.yyyy_MM_dd));
                hssstDealerHistory.setAllregMerNumberDir(reportDao.getHSSDayregMerNumberDir(dealerId,null,endDateHis));
                hssstDealerHistory.setAllregMerNumberSub(reportDao.getHSSDayregMerNumberSub(dealerId,null,endDateHis));
                hssstDealerHistory.setAllcheckMerNumberDir(reportDao.getHSSDaycheckMerNumberDir(dealerId,null,endDateHis));
                hssstDealerHistory.setAllcheckMerNumberSub(reportDao.getHSSDaycheckMerNumberSub(dealerId,null,endDateHis));
                hssstDealerHistory.setAllMertradeAmountDir(reportDao.getHSSDayMertradeAmountDir(dealerId,null,endDateHis));
                hssstDealerHistory.setAllMertradeAmountSub(reportDao.getHSSDayMertradeAmountSub(dealerId,null,endDateHis));
                if(level== EnumDealerLevel.FIRST.getId()) {
                    hssstDealerHistory.setAllqrCode(reportDao.getQrCodeNumberfirst(EnumQRCodeSysType.HSS.getId(),dealerId, null, endDateHis));
                }
                else
                {
                    hssstDealerHistory.setAllqrCode(reportDao.getQrCodeNumbersecond(EnumQRCodeSysType.HSS.getId(),dealerId, null, endDateHis));
                }
                if(hssstDealerHistory.getId()==0){
                    //新增
                    reportDao.insertdealerhistory(hssstDealerHistory);
                }
                else{
                    //更新
                    reportDao.updatedealerhistory(hssstDealerHistory);
                }
            }
        }
        if(hsystDealerHistory==null){
            hsystDealerHistory=new STDealerHistory();
            hsystDealerHistory.setSystype(EnumQRCodeSysType.HSY.getId());
            hsystDealerHistory.setDealerId(dealerId);
        }
        if(hsystDealerHistory!=null){
            if(hsystDealerHistory.getRecordDay()==null||
                    !DateFormatUtil.format(hsystDealerHistory.getRecordDay(), "yyyy-MM-dd").equals(endTimeHis)){
                hsystDealerHistory.setRecordDay(DateFormatUtil.parse(endTimeHis,DateFormatUtil.yyyy_MM_dd));
                hsystDealerHistory.setAllregMerNumberDir(reportDao.getHSYDayregMerNumberDir(dealerId,null,endDateHis));
                hsystDealerHistory.setAllregMerNumberSub(reportDao.getHSYDayregMerNumberSub(dealerId,null,endDateHis));
                hsystDealerHistory.setAllcheckMerNumberDir(reportDao.getHSYDaycheckMerNumberDir(dealerId,null,endDateHis));
                hsystDealerHistory.setAllcheckMerNumberSub(reportDao.getHSYDaycheckMerNumberSub(dealerId,null,endDateHis));
                hsystDealerHistory.setAllMertradeAmountDir(reportDao.getHSYDayMertradeAmountDir(dealerId,null,endDateHis));
                hsystDealerHistory.setAllMertradeAmountSub(reportDao.getHSYDayMertradeAmountSub(dealerId,null,endDateHis));
                if(level== EnumDealerLevel.FIRST.getId()) {
                    hsystDealerHistory.setAllqrCode(reportDao.getQrCodeNumberfirst(EnumQRCodeSysType.HSY.getId(),dealerId, null, endDateHis));
                }
                else
                {
                    hsystDealerHistory.setAllqrCode(reportDao.getQrCodeNumbersecond(EnumQRCodeSysType.HSY.getId(),dealerId, null, endDateHis));
                }
                if(hsystDealerHistory.getId()==0){
                    //新增
                    reportDao.insertdealerhistory(hsystDealerHistory);
                }
                else{
                    //更新
                    reportDao.updatedealerhistory(hsystDealerHistory);
                }
            }
        }
        //============================================


        //昨日分润统计
        BigDecimal yDayProfit;//=reportDao.getDayProfit(acountid,startDate,endDate);
        //分润统计
        BigDecimal allProfit=reportDao.getDayProfit(acountid,null,null);
        //待结算金额
         Optional<Account>  accountOptional=accountService.getById(acountid);
        BigDecimal duesettleAmount=accountOptional.get().getDueSettleAmount();
        //可用余额
        BigDecimal availableAmount=accountOptional.get().getAvailable();

        //HSS昨日商户交易总额-直属
        BigDecimal hssyDayMertradeAmountDir;//= reportDao.getHSSDayMertradeAmountDir(dealerId,startDate,endDate);
        //HSS昨日商户交易总额-下级
        BigDecimal hssyDayMertradeAmountSub;//=reportDao.getHSSDayMertradeAmountSub(dealerId,startDate,endDate);
        //HSY昨日商户交易总额-直属
        BigDecimal hsyyDayMertradeAmountDir;//=reportDao.getHSYDayMertradeAmountDir(dealerId,startDate,endDate);
        //HSY昨日商户交易总额-下级
        BigDecimal hsyyDayMertradeAmountSub;//=reportDao.getHSYDayMertradeAmountSub(dealerId,startDate,endDate);

        //HSS昨日商户注册数-直属
        Integer hssyDayregMerNumberDir;//=reportDao.getHSSDayregMerNumberDir(dealerId,startDate,endDate);
        //HSS昨日商户注册数-下级代理
        Integer hssyDayregMerNumberSub;//=reportDao.getHSSDayregMerNumberSub(dealerId,startTime,endTime);
        //HSY昨日商户注册数-直属
        Integer hsyyDayregMerNumberDir;//=reportDao.getHSYDayregMerNumberDir(dealerId,startTime,endTime);
        //HSY昨日商户注册数-下级代理
        Integer hsyyDayregMerNumberSub;//=reportDao.getHSYDayregMerNumberSub(dealerId,startTime,endTime);

        //HSS商户注册数-直属
        Integer hssregMerNumberDir=getisInterger(reportDao.getHSSDayregMerNumberDir(dealerId,endDateHis,tmDate))+getisInterger(hssstDealerHistory.getAllregMerNumberDir());
        //HSS商户注册数-下级代理
        Integer hssregMerNumberSub=getisInterger(reportDao.getHSSDayregMerNumberSub(dealerId,endDateHis,tmDate))+getisInterger(hssstDealerHistory.getAllregMerNumberSub());
        //HSY商户注册数-直属
        Integer hsyregMerNumberDir=getisInterger(reportDao.getHSYDayregMerNumberDir(dealerId,endDateHis,tmDate))+getisInterger(hsystDealerHistory.getAllregMerNumberDir());
        //HSY商户注册数-下级代理
        Integer hsyregMerNumberSub=getisInterger(reportDao.getHSYDayregMerNumberSub(dealerId,endDateHis,tmDate))+getisInterger(hsystDealerHistory.getAllregMerNumberSub());

        //HSS昨日商户审核数-直属
        Integer hssyDaycheckMerNumberDir;//=reportDao.getHSSDaycheckMerNumberDir(dealerId,startDate,endDate);
        //HSS昨日商户审核数-下级代理
        Integer hssyDaycheckMerNumberSub;//=reportDao.getHSSDaycheckMerNumberSub(dealerId,startTime,endTime);
        //HSY昨日商户审核数-直属
        Integer hsyyDaycheckMerNumberDir;//=reportDao.getHSYDaycheckMerNumberDir(dealerId,startTime,endTime);
        //HSY昨日商户审核数-下级代理
        Integer hsyyDaycheckMerNumberSub;//=reportDao.getHSYDaycheckMerNumberSub(dealerId,startTime,endTime);

        //HSS商户审核数-直属
        Integer hsscheckMerNumberDir=getisInterger(reportDao.getHSSDaycheckMerNumberDir(dealerId,endDateHis,tmDate))+getisInterger(hssstDealerHistory.getAllcheckMerNumberDir());
        //HSS商户审核数-下级代理
        Integer hsscheckMerNumberSub=getisInterger(reportDao.getHSSDaycheckMerNumberSub(dealerId,endDateHis,tmDate))+getisInterger(hssstDealerHistory.getAllcheckMerNumberSub());
        //HSY商户审核数-直属
        Integer hsycheckMerNumberDir=getisInterger(reportDao.getHSYDaycheckMerNumberDir(dealerId,endDateHis,tmDate))+getisInterger(hsystDealerHistory.getAllcheckMerNumberDir());
        //HSY商户审核数-下级代理
        Integer hsycheckMerNumberSub=getisInterger(reportDao.getHSYDaycheckMerNumberSub(dealerId,endDateHis,tmDate))+getisInterger(hsystDealerHistory.getAllcheckMerNumberSub());

        //HSS二维码总数
        Integer hssqrCodeNumber=0;
        //HSY二维码总数
        Integer hsyqrCodeNumber=0;
        if(level== EnumDealerLevel.FIRST.getId()){
            hssqrCodeNumber=getisInterger(reportDao.getQrCodeNumberfirst(EnumQRCodeSysType.HSS.getId(),dealerId,endDateHis,tmDate))+getisInterger(hssstDealerHistory.getAllqrCode());
            hsyqrCodeNumber=getisInterger(reportDao.getQrCodeNumberfirst(EnumQRCodeSysType.HSY.getId(),dealerId,endDateHis,tmDate))+getisInterger(hsystDealerHistory.getAllqrCode());
        }else if(level==EnumDealerLevel.SECOND.getId()){
            hssqrCodeNumber=getisInterger(reportDao.getQrCodeNumbersecond(EnumQRCodeSysType.HSS.getId(),dealerId,endDateHis,tmDate))+getisInterger(hssstDealerHistory.getAllqrCode());
            hsyqrCodeNumber=getisInterger(reportDao.getQrCodeNumbersecond(EnumQRCodeSysType.HSY.getId(),dealerId,endDateHis,tmDate))+getisInterger(hsystDealerHistory.getAllqrCode());
        }

        //插入日数据
        if(hssstDealerRecord==null){
            hssstDealerRecord=new STDealerRecord();
            hssstDealerRecord.setDealerId(dealerId);
            hssstDealerRecord.setProxy_name("");
            hssstDealerRecord.setRecordDay(DateFormatUtil.parse(startTime,DateFormatUtil.yyyy_MM_dd));
            hssstDealerRecord.setSys_type(EnumQRCodeSysType.HSS.getId());

            yDayProfit=reportDao.getDayProfit(acountid,startDate,endDate);
            hssyDaycheckMerNumberDir=reportDao.getHSSDaycheckMerNumberDir(dealerId,startDate,endDate);
            hssyDaycheckMerNumberSub=reportDao.getHSSDaycheckMerNumberSub(dealerId,startTime,endTime);
            hssyDayMertradeAmountDir=reportDao.getHSSDayMertradeAmountDir(dealerId,startDate,endDate);
            hssyDayMertradeAmountSub=reportDao.getHSSDayMertradeAmountSub(dealerId,startDate,endDate);
            hssyDayregMerNumberDir=reportDao.getHSSDayregMerNumberDir(dealerId,startDate,endDate);
            hssyDayregMerNumberSub=reportDao.getHSSDayregMerNumberSub(dealerId,startTime,endTime);

            hssstDealerRecord.setYDayProfit(yDayProfit);
            hssstDealerRecord.setYDaycheckMerNumberDir(hssyDaycheckMerNumberDir);
            hssstDealerRecord.setYDaycheckMerNumberSub(hssyDaycheckMerNumberSub);
            hssstDealerRecord.setYDayMertradeAmountDir(hssyDayMertradeAmountDir);
            hssstDealerRecord.setYDayMertradeAmountSub(hssyDayMertradeAmountSub);
            hssstDealerRecord.setYDayregMerNumberDir(hssyDayregMerNumberDir);
            hssstDealerRecord.setYDayregMerNumberSub(hssyDayregMerNumberSub);
            reportDao.insertstdealerrecord(hssstDealerRecord);
        }
        else {
            yDayProfit=hssstDealerRecord.getYDayProfit();
            hssyDaycheckMerNumberDir=hssstDealerRecord.getYDaycheckMerNumberDir();
            hssyDaycheckMerNumberSub=hssstDealerRecord.getYDaycheckMerNumberSub();
            hssyDayMertradeAmountDir=hssstDealerRecord.getYDayMertradeAmountDir();
            hssyDayMertradeAmountSub=hssstDealerRecord.getYDayMertradeAmountSub();
            hssyDayregMerNumberDir=hssstDealerRecord.getYDayregMerNumberDir();
            hssyDayregMerNumberSub=hssstDealerRecord.getYDayregMerNumberSub();
        }

        if(hsystDealerRecord==null){
            hsystDealerRecord=new STDealerRecord();
            hsystDealerRecord.setDealerId(dealerId);
            hsystDealerRecord.setProxy_name("");
            hsystDealerRecord.setRecordDay(DateFormatUtil.parse(startTime,DateFormatUtil.yyyy_MM_dd));
            hsystDealerRecord.setSys_type(EnumQRCodeSysType.HSY.getId());

            hsyyDaycheckMerNumberDir=reportDao.getHSYDaycheckMerNumberDir(dealerId,startDate,endDate);
            hsyyDaycheckMerNumberSub=reportDao.getHSYDaycheckMerNumberSub(dealerId,startTime,endTime);
            hsyyDayMertradeAmountDir=reportDao.getHSYDayMertradeAmountDir(dealerId,startDate,endDate);
            hsyyDayMertradeAmountSub=reportDao.getHSYDayMertradeAmountSub(dealerId,startDate,endDate);
            hsyyDayregMerNumberDir=reportDao.getHSYDayregMerNumberDir(dealerId,startDate,endDate);
            hsyyDayregMerNumberSub=reportDao.getHSYDayregMerNumberSub(dealerId,startTime,endTime);

            hsystDealerRecord.setYDaycheckMerNumberDir(hsyyDaycheckMerNumberDir);
            hsystDealerRecord.setYDaycheckMerNumberSub(hsyyDaycheckMerNumberSub);
            hsystDealerRecord.setYDayMertradeAmountDir(hsyyDayMertradeAmountDir);
            hsystDealerRecord.setYDayMertradeAmountSub(hsyyDayMertradeAmountSub);
            hsystDealerRecord.setYDayregMerNumberDir(hsyyDayregMerNumberDir);
            hsystDealerRecord.setYDayregMerNumberSub(hsyyDayregMerNumberSub);
            reportDao.insertstdealerrecord(hsystDealerRecord);

        }
        else{
            hsyyDaycheckMerNumberDir=hsystDealerRecord.getYDaycheckMerNumberDir();
            hsyyDaycheckMerNumberSub=hsystDealerRecord.getYDaycheckMerNumberSub();
            hsyyDayMertradeAmountDir=hsystDealerRecord.getYDayMertradeAmountDir();
            hsyyDayMertradeAmountSub=hsystDealerRecord.getYDayMertradeAmountSub();
            hsyyDayregMerNumberDir=hsystDealerRecord.getYDayregMerNumberDir();
            hsyyDayregMerNumberSub=hsystDealerRecord.getYDayregMerNumberSub();
        }
        //
        if(hssyDayMertradeAmountDir==null){hssyDayMertradeAmountDir=new BigDecimal(0);}
        if(hssyDayMertradeAmountSub==null){hssyDayMertradeAmountSub=new BigDecimal(0);}
        if(hsyyDayMertradeAmountDir==null){hsyyDayMertradeAmountDir=new BigDecimal(0);}
        if(hsyyDayMertradeAmountSub==null){hsyyDayMertradeAmountSub=new BigDecimal(0);}

        //加历史数据


        HomeReportResponse homeReportResponse=new HomeReportResponse();
        homeReportResponse.setAllProfit(allProfit);
        homeReportResponse.setYDayProfit(yDayProfit);
        homeReportResponse.setDuesettleAmount(duesettleAmount);
        homeReportResponse.setAvailableAmount(availableAmount);
        DealerReport hssdealerReport=new DealerReport();
        hssdealerReport.setYDayMertradeAmountDir(hssyDayMertradeAmountDir);
        hssdealerReport.setYDayMertradeAmountSub(hssyDayMertradeAmountSub);
        hssdealerReport.setYDayMertradeAmount(hssyDayMertradeAmountDir.add(hssyDayMertradeAmountSub));
        hssdealerReport.setYDayregMerNumberDir(hssyDayregMerNumberDir);
        hssdealerReport.setYDayregMerNumberSub(hssyDayregMerNumberSub);
        hssdealerReport.setRegMerNumberDir(hssregMerNumberDir);
        hssdealerReport.setRegMerNumberSub(hssregMerNumberSub);
        hssdealerReport.setYDaycheckMerNumberDir(hssyDaycheckMerNumberDir);
        hssdealerReport.setYDaycheckMerNumberSub(hssyDaycheckMerNumberSub);
        hssdealerReport.setCheckMerNumberDir(hsscheckMerNumberDir);
        hssdealerReport.setCheckMerNumberSub(hsscheckMerNumberSub);
        hssdealerReport.setQrCodeNumber(hssqrCodeNumber);
        homeReportResponse.setDealerReporthss(hssdealerReport);
         DealerReport hsydealerReport=new DealerReport();
         hsydealerReport.setYDayMertradeAmountDir(hsyyDayMertradeAmountDir);
         hsydealerReport.setYDayMertradeAmountSub(hsyyDayMertradeAmountSub);
         hsydealerReport.setYDayMertradeAmount(hsyyDayMertradeAmountDir.add(hsyyDayMertradeAmountSub));
         hsydealerReport.setYDayregMerNumberDir(hsyyDayregMerNumberDir);
         hsydealerReport.setYDayregMerNumberSub(hsyyDayregMerNumberSub);
         hsydealerReport.setRegMerNumberDir(hsyregMerNumberDir);
         hsydealerReport.setRegMerNumberSub(hsyregMerNumberSub);
         hsydealerReport.setYDaycheckMerNumberDir(hsyyDaycheckMerNumberDir);
         hsydealerReport.setYDaycheckMerNumberSub(hsyyDaycheckMerNumberSub);
         hsydealerReport.setCheckMerNumberDir(hsycheckMerNumberDir);
         hsydealerReport.setCheckMerNumberSub(hsycheckMerNumberSub);
         hsydealerReport.setQrCodeNumber(hsyqrCodeNumber);
         homeReportResponse.setDealerReporthsy(hsydealerReport);

        return homeReportResponse;
    }

    private Integer getisInterger(Integer v){
        if(v==null){
            return 0;
        }
        return v;
    }

    @Override
    public void initReport(){
        String endDate=DateFormatUtil.format(new Date(), "yyyy-MM-dd")+" 00:00:00";

        long acountid=0;
        long dealerId=0;
//        //历史分润统计
//        BigDecimal allProfit=reportDao.getDayProfit(acountid,null,endDate);
//        //HSS商户注册数-直属
//        Integer hssregMerNumberDir=reportDao.getHSSDayregMerNumberDir(dealerId,null,endDate);
//        //HSS商户注册数-下级代理
//        Integer hssregMerNumberSub=reportDao.getHSSDayregMerNumberSub(dealerId,null,endDate);
//        //HSY商户注册数-直属
//        Integer hsyregMerNumberDir=reportDao.getHSYDayregMerNumberDir(dealerId,null,endDate);
//        //HSY商户注册数-下级代理
//        Integer hsyregMerNumberSub=reportDao.getHSYDayregMerNumberSub(dealerId,null,endDate);
//
//        //HSS商户审核数-直属
//        Integer hsscheckMerNumberDir=reportDao.getHSSDaycheckMerNumberDir(dealerId,null,endDate);
//        //HSS商户审核数-下级代理
//        Integer hsscheckMerNumberSub=reportDao.getHSSDaycheckMerNumberSub(dealerId,null,endDate);
//        //HSY商户审核数-直属
//        Integer hsycheckMerNumberDir=reportDao.getHSYDaycheckMerNumberDir(dealerId,null,endDate);
//        //HSY商户审核数-下级代理
//        Integer hsycheckMerNumberSub=reportDao.getHSYDaycheckMerNumberSub(dealerId,null,endDate);

        //HSS历史直属商户注册数
        List<DealerRegCheck> hssHisTJregMerNumberDir=reportDao.getHSSHisTJregMerNumberDir(null,endDate);
        //HSS历史下级代理商户注册数
        List<DealerRegCheck> hssHisTJregMerNumberSub=reportDao.getHSSHisTJregMerNumberSub(null,endDate);
        //HSS历史直属商户审核数
        List<DealerRegCheck> hssHisTJcheckMerNumberDir=reportDao.getHSSHisTJcheckMerNumberDir(null,endDate);
        //HSS历史下级代理商户审核数
        List<DealerRegCheck> hssHisTJcheckMerNumberSub=reportDao.getHSSHisTJcheckMerNumberSub(null,endDate);
        List<DealerRegCheck> hssqrCode=reportDao.getHisTJQrCodeNumberfirst("hss",null,endDate);

        //HSY历史直属商户注册数
        List<DealerRegCheck> hsyHisTJregMerNumberDir=reportDao.getHSYHisTJregMerNumberDir(null,endDate);
        //HSY历史下级代理商户注册数
        List<DealerRegCheck> hsyHisTJregMerNumberSub=reportDao.getHSYHisTJregMerNumberSub(null,endDate);
        //HSY历史直属商户审核数
        List<DealerRegCheck> hsyHisTJcheckMerNumberDir=reportDao.getHSYHisTJcheckMerNumberDir(null,endDate);
        //HSY历史下级代理商户审核数
        List<DealerRegCheck> hsyHisTJcheckMerNumberSub=reportDao.getHSYHisTJcheckMerNumberSub(null,endDate);
        List<DealerRegCheck> hsyqrCode=reportDao.getHisTJQrCodeNumberfirst("hsy",null,endDate);

        //List<Integer>

    }
}
