package com.jkm.hss.bill.service.impl;

import com.jkm.base.common.entity.ExcelSheetVO;
import com.jkm.base.common.util.ExcelUtil;
import com.jkm.hss.account.enums.EnumSplitAccountUserType;
import com.jkm.hss.account.enums.EnumSplitBusinessType;
import com.jkm.hss.bill.dao.ProfitDao;
import com.jkm.hss.bill.entity.JkmProfitDetailsResponse;
import com.jkm.hss.bill.entity.ProfitResponse;
import com.jkm.hss.bill.helper.responseparam.HssAppTotalProfitResponse;
import com.jkm.hss.bill.service.ProfitService;
import com.jkm.hss.merchant.entity.ProfitDetailsRequest;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
//        ProfitDetailsRequest request =selectTime(req);
        List<JkmProfitDetailsResponse> list = profitDao.selectProfitDetails(req);
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
                if (list.get(i).getSplitTotalAmount()!=null&&!"".equals(list.get(i).getSplitTotalAmount())){
                    list.get(i).setSplitTotalAmount("0.00");
                }
                if (list.get(i).getSplitAmount()!=null&&!"".equals(list.get(i).getSplitAmount())){
                    list.get(i).setSplitAmount("0.00");
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

    @Override
    public String downloadExcel(ProfitDetailsRequest req, String baseUrl) {
        final String tempDir = this.getTempDir();
        final File excelFile = new File(tempDir + File.separator + ".csv");
        final ExcelSheetVO excelSheet = getExcelSheet(req,baseUrl);
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
     * 获取好收收APP昨日，进入，总计分润
     *
     * @param accountIds
     * @return
     */
    @Override
    public HssAppTotalProfitResponse getTotalProfit(List<Long> accountIds) {
        return profitDao.getTotalProfit(accountIds);
    }

    private ExcelSheetVO getExcelSheet(ProfitDetailsRequest req, String baseUrl) {
        List<JkmProfitDetailsResponse> list = getProfitDetails(req);
        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<List<String>> datas = new ArrayList<List<String>>();
        final ArrayList<String> heads = new ArrayList<>();
        excelSheetVO.setName("profitDetail");
        heads.add("分润流水号");
        heads.add("业务类型");
        heads.add("分润时间");
        heads.add("交易订单号");
        heads.add("结算周期");
        heads.add("分润总额");
        heads.add("分润金额");
        heads.add("分润出款账户");
        heads.add("分润方名称");
        heads.add("分润方类型");
        heads.add("备注信息");
        datas.add(heads);
        if (list.size()>0){
            for (int i=0;i<list.size();i++) {
                ArrayList<String> columns = new ArrayList<>();
                columns.add(list.get(i).getSplitSn());
                columns.add(list.get(i).getBusinessType());
                columns.add(list.get(i).getSplitDates());
                columns.add(list.get(i).getOrderNo());
                columns.add(list.get(i).getSettleType());
                columns.add(list.get(i).getSplitTotalAmount());
                columns.add(list.get(i).getSplitAmount());
                columns.add(list.get(i).getOutMoneyAccountName());
                columns.add(list.get(i).getReceiptMoneyUserName());
                columns.add(list.get(i).getProfitType());
                columns.add(list.get(i).getRemark());
                datas.add(columns);
            }
        }

        excelSheetVO.setDatas(datas);
        return excelSheetVO;
    }

    private List<JkmProfitDetailsResponse> getProfitDetails(ProfitDetailsRequest req) {
        List<JkmProfitDetailsResponse> list = profitDao.getProfitDetails(req);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list!=null){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getPayChannelSign()!=0){
                    list.get(i).setRemark(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getUpperChannel().getValue());
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
                if (list.get(i).getAccountUserType()==1){
                    list.get(i).setProfitType(EnumSplitAccountUserType.JKM.getValue());
                }
                if (list.get(i).getAccountUserType()==2){
                    list.get(i).setProfitType(EnumSplitAccountUserType.MERCHANT.getValue());
                }
                if (list.get(i).getAccountUserType()==3){
                    list.get(i).setProfitType(EnumSplitAccountUserType.FIRST_DEALER.getValue());
                }
                if (list.get(i).getAccountUserType()==4){
                    list.get(i).setProfitType(EnumSplitAccountUserType.SECOND_DEALER.getValue());
                }
                if (list.get(i).getSplitDate()!=null){
                    String dates = sdf.format(list.get(i).getSplitDate());
                    list.get(i).setSplitDates(dates);
                }
                if (list.get(i).getSplitTotalAmount()!=null&&!"".equals(list.get(i).getSplitTotalAmount())){
                    list.get(i).setSplitTotalAmount("0.00");
                }
                if (list.get(i).getSplitAmount()!=null&&!"".equals(list.get(i).getSplitAmount())){
                    list.get(i).setSplitAmount("0.00");
                }
            }
        }
        return list;
    }

    /**
     * 获取临时路径
     *
     * @return
     */
    public static String getTempDir() {
        final String dir = System.getProperty("java.io.tmpdir") + "hss" + File.separator + "profitDetail";
        final File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }
}
