package com.jkm.hss.merchant.service.impl;

import com.jkm.base.common.entity.ExcelSheetVO;
import com.jkm.base.common.util.ExcelUtil;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.dao.MerchantInfoQueryDao;
import com.jkm.hss.merchant.entity.*;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.enums.EnumSource;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.MerchantInfoQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2016/11/27.
 */
@Slf4j
@Service
public class MerchantInfoQueryServiceImpl implements MerchantInfoQueryService {

    @Autowired
    private MerchantInfoQueryDao merchantInfoQueryDao;

    @Autowired
    private DealerService dealerService;


    @Override
    public List<MerchantInfoResponse> getAll(MerchantInfoRequest req) {
        String mobile = req.getMobile();
        if (mobile!=null&&!mobile.equals("")){
            req.setMobile(MerchantSupport.encryptMobile(mobile));
        }
        Integer status = req.getStatus();
        if (status!=null) {
            if (status == 3) {
                List<MerchantInfoResponse> list = this.merchantInfoQueryDao.getAll(req);
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getMobile() != null && !list.get(i).getMobile().equals("")) {
                            list.get(i).setMobile(MerchantSupport.decryptMobile(list.get(i).getMobile()));
                        }
                        if (list.get(i).getOemId()==0){
                            list.get(i).setBranchCompany("金开门");
                        }

                    }
                }
                return list;
            }
        }
        List<MerchantInfoResponse> list = this.merchantInfoQueryDao.getAll1(req);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if(list.get(i).getMobile()!=null&&!list.get(i).getMobile().equals("")){
                    list.get(i).setMobile(MerchantSupport.decryptMobile(list.get(i).getMobile()));
                }


            }
        }
        return list;
    }


    private List<MerchantInfoResponse> downloade(MerchantInfoRequest req) {
        String mobile = req.getMobile();
        if (mobile!=null&&!mobile.equals("")){
            req.setMobile(MerchantSupport.encryptMobile(mobile));
        }
        Integer status = req.getStatus();
        if (status!=null) {
            if (status == 3) {
                List<MerchantInfoResponse> list = this.merchantInfoQueryDao.downloade1(req);
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getMobile() != null && !list.get(i).getMobile().equals("")) {
                            list.get(i).setMobile(MerchantSupport.decryptMobile(list.get(i).getMobile()));
                        }

                    }
                }
                return list;
            }
        }
        List<MerchantInfoResponse> list = this.merchantInfoQueryDao.downloade(req);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if(list.get(i).getMobile()!=null&&!list.get(i).getMobile().equals("")){
                    list.get(i).setMobile(MerchantSupport.decryptMobile(list.get(i).getMobile()));
                }

            }
        }
        return list;
    }

    @Override
    public int getCount(MerchantInfoRequest req) {
        Integer status = req.getStatus();
        if (status!=null){
            if (status==3){
                int count = merchantInfoQueryDao.getCount(req);
                return count;
            }
        }
        int count = merchantInfoQueryDao.getCount1(req);
        return count;
    }

    @Override
    public HistoryPhotoChangeResponse getPhoto(long merchantId) {
        return this.merchantInfoQueryDao.getPhoto(merchantId);
    }

    @Override
    public void saveHistory(long merchantId, String photo, int type, String reasonDescription, String cardName, String operator) {
        Map map = new HashMap();
        map.put("merchantId",merchantId);
        map.put("photo",photo);
        map.put("type",type);
        map.put("reasonDescription",reasonDescription);
        map.put("cardName",cardName);
        map.put("operator",operator);
        this.merchantInfoQueryDao.saveHistory(map);
    }

    @Override
    public void savePhotoChang(String photoName,long merchantId) {
        Map map = new HashMap();
        map.put("photoName",photoName);
        map.put("merchantId",merchantId);
        this.merchantInfoQueryDao.savePhotoChang(map);
    }

    @Override
    public void savePhotoChang1(String photoName,long merchantId) {
        Map map = new HashMap();
        map.put("photoName",photoName);
        map.put("merchantId",merchantId);
        this.merchantInfoQueryDao.savePhotoChang1(map);
    }

    @Override
    public void savePhotoChang2(String photoName,long merchantId) {
        Map map = new HashMap();
        map.put("photoName",photoName);
        map.put("merchantId",merchantId);
        this.merchantInfoQueryDao.savePhotoChang2(map);
    }

    @Override
    public void savePhotoChang3(String photoName,long merchantId) {
        Map map = new HashMap();
        map.put("photoName",photoName);
        map.put("merchantId",merchantId);
        this.merchantInfoQueryDao.savePhotoChang3(map);
    }

    @Override
    public void savePhotoChang4(String photoName,long merchantId) {
        Map map = new HashMap();
        map.put("photoName",photoName);
        map.put("merchantId",merchantId);
        this.merchantInfoQueryDao.savePhotoChang4(map);
    }

    @Override
    public List<HistoryPhotoChangeResponse> selectHistory(HistoryPhotoChangeRequest request) {
        List<HistoryPhotoChangeResponse> list = this.merchantInfoQueryDao.selectHistory(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getCreateTime()!=null&&!list.get(i).getCreateTime().equals("")){
                    String dates = sdf.format(list.get(i).getCreateTime());
                    list.get(i).setCreateTimes(dates);
                }
            }
        }
        return list;
    }

    @Override
    public int selectHistoryCount(HistoryPhotoChangeRequest request) {
        return this.merchantInfoQueryDao.selectHistoryCount(request);
    }

    @Override
    public HsyHistoryPhotoChangeResponse getHsyPhoto(long sid) {
        return this.merchantInfoQueryDao.getHsyPhoto(sid);
    }

    @Override
    public void saveHsyHistory(long sid, String photo, int hsyType, String reasonDescription, String cardName, String operator) {
        Map map = new HashMap();
        map.put("sid",sid);
        map.put("photo",photo);
        map.put("hsyType",hsyType);
        map.put("reasonDescription",reasonDescription);
        map.put("cardName",cardName);
        map.put("operator",operator);
        this.merchantInfoQueryDao.saveHsyHistory(map);
    }

    @Override
    public void saveHsyPhotoChang(String photoName, long sid) {
        Map map = new HashMap();
        map.put("photoName",photoName);
        map.put("sid",sid);
        this.merchantInfoQueryDao.saveHsyPhotoChang(map);
    }

    @Override
    public void saveHsyPhotoChang1(String photoName, long sid) {
        Map map = new HashMap();
        map.put("photoName",photoName);
        map.put("sid",sid);
        this.merchantInfoQueryDao.saveHsyPhotoChang1(map);
    }

    @Override
    public void saveHsyPhotoChang2(String photoName, long sid) {
        Map map = new HashMap();
        map.put("photoName",photoName);
        map.put("sid",sid);
        this.merchantInfoQueryDao.saveHsyPhotoChang2(map);
    }

    @Override
    public void saveHsyPhotoChang3(String photoName, long sid) {
        Map map = new HashMap();
        map.put("photoName",photoName);
        map.put("sid",sid);
        this.merchantInfoQueryDao.saveHsyPhotoChang3(map);
    }

    @Override
    public void saveHsyPhotoChang4(String photoName, long sid) {
        Map map = new HashMap();
        map.put("photoName",photoName);
        map.put("sid",sid);
        this.merchantInfoQueryDao.saveHsyPhotoChang4(map);
    }

    @Override
    public void saveHsyPhotoChang5(String photoName, long sid) {
        Map map = new HashMap();
        map.put("photoName",photoName);
        map.put("sid",sid);
        this.merchantInfoQueryDao.saveHsyPhotoChang5(map);
    }

    @Override
    public List<HsyHistoryPhotoChangeResponse> selectHsyHistory(HistoryPhotoChangeRequest request) {
        List<HsyHistoryPhotoChangeResponse> list = this.merchantInfoQueryDao.selectHsyHistory(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getCreateTime()!=null&&!list.get(i).getCreateTime().equals("")){
                    String dates = sdf.format(list.get(i).getCreateTime());
                    list.get(i).setCreateTimes(dates);
                }
            }
        }
        return list;
    }

    @Override
    public int selectHsyHistoryCount(HistoryPhotoChangeRequest request) {
        return this.merchantInfoQueryDao.selectHsyHistoryCount(request);
    }

    @Override
    public List<MerchantInfoResponse> getRecord(MerchantInfoRequest req) {

        List<MerchantInfoResponse> list = this.merchantInfoQueryDao.getRecord(req);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if(list.get(i).getMobile()!=null&&!list.get(i).getMobile().equals("")){
                    list.get(i).setMobile(MerchantSupport.decryptMobile(list.get(i).getId(),list.get(i).getMobile()));
                }
                if (list.get(i).getLevel()==1){
                    list.get(i).setProxyName(list.get(i).getProxyName());
                }if (list.get(i).getLevel()==2){
                    list.get(i).setProxyName1(list.get(i).getProxyName());
                    String proxyName = dealerService.selectProxyName(list.get(i).getFirstLevelDealerId());
                    list.get(i).setProxyName(proxyName);
                }
            }
        }

        return list;
    }

    @Override
    public int getCountRecord(MerchantInfoRequest req) {
        int count = this.merchantInfoQueryDao.getCountRecord(req);

        return count;
    }

    /**
     * 下载Excele
     * @param
     * @param baseUrl
     * @return
     */
    @Override
    @Transactional
    public String downloadExcel(MerchantInfoRequest req, String baseUrl) {
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
    private ExcelSheetVO generateCodeExcelSheet(MerchantInfoRequest req,String baseUrl) {
        List<MerchantInfoResponse> list = downloade(req);
        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<List<String>> datas = new ArrayList<List<String>>();
        final ArrayList<String> heads = new ArrayList<>();
        excelSheetVO.setName("Merchant");
        heads.add("商户编号");
        heads.add("商户名称");
        heads.add("所属一级代理");
        heads.add("所属二级代理");
        heads.add("注册时间");
        heads.add("注册手机号");
        heads.add("注册方式");
        heads.add("认证时间");
        heads.add("审核时间");
        heads.add("审核状态");
        datas.add(heads);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                ArrayList<String> columns = new ArrayList<>();
                columns.add(list.get(i).getMarkCode());
                columns.add(list.get(i).getMerchantName());
                columns.add(list.get(i).getProxyName());
                columns.add(list.get(i).getProxyName1());
                if (list.get(i).getCreateTime()!= null && !"".equals(list.get(i).getCreateTime())){
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String st = df.format(list.get(i).getCreateTime());
                    columns.add(st);

                }else {
                    columns.add("");
                }
                columns.add(list.get(i).getMobile());
                if (list.get(i).getSource()==0){
                    columns.add(EnumSource.SCAN.getValue());
                }
                if (list.get(i).getSource()==1){
                    columns.add(EnumSource.RECOMMEND.getValue());
                }
                if (list.get(i).getSource()==2){
                    columns.add(EnumSource.DEALERRECOMMEND.getValue());
                }
                if (list.get(i).getAuthenticationTime()!=null&&!list.get(i).getAuthenticationTime().equals("")){
                    columns.add(list.get(i).getAuthenticationTime().substring(0,list.get(i).getAuthenticationTime().length()-2));
                }else {
                    columns.add("");
                }
                if (list.get(i).getCheckedTime()!=null&&!list.get(i).getCheckedTime().equals("")){
                    columns.add(list.get(i).getCheckedTime().substring(0,list.get(i).getCheckedTime().length()-2));
                }else {
                    columns.add("");
                }

                if (list.get(i).getStatus()==0){
                    columns.add(EnumMerchantStatus.INIT.getName());
                }
                if (list.get(i).getStatus()==1){
                    columns.add(EnumMerchantStatus.ONESTEP.getName());
                }
                if (list.get(i).getStatus()==2){
                    columns.add(EnumMerchantStatus.REVIEW.getName());
                }
                if (list.get(i).getStatus()==3 || list.get(i).getStatus()==6){
                    columns.add(EnumMerchantStatus.PASSED.getName());
                }
                if (list.get(i).getStatus()==4){
                    columns.add(EnumMerchantStatus.UNPASSED.getName());
                }
                datas.add(columns);
            }
        }
        excelSheetVO.setDatas(datas);
        return excelSheetVO;
    }


}
