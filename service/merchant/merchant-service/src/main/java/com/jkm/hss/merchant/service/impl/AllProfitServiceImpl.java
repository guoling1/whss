package com.jkm.hss.merchant.service.impl;

import com.jkm.base.common.entity.ExcelSheetVO;
import com.jkm.base.common.util.ExcelUtil;
import com.jkm.hss.account.enums.EnumSplitBusinessType;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.dao.AllProfitDao;
import com.jkm.hss.merchant.helper.request.CompanyPrifitRequest;
import com.jkm.hss.merchant.helper.response.CompanyProfitResponse;
import com.jkm.hss.merchant.service.AllProfitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        CompanyPrifitRequest request =selectTime(req);
        List<CompanyProfitResponse> list = allProfitDao.selectCompanyProfit(request);
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


    public List<CompanyProfitResponse> selectCompanyProfitDc(CompanyPrifitRequest req) throws ParseException {
        CompanyPrifitRequest request =selectTime(req);
        List<CompanyProfitResponse> list = allProfitDao.selectCompanyProfitDc(request);
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
        CompanyPrifitRequest request =selectTime(req);
        List<CompanyProfitResponse> list = allProfitDao.selectOneProfit(request);
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


    public List<CompanyProfitResponse> selectOneProfitDc(CompanyPrifitRequest req) {
        CompanyPrifitRequest request =selectTime(req);
        List<CompanyProfitResponse> list = allProfitDao.selectOneProfitDc(request);
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
        CompanyPrifitRequest request =selectTime(req);
        List<CompanyProfitResponse> list = allProfitDao.selectOneProfitCount(request);
        return list;
    }

    @Override
    public List<CompanyProfitResponse> selectTwoProfit(CompanyPrifitRequest req) {
        CompanyPrifitRequest request =selectTime(req);
        List<CompanyProfitResponse> list = allProfitDao.selectTwoProfit(request);
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


    public List<CompanyProfitResponse> selectTwoProfitDc(CompanyPrifitRequest req) {
        CompanyPrifitRequest request =selectTime(req);
        List<CompanyProfitResponse> list = allProfitDao.selectTwoProfitDc(request);
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
    public List<CompanyProfitResponse> selectTwoProfitCount(CompanyPrifitRequest req) {
        CompanyPrifitRequest request =selectTime(req);
        List<CompanyProfitResponse> list = allProfitDao.selectTwoProfitCount(request);
        return list;
    }


    private CompanyPrifitRequest selectTime(CompanyPrifitRequest req) {
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

    @Override
    public String downloadExcel(CompanyPrifitRequest req, String baseUrl) throws ParseException {
        final String tempDir = this.getTempDir();
        final File excelFile = new File(tempDir + File.separator + ".xls");
        final ExcelSheetVO excelSheet = generateCodeExcelSheet(req,baseUrl);
        final List<ExcelSheetVO> excelSheets = new ArrayList<>();
        excelSheets.add(excelSheet);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(excelFile);
            ExcelUtil.exportExcel(excelSheets, fileOutputStream);
            return excelFile.getAbsolutePath();
        } catch (final Exception e) {
            log.error("download trade record error", e);
            e.printStackTrace();
        }  finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (final IOException e) {
                    log.error("close fileOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    @Override
    public String downloadExcelOneDealer(CompanyPrifitRequest req, String baseUrl) throws ParseException {
        final String tempDir = this.getTempDir();
        final File excelFile = new File(tempDir + File.separator + ".xls");
        final ExcelSheetVO excelSheet = generateCodeExcelSheet1(req,baseUrl);
        final List<ExcelSheetVO> excelSheets = new ArrayList<>();
        excelSheets.add(excelSheet);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(excelFile);
            ExcelUtil.exportExcel(excelSheets, fileOutputStream);
            return excelFile.getAbsolutePath();
        } catch (final Exception e) {
            log.error("download trade record error", e);
            e.printStackTrace();
        }  finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (final IOException e) {
                    log.error("close fileOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    @Override
    public String downloadExcelTwoDealer(CompanyPrifitRequest req, String baseUrl) throws ParseException {
        final String tempDir = this.getTempDir();
        final File excelFile = new File(tempDir + File.separator + ".xls");
        final ExcelSheetVO excelSheet = generateCodeExcelSheet2(req,baseUrl);
        final List<ExcelSheetVO> excelSheets = new ArrayList<>();
        excelSheets.add(excelSheet);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(excelFile);
            ExcelUtil.exportExcel(excelSheets, fileOutputStream);
            return excelFile.getAbsolutePath();
        } catch (final Exception e) {
            log.error("download trade record error", e);
            e.printStackTrace();
        }  finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (final IOException e) {
                    log.error("close fileOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 获取临时路径
     *
     * @return
     */
    public static String getTempDir() {
        final String dir = System.getProperty("java.io.tmpdir") + "hss" + File.separator + "trade" + File.separator + "record";
        final File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    /**
     * 生成ExcelVo
     * @param
     * @param baseUrl
     * @return
     */
    private ExcelSheetVO generateCodeExcelSheet(CompanyPrifitRequest req, String baseUrl) throws ParseException {
        List<CompanyProfitResponse> list = selectCompanyProfitDc(req);
        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<List<String>> datas = new ArrayList<List<String>>();
        final ArrayList<String> heads = new ArrayList<>();
        excelSheetVO.setName("companyProfit");
        heads.add("收益日期");
        heads.add("收益类型");
        heads.add("收益金额");
        datas.add(heads);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                ArrayList<String> columns = new ArrayList<>();
                if (list.get(i).getSplitDate()!= null && !"".equals(list.get(i).getSplitDate())){
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String st = df.format(list.get(i).getSplitDate());
                    columns.add(st);

                }else {
                    columns.add("");
                }
                columns.add(list.get(i).getBusinessType());
                columns.add(String.valueOf(list.get(i).getSplitAmount()));

                datas.add(columns);
            }
        }
        excelSheetVO.setDatas(datas);
        return excelSheetVO;
    }

    /**
     * 生成ExcelVo
     * @param
     * @param baseUrl
     * @return
     */
    private ExcelSheetVO generateCodeExcelSheet1(CompanyPrifitRequest req, String baseUrl) throws ParseException {
        List<CompanyProfitResponse> list = selectOneProfitDc(req);
        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<List<String>> datas = new ArrayList<List<String>>();
        final ArrayList<String> heads = new ArrayList<>();
        excelSheetVO.setName("companyProfit");
        heads.add("代理商名称");
        heads.add("代理商编号");
        heads.add("收益日期");
        heads.add("收益类型");
        heads.add("收益金额");
        datas.add(heads);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                ArrayList<String> columns = new ArrayList<>();
                columns.add(list.get(i).getProxyName());
                columns.add(list.get(i).getMarkCode());
                if (list.get(i).getSplitDate()!= null && !"".equals(list.get(i).getSplitDate())){
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String st = df.format(list.get(i).getSplitDate());
                    columns.add(st);

                }else {
                    columns.add("");
                }
                columns.add(list.get(i).getBusinessType());
                columns.add(String.valueOf(list.get(i).getSplitAmount()));

                datas.add(columns);
            }
        }
        excelSheetVO.setDatas(datas);
        return excelSheetVO;
    }

    /**
     * 生成ExcelVo
     * @param
     * @param baseUrl
     * @return
     */
    private ExcelSheetVO generateCodeExcelSheet2(CompanyPrifitRequest req, String baseUrl) throws ParseException {
        List<CompanyProfitResponse> list = selectTwoProfitDc(req);
        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<List<String>> datas = new ArrayList<List<String>>();
        final ArrayList<String> heads = new ArrayList<>();
        excelSheetVO.setName("companyProfit");
        heads.add("上级代理商名称");
        heads.add("代理商名称");
        heads.add("代理商编号");
        heads.add("收益日期");
        heads.add("收益类型");
        heads.add("收益金额");
        datas.add(heads);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                ArrayList<String> columns = new ArrayList<>();
                columns.add(list.get(i).getProxyName());
                columns.add(list.get(i).getProxyName1());
                columns.add(list.get(i).getMarkCode());
                if (list.get(i).getSplitDate()!= null && !"".equals(list.get(i).getSplitDate())){
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String st = df.format(list.get(i).getSplitDate());
                    columns.add(st);

                }else {
                    columns.add("");
                }
                columns.add(list.get(i).getBusinessType());
                columns.add(String.valueOf(list.get(i).getSplitAmount()));

                datas.add(columns);
            }
        }
        excelSheetVO.setDatas(datas);
        return excelSheetVO;
    }
}
