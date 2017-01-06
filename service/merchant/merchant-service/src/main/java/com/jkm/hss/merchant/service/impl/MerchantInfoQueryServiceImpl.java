package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.ExcelSheetVO;
import com.jkm.base.common.util.ExcelUtil;
import com.jkm.hss.merchant.dao.MerchantInfoDao;
import com.jkm.hss.merchant.dao.MerchantInfoQueryDao;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import com.jkm.hss.merchant.service.MerchantInfoQueryService;
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
import java.util.Date;
import java.util.List;

/**
 * Created by zhangbin on 2016/11/27.
 */
@Slf4j
@Service
public class MerchantInfoQueryServiceImpl implements MerchantInfoQueryService {

    @Autowired
    private MerchantInfoQueryDao merchantInfoQueryDao;

    @Autowired
    private MerchantInfoDao merchantInfoDao;

    @Override
    public Optional<MerchantInfo> selectById(long id) {
        Optional<MerchantInfo> optionalSelectId = this.merchantInfoQueryDao.selectById(id);
        return optionalSelectId;
    }

    @Override
    public Optional<MerchantInfo> selectByStatus(int status) {
        Optional<MerchantInfo> optionalSelectStatus = this.merchantInfoQueryDao.selectByStatus(status);
        return optionalSelectStatus;
    }

    @Override
    public Optional<MerchantInfo> selectByDealerId(long dealerId) {
        Optional<MerchantInfo> optionalSelectDealerId = this.merchantInfoQueryDao.selectByDealerId(dealerId);
        return optionalSelectDealerId;
    }

    @Override
    public List<MerchantInfoResponse> getAll(MerchantInfoResponse merchantInfoResponse) {

        List<MerchantInfoResponse> list = this.merchantInfoQueryDao.getAll(merchantInfoResponse);
        if (list.size()>0){
            for (int i=0;list.size()>i;i++){
                if (list.get(i).getLevel()==1){
                    list.get(i).setProxyName(list.get(i).getProxyName());
                }
                if (list.get(i).getLevel()==2){
                    MerchantInfoResponse res = merchantInfoQueryDao.getProxyName1(list.get(i).getFirstLevelDealerId());
                    list.get(i).setProxyName1(res.getProxyName());
                }
            }
        }
        return list;
    }

    @Override
    public List<MerchantInfoResponse> selectByName(int pageNo, int pageSize, String merchantName) {

        List<MerchantInfoResponse> list = this.merchantInfoQueryDao.selectByName(pageNo,pageSize,merchantName);

        return list;
    }

    @Override
    public List<MerchantInfoResponse> getCount() {

        List<MerchantInfoResponse> list = this.merchantInfoQueryDao.getCount();

        return list;
    }

    @Override
    public List<MerchantInfoResponse> getRecord(MerchantInfoResponse merchantInfoResponse) {

        List<MerchantInfoResponse> list = this.merchantInfoQueryDao.getRecord(merchantInfoResponse);
        if (list.size()>0){
            for (int i=0;list.size()>i;i++){
                if (list.get(i).getLevel()==1){
                    list.get(i).setProxyName(list.get(i).getProxyName());
                }
                if (list.get(i).getLevel()==2){
                    MerchantInfoResponse res = merchantInfoQueryDao.getProxyName(list.get(i).getFirstLevelDealerId());
                    list.get(i).setProxyName1(res.getProxyName());
                }
            }
        }
        return list;
    }

    @Override
    public List<MerchantInfoResponse> getCountRecord() {
        List<MerchantInfoResponse> list = this.merchantInfoQueryDao.getCountRecord();

        return list;
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

    @Override
    public String downloadExcel(MerchantInfoResponse merchantInfoResponse, String baseUrl) throws ParseException {
        final String tempDir = this.getTempDir();
        final File excelFile = new File(tempDir + File.separator + ".xls");
        final ExcelSheetVO excelSheet = generateCodeExcelSheet(merchantInfoResponse,baseUrl);
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
     * 生成ExcelVo
     * @param
     * @param baseUrl
     * @return
     */
    private ExcelSheetVO generateCodeExcelSheet(MerchantInfoResponse merchantInfoResponse, String baseUrl) throws ParseException {
        List<MerchantInfoResponse> list = merchantInfoQueryDao.selectMerchant(merchantInfoResponse);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){

                if (list.get(i).getLevel()==1){
                    list.get(i).setProxyName(list.get(i).getProxyName());
                }
                if (list.get(i).getLevel()==2){
                    MerchantInfoResponse res = merchantInfoQueryDao.getProxyName1(list.get(i).getFirstLevelDealerId());
                    if (res!=null){
                        list.get(i).setProxyName1(res.getProxyName());

                    }

                }


            }
        }
        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<List<String>> datas = new ArrayList<List<String>>();
        final ArrayList<String> heads = new ArrayList<>();
        excelSheetVO.setName("merchant");
        heads.add("商户编号");
        heads.add("商户名称");
        heads.add("所属一级代理");
        heads.add("所属二级代理");
        heads.add("注册时间");
        heads.add("认证时间");
        heads.add("审核时间");
        heads.add("审核状态");
        datas.add(heads);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                ArrayList<String> columns = new ArrayList<>();
                columns.add(String.valueOf(list.get(i).getId()));
                if (!"".equals(list.get(i).getMerchantName())){
                    columns.add(list.get(i).getMerchantName());
                }
                if (!"".equals(list.get(i).getProxyName())){
                    columns.add(list.get(i).getProxyName());
                }
                if (!"".equals(list.get(i).getProxyName1())){
                    columns.add(list.get(i).getProxyName1());
                }
                if (list.get(i).getCreateTime()!= null && !"".equals(list.get(i).getCreateTime())){
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String st = df.format(list.get(i).getCreateTime());
                    columns.add(st);

                }else {
                    columns.add("-");
                }
                if (list.get(i).getAuthenticationTime()!=null && !"".equals(list.get(i).getAuthenticationTime())){
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String AuthenticationTime = list.get(i).getAuthenticationTime();
                    Date date =formatter.parse(AuthenticationTime);
                    String authenticationTime = formatter.format(date);
//                    list.get(i).setCreateTime(authenticationTime);
                    columns.add(authenticationTime);
                }else {
                    columns.add("-");
                }
                if (list.get(i).getCheckedTime()!=null && !"".equals(list.get(i).getCreateTime())){
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String st = df.format(list.get(i).getCreateTime());
                    columns.add(st);
                }else {
                    columns.add("-");
                }
                if (list.get(i).getStatus()==0){
                    columns.add("初始化");
                }
                if (list.get(i).getStatus()==1){
                    columns.add("填写完第一步");
                }
                if (list.get(i).getStatus()==2){
                    columns.add("待审核");
                }
                if (list.get(i).getStatus()==3){
                    columns.add("审核通过");
                }
                if (list.get(i).getStatus()==4) {
                    columns.add("审核未通过");
                }

                datas.add(columns);
            }
        }
        excelSheetVO.setDatas(datas);
        return excelSheetVO;
    }
}
