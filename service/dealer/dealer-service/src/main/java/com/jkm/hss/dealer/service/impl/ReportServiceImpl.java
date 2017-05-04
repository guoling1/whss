package com.jkm.hss.dealer.service.impl;
import com.google.common.base.Optional;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.dealer.entity.STDealerRecord;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.helper.requestparam.DealerReportRequest;
import com.jkm.hss.dealer.helper.response.DealerReport;
import com.jkm.hss.dealer.helper.response.HomeReportResponse;
import com.jkm.hss.dealer.service.ReportService;

import java.math.BigDecimal;
import java.util.Date;
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
        String startDate=startTime+" 00:00:00";
        String endDate=endTime+" 00:00:00";
        STDealerRecord hssstDealerRecord=reportDao.getstdealerrecord(dealerId,startTime,EnumQRCodeSysType.HSS.getId());
        STDealerRecord hsystDealerRecord=reportDao.getstdealerrecord(dealerId,startTime,EnumQRCodeSysType.HSY.getId());

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
        Integer hssregMerNumberDir=reportDao.getHSSDayregMerNumberDir(dealerId,null,null);
        //HSS昨日商户注册数-下级代理
        Integer hssregMerNumberSub=reportDao.getHSSDayregMerNumberSub(dealerId,null,null);
        //HSY昨日商户注册数-直属
        Integer hsyregMerNumberDir=reportDao.getHSYDayregMerNumberDir(dealerId,null,null);
        //HSY昨日商户注册数-下级代理
        Integer hsyregMerNumberSub=reportDao.getHSYDayregMerNumberSub(dealerId,null,null);

        //HSS昨日商户审核数-直属
        Integer hssyDaycheckMerNumberDir;//=reportDao.getHSSDaycheckMerNumberDir(dealerId,startDate,endDate);
        //HSS昨日商户审核数-下级代理
        Integer hssyDaycheckMerNumberSub;//=reportDao.getHSSDaycheckMerNumberSub(dealerId,startTime,endTime);
        //HSY昨日商户审核数-直属
        Integer hsyyDaycheckMerNumberDir;//=reportDao.getHSYDaycheckMerNumberDir(dealerId,startTime,endTime);
        //HSY昨日商户审核数-下级代理
        Integer hsyyDaycheckMerNumberSub;//=reportDao.getHSYDaycheckMerNumberSub(dealerId,startTime,endTime);

        //HSS商户审核数-直属
        Integer hsscheckMerNumberDir=reportDao.getHSSDaycheckMerNumberDir(dealerId,null,null);
        //HSS商户审核数-下级代理
        Integer hsscheckMerNumberSub=reportDao.getHSSDaycheckMerNumberSub(dealerId,null,null);
        //HSY商户审核数-直属
        Integer hsycheckMerNumberDir=reportDao.getHSYDaycheckMerNumberDir(dealerId,null,null);
        //HSY商户审核数-下级代理
        Integer hsycheckMerNumberSub=reportDao.getHSYDaycheckMerNumberSub(dealerId,null,null);

        //HSS二维码总数
        Integer hssqrCodeNumber=0;
        //HSY二维码总数
        Integer hsyqrCodeNumber=0;
        if(level== EnumDealerLevel.FIRST.getId()){
            hssqrCodeNumber=reportDao.getHSSQrCodeNumberfirst(dealerId,null,null);
            hsyqrCodeNumber=reportDao.getHSYQrCodeNumberfirst(dealerId,null,null);
        }else if(level==EnumDealerLevel.SECOND.getId()){
            hssqrCodeNumber=reportDao.getHSSQrCodeNumbersecond(dealerId,null,null);
            hsyqrCodeNumber=reportDao.getHSYQrCodeNumbersecond(dealerId,null,null);
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

    @Override
    public void initReport(){

    }
}
