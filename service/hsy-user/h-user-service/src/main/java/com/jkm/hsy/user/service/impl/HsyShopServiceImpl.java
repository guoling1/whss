package com.jkm.hsy.user.service.impl;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.merchant.entity.BankCardBin;
import com.jkm.hss.merchant.service.BankCardBinService;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.constant.FileType;
import com.jkm.hsy.user.constant.IndustryCodeType;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.entity.*;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.jkm.hsy.user.service.HsyFileService;
import com.jkm.hsy.user.service.HsyShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * Created by Allen on 2017/1/10.
 */
@Service("hsyShopService")
public class HsyShopServiceImpl implements HsyShopService {
    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private BankCardBinService bankCardBinService;
    @Autowired
    private HsyFileService hsyFileService;
    @Autowired
    private HsyUserDao hsyUserDao;

    /**HSY001005 更新店铺资料店铺名字*/
    public String updateHsyShop(String dataParam,AppParam appParam,Map<String,MultipartFile> files)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppBizShop appBizShop=null;
        try{
            appBizShop=gson.fromJson(dataParam, AppBizShop.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appBizShop.getId()!=null&&!appBizShop.getId().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"店铺ID");
        if(!(appBizShop.getShortName()!=null&&!appBizShop.getShortName().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"店铺简称");
        if(!(appBizShop.getDistrictCode()!=null&&!appBizShop.getDistrictCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"所在地");
        if(!(appBizShop.getAddress()!=null&&!appBizShop.getAddress().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"街道");
        if(!(appBizShop.getIsPublic()!=null&&!appBizShop.getIsPublic().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"结算类型");

        String fileKey="fileA";
        MultipartFile file=files.get(fileKey);
        String type="";
        if(fileKey.equals("fileA")&& FileType.contains(appBizShop.getFileA()))
            type=appBizShop.getFileA();
        else
            throw new ApiHandleException(ResultCode.FILE_TYPE_NOT_EXSIT);
        String uuid="";
        try {
            uuid=hsyFileService.insertFileAndUpload(file, type);
        }catch(Exception e){
            e.printStackTrace();
            throw new ApiHandleException(ResultCode.FILE_UPLOAD_FAIL);
        }
        appBizShop.setLicenceID(uuid);

        /**商铺修改*/
        Date date=new Date();
        appBizShop.setUpdateTime(date);
        hsyShopDao.update(appBizShop);
        return "";
    }

    /**HSY001006 更新店铺资料联系人*/
    public String updateHsyShopContact(String dataParam,AppParam appParam,Map<String,MultipartFile> files)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();

        /**参数转化*/
        AppBizShop appBizShop=null;
        try{
            appBizShop=gson.fromJson(dataParam, AppBizShop.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appBizShop.getId()!=null&&!appBizShop.getId().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"店铺ID");
        if(!(appBizShop.getContactName()!=null&&!appBizShop.getContactName().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"联系人");
        if(!(appBizShop.getContactCellphone()!=null&&!appBizShop.getContactCellphone().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"联系人手机");
        if(!(appBizShop.getUid()!=null&&!appBizShop.getUid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"用户ID");
        if (!ValidateUtils.isMobile(appBizShop.getContactCellphone()))
            throw new ApiHandleException(ResultCode.CELLPHONE_NOT_CORRECT_FORMAT);

        if(files.size()!=3)
            throw new ApiHandleException(ResultCode.UPLOADFILE_COUNT_NOT_RIGHT);
        for (String fileKey : files.keySet()) {
            MultipartFile file=files.get(fileKey);
            if(file==null||(file!=null&&file.getSize()==0))
                throw new ApiHandleException(ResultCode.UPLOADFILE_NOT_EXSITS);
        }

        AppAuUser appAuUser=new AppAuUser();
        appAuUser.setId(appBizShop.getUid());
        Set<String> set=files.keySet();
        Iterator<String> it=set.iterator();
        while(it.hasNext()){
            String fileKey=it.next();
            MultipartFile file=files.get(fileKey);
            String type="";
            if(fileKey.equals("fileA")&&FileType.contains(appBizShop.getFileA()))
                type=appBizShop.getFileA();
            else if(fileKey.equals("fileB")&&FileType.contains(appBizShop.getFileB()))
                type=appBizShop.getFileB();
            else if(fileKey.equals("fileC")&&FileType.contains(appBizShop.getFileC()))
                type=appBizShop.getFileC();
            else
                throw new ApiHandleException(ResultCode.FILE_TYPE_NOT_EXSIT);
            String uuid="";
            try {
                uuid=hsyFileService.insertFileAndUpload(file, type);
            }catch(Exception e){
                e.printStackTrace();
                throw new ApiHandleException(ResultCode.FILE_UPLOAD_FAIL);
            }
            if(type.equals(FileType.IDCARDF.fileIndex))
                appAuUser.setIdcardf(uuid);
            else if(type.equals(FileType.IDCARDB.fileIndex))
                appAuUser.setIdcardb(uuid);
            else if(type.equals(FileType.IDCARDH.fileIndex))
                appAuUser.setIdcardh(uuid);

        }

        /**商铺 用户修改*/
        Date date=new Date();
        appBizShop.setUpdateTime(date);
        hsyShopDao.update(appBizShop);
        appAuUser.setUpdateTime(date);
        hsyUserDao.updateByID(appAuUser);
        return "";
    }

    /**HSY001007 保存结算账户*/
    public String insertHsyCard(String dataParam,AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();

        /**参数转化*/
        AppBizCard appBizCard=null;
        try{
            appBizCard=gson.fromJson(dataParam, AppBizCard.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appBizCard.getCardNO()!=null&&!appBizCard.getCardNO().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"结算卡号");
        if(!(appBizCard.getCardBank()!=null&&!appBizCard.getCardBank().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"开户行");
        if(!(appBizCard.getBankAddress()!=null&&!appBizCard.getBankAddress().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"所在支行");
        if(!(appBizCard.getCardAccountName()!=null&&!appBizCard.getCardAccountName().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"开户名");
        if(!(appBizCard.getSid()!=null&&!appBizCard.getSid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"商铺ID");

        /**数据验证*/
        List<AppBizCard> appBizCardList=hsyShopDao.findAppBizCardByParam(appBizCard);
        if(appBizCardList!=null&&appBizCardList.size()!=0)
            throw new ApiHandleException(ResultCode.CARDNO_HAS_EXSITED);

        /**结算账户保存*/
        Date date=new Date();
        appBizCard.setCreateTime(date);
        appBizCard.setUpdateTime(date);
        appBizCard.setStatus(AppConstant.CARD_STATUS_NORMAL);
        hsyShopDao.insertAppBizCard(appBizCard);
        return "{\"id\":"+appBizCard.getId()+"}";
    }

    /**HSY001008 卡号匹配银行*/
    public String findHsyCardBankByBankNO(String dataParam,AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();

        /**参数转化*/
        AppBizCard appBizCard=null;
        try{
            appBizCard=gson.fromJson(dataParam, AppBizCard.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appBizCard.getCardNO()!=null&&!appBizCard.getCardNO().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"银行卡号");

        Optional<BankCardBin> bankCardBinOptional=bankCardBinService.analyseCardNo(appBizCard.getCardNO());
        return gson.toJson(bankCardBinOptional.get());
    }

    /**HSY001009 读取地区下拉列表*/
    public String findDistrictByParentCode(String dataParam,AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();

        /**参数转化*/
        AppBizDistrict appBizDistrict=null;
        try{
            appBizDistrict=gson.fromJson(dataParam, AppBizDistrict.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appBizDistrict.getParentCode()!=null&&!appBizDistrict.getParentCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"");

        List<AppBizDistrict> list=hsyShopDao.findDistrictByParentCode(appBizDistrict);
        return gson.toJson(list);
    }

    /**HSY001013 找到行业列表*/
    public String findIndustryList(String dataParam,AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        List<Map<String,String>> list=new ArrayList<Map<String,String>>();
        for(IndustryCodeType industryCodeType:IndustryCodeType.values()) {
            Map<String,String> map=new HashMap<String,String>();
            map.put("key", industryCodeType.industryCodeKey+"");
            map.put("value", industryCodeType.industryCodeValue);
            list.add(map);
        }
        return gson.toJson(list);
    }
}
