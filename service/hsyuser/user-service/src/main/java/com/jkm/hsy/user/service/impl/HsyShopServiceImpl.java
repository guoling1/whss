package com.jkm.hsy.user.service.impl;

import com.google.common.base.Optional;
import com.google.gson.*;
import com.jkm.base.common.enums.EnumGlobalIDPro;
import com.jkm.base.common.enums.EnumGlobalIDType;
import com.jkm.base.common.util.GlobalID;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.admin.helper.responseparam.QRCodeList;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.merchant.entity.BankCardBin;
import com.jkm.hss.merchant.service.BankCardBinService;
import com.jkm.hsy.user.constant.*;
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

import java.lang.reflect.Type;
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
    @Autowired
    private QRCodeService qRCodeService;

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
        if(appBizShop.getIsPublic()==1) {
            if (!(appBizShop.getLicenceNO() != null && !appBizShop.getLicenceNO().equals("")))
                throw new ApiHandleException(ResultCode.PARAM_LACK, "营业执照号");
            if (!(appBizShop.getLicenceStartDate() != null && !appBizShop.getLicenceStartDate().equals("")))
                throw new ApiHandleException(ResultCode.PARAM_LACK, "营业执照起期");
            if (!(appBizShop.getLicenceEndDate() != null && !appBizShop.getLicenceEndDate().equals("")))
                throw new ApiHandleException(ResultCode.PARAM_LACK, "营业执照止期");
        }

        String fileKey="fileA";
        MultipartFile file=files.get(fileKey);
        if(file!=null&&file.getSize()!=0) {
            String type = "";
            if (fileKey.equals("fileA") && FileType.contains(appBizShop.getFileA()))
                type = appBizShop.getFileA();
            else
                throw new ApiHandleException(ResultCode.FILE_TYPE_NOT_EXSIT);
            String uuid = "";
            try {
                uuid = hsyFileService.insertFileAndUpload(file, type);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ApiHandleException(ResultCode.FILE_UPLOAD_FAIL);
            }
            appBizShop.setLicenceID(uuid);
        }

        /**商铺修改*/
        Date date=new Date();
        appBizShop.setUpdateTime(date);
        appBizShop.setStatus(AppConstant.SHOP_STATUS_REGISTERED);
        hsyShopDao.update(appBizShop);

        List<AppBizShopUserRole> surList=hsyShopDao.findsurByRoleTypeSid(appBizShop.getId());
        if(surList!=null&&surList.size()!=0)
        {
            AppBizShopUserRole sur= surList.get(0);
            AppAuUser user=new AppAuUser();
            user.setId(sur.getUid());
            user.setAuStep("1");
            user.setUpdateTime(date);
            hsyUserDao.updateByID(user);
        }
        return "{\"auStep\":\"1\"}";
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

        MultipartFile fileA=files.get("fileA");
        if(fileA==null||(fileA!=null&&fileA.getSize()==0))
            throw new ApiHandleException(ResultCode.UPLOADFILE_NOT_EXSITS,"fileA");
        MultipartFile fileB=files.get("fileB");
        if(fileB==null||(fileB!=null&&fileB.getSize()==0))
            throw new ApiHandleException(ResultCode.UPLOADFILE_NOT_EXSITS,"fileB");

        AppAuUser appAuUser=new AppAuUser();
        appAuUser.setId(appBizShop.getUid());
        files.remove("fileC");
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
        }

        /**商铺 用户修改*/
        Date date=new Date();
        appBizShop.setUpdateTime(date);
        hsyShopDao.update(appBizShop);
        appAuUser.setUpdateTime(date);
        appAuUser.setAuStep("3");
        appAuUser.setRealname(appBizShop.getContactName());
        hsyUserDao.updateByID(appAuUser);
        return "{\"auStep\":\"3\"}";
    }
    /**HSY001006 更新店铺资料联系人 v1o6版本增加 by wayne*/
    public String updateHsyShopContact1o6(String dataParam,AppParam appParam,Map<String,MultipartFile> files)throws ApiHandleException{
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

        MultipartFile fileA=files.get("fileA");
        if(fileA==null||(fileA!=null&&fileA.getSize()==0))
            throw new ApiHandleException(ResultCode.UPLOADFILE_NOT_EXSITS,"fileA");
        MultipartFile fileB=files.get("fileB");
        if(fileB==null||(fileB!=null&&fileB.getSize()==0))
            throw new ApiHandleException(ResultCode.UPLOADFILE_NOT_EXSITS,"fileB");
        MultipartFile fileC=files.get("fileC");
        if(fileC==null||(fileC!=null&&fileC.getSize()==0))
            throw new ApiHandleException(ResultCode.UPLOADFILE_NOT_EXSITS,"fileC");
//        MultipartFile fileD=files.get("fileD");
//        if(fileD==null||(fileD!=null&&fileD.getSize()==0))
//            throw new ApiHandleException(ResultCode.UPLOADFILE_NOT_EXSITS,"fileD");


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
//            else if(fileKey.equals("fileD")&&FileType.contains(appBizShop.getFileD()))
//                type=appBizShop.getFileD();
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
            else if(type.equals(FileType.IDCARDC.fileIndex))
                appAuUser.setIdcardc(uuid);
//            else if(type.equals(FileType.CONTRACT.fileIndex))
//                appAuUser.setContractID(uuid);
        }

        /**商铺 用户修改*/
        Date date=new Date();
        appBizShop.setUpdateTime(date);
        hsyShopDao.update(appBizShop);
        appAuUser.setUpdateTime(date);
        appAuUser.setAuStep("3");
        appAuUser.setRealname(appBizShop.getContactName());
        hsyUserDao.updateByID(appAuUser);
        return "{\"auStep\":\"3\"}";
    }

    /**
     * 设置商户邮箱
     */
    public String updateHsyShopEmail(String dataParam, AppParam appParam) throws ApiHandleException {
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
        if(!(appBizShop.getEmail()!=null&&!appBizShop.getEmail().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"邮箱");
        /**商铺 用户修改*/
        Date date=new Date();
        appBizShop.setUpdateTime(date);
        hsyShopDao.update(appBizShop);
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
        if(!(appBizCard.getBranchCode()!=null&&!appBizCard.getBranchCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"联行号");
        if(appBizCard.getBranchCode().equals("-1"))
            appBizCard.setBranchCode(null);
        if(!(appBizCard.getBankAddress()!=null&&!appBizCard.getBankAddress().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"所在支行");
        if(!(appBizCard.getCardAccountName()!=null&&!appBizCard.getCardAccountName().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"开户名");
        if(!(appBizCard.getSid()!=null&&!appBizCard.getSid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"商铺ID");
        if(!(appBizCard.getIdcardNO()!=null&&!appBizCard.getIdcardNO().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"身份证号");
        if(!(appBizCard.getBranchDistrictCode()!=null&&!appBizCard.getBranchDistrictCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"联行号地区代码");

        /**数据验证*/
        List<AppBizCard> appBizCardList=hsyShopDao.findAppBizCardByParam(appBizCard);
        Date date=new Date();
        if(appBizCardList!=null&&appBizCardList.size()!=0)
        {
            /**结算账户更新*/
            appBizCard.setId(appBizCardList.get(0).getId());
            appBizCard.setUpdateTime(date);
            appBizCard.setStatus(AppConstant.CARD_STATUS_NORMAL);
            hsyShopDao.updateAppBizCard(appBizCard);
        }
        else
        {
            /**结算账户保存*/
            appBizCard.setCreateTime(date);
            appBizCard.setUpdateTime(date);
            appBizCard.setStatus(AppConstant.CARD_STATUS_NORMAL);
            hsyShopDao.insertAppBizCard(appBizCard);
        }

        List<AppBizShopUserRole> surList=hsyShopDao.findsurByRoleTypeSid(appBizCard.getSid());
        if(surList!=null&&surList.size()!=0)
        {
            AppBizShopUserRole sur= surList.get(0);
            AppAuUser user=new AppAuUser();
            user.setId(sur.getUid());
            user.setIdcard(appBizCard.getIdcardNO());
            user.setAuStep("4");
            user.setUpdateTime(date);
            hsyUserDao.updateByID(user);
        }

        AppBizShop appBizShop=new AppBizShop();
        appBizShop.setId(appBizCard.getSid());
        appBizShop.setStatus(AppConstant.SHOP_STATUS_NO_CHECK);
        appBizShop.setCommitTime(new Date());
        hsyShopDao.update(appBizShop);
        return "{\"id\":"+appBizCard.getId()+",\"auStep\":\"4\",\"status\":"+AppConstant.SHOP_STATUS_NO_CHECK+"}";
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

        Optional<BankCardBin> bankCardBinOptional=null;
        try {
            bankCardBinOptional = bankCardBinService.analyseCardNo(appBizCard.getCardNO());
        }catch(Exception e){
            return gson.toJson(null);
        }
        if(bankCardBinOptional.isPresent())
            return gson.toJson(bankCardBinOptional.get());
        else
            return gson.toJson(bankCardBinOptional.absent());
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
            throw new ApiHandleException(ResultCode.PARAM_LACK,"父节点代码");

        List<AppBizDistrict> list=hsyShopDao.findDistrictByParentCode(appBizDistrict);
        return gson.toJson(list);
    }

    /**HSY001010 找到店铺列表*/
    public String findShopList(String dataParam,AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppBizShop appBizShop=null;
        try{
            appBizShop=gson.fromJson(dataParam, AppBizShop.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appBizShop.getUid()!=null&&!appBizShop.getUid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"店铺ID");

        List<AppBizShop> shopList=hsyShopDao.findShopList(appBizShop);
        gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
                return new JsonPrimitive(date.getTime());
            }
        }).registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();
        Map map=new HashMap();
        map.put("shopList",shopList);
        return gson.toJson(map);
    }

    /**HSY001012 找到店铺细节*/
    public String findShopDetail(String dataParam,AppParam appParam)throws ApiHandleException{
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
            throw new ApiHandleException(ResultCode.PARAM_LACK,"商铺ID");

        List<AppBizShop> shopList=hsyShopDao.findShopDetail(appBizShop);
        List<AppAuUser> userList=hsyShopDao.findUserByShopID(appBizShop.getId());
        List<QRCodeList> qrList=qRCodeService.bindShopList(appBizShop.getId(),AppConstant.FIlE_ROOT);
        if(shopList!=null&&shopList.size()!=0)
            appBizShop=shopList.get(0);
        Map map=new HashMap();
        map.put("appBizShop",appBizShop);
        map.put("userList",userList);
        map.put("qrList",qrList);
        map.put("qrUrl",AppConstant.QR_URL);
        gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().contains("password");
            }
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
                return new JsonPrimitive(date.getTime());
            }
        }).registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();
        return gson.toJson(map);
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

    /**HSY001014 新增店铺*/
    public String insertBranchShop(String dataParam,AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppBizShop appBizShop=null;
        try{
            appBizShop=gson.fromJson(dataParam, AppBizShop.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appBizShop.getShortName()!=null&&!appBizShop.getShortName().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"店铺简称");
        if(!(appBizShop.getDistrictCode()!=null&&!appBizShop.getDistrictCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"所在地");
        if(!(appBizShop.getAddress()!=null&&!appBizShop.getAddress().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"街道");
        if(!(appBizShop.getUid()!=null&&!appBizShop.getUid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"用户ID");

        appBizShop.setType(AppConstant.ROLE_TYPE_PRIMARY);
        AppBizShop primaryAppBizShop=null;
        List<AppBizShop> shopList=hsyShopDao.findPrimaryAppBizShopByUserID(appBizShop);
        if(!(shopList!=null&&shopList.size()!=0))
            throw new ApiHandleException(ResultCode.PRIMARY_SHOP_NOT_EXSIT);
        else
            primaryAppBizShop=shopList.get(0);

        appBizShop.setName(primaryAppBizShop.getName());
        appBizShop.setIndustryCode(primaryAppBizShop.getIndustryCode());
        appBizShop.setLicenceID(primaryAppBizShop.getLicenceID());
        appBizShop.setStorefrontID(primaryAppBizShop.getStorefrontID());
        appBizShop.setCounterID(primaryAppBizShop.getCounterID());
        appBizShop.setIndoorID(primaryAppBizShop.getIndoorID());
        appBizShop.setParentID(primaryAppBizShop.getId());
        appBizShop.setContactName(primaryAppBizShop.getContactName());
        if(!(appBizShop.getUid()!=null&&!appBizShop.getUid().equals("")))
            appBizShop.setContactCellphone(primaryAppBizShop.getContactCellphone());
        appBizShop.setStatus(AppConstant.SHOP_STATUS_NORMAL);
        appBizShop.setIsPublic(primaryAppBizShop.getIsPublic());
        Date date=new Date();
        appBizShop.setCreateTime(date);
        appBizShop.setUpdateTime(date);
        hsyShopDao.insert(appBizShop);
        AppBizShopUserRole appBizShopUserRole=new AppBizShopUserRole();
        appBizShopUserRole.setSid(appBizShop.getId());
        appBizShopUserRole.setUid(appBizShop.getUid());
        appBizShopUserRole.setRole(AppConstant.ROLE_CORPORATION);
        appBizShopUserRole.setStatus(AppConstant.ROLE_STATUS_NORMAL);
        appBizShopUserRole.setType(AppConstant.ROLE_TYPE_BRANCH);
        hsyShopDao.insertAppBizShopUserRole(appBizShopUserRole);
        AppBizShop appBizShopUp=new AppBizShop();
        appBizShopUp.setId(appBizShop.getId());
        appBizShopUp.setGlobalID(GlobalID.GetGlobalID(EnumGlobalIDType.MERCHANT, EnumGlobalIDPro.MAX,appBizShop.getId().toString()));
        hsyShopDao.update(appBizShopUp);
        return "{\"id\":"+appBizShop.getId()+"}";
    }

    /**HSY001015 店铺签约信息*/
    public String findContractInfo(String dataParam,AppParam appParam)throws ApiHandleException{
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
            throw new ApiHandleException(ResultCode.PARAM_LACK,"商铺ID");

        List<AppBizShop> shopList=hsyShopDao.findShopDetail(appBizShop);
        List<AppAuUser> userList=hsyShopDao.findCorporateUserByShopID(appBizShop.getId());
        if(shopList!=null&&shopList.size()!=0)
            appBizShop=shopList.get(0);
        AppAuUser appAuUser=null;
        if(userList!=null&&userList.size()!=0)
            appAuUser=userList.get(0);
        List<Map> rateList=new ArrayList<Map>();
        Map rateMapWX=new HashMap();
        rateMapWX.put("name","微信费率");
        rateMapWX.put("rate",appAuUser==null||appAuUser.getWeixinRate()==null?0:appAuUser.getWeixinRate());
        rateList.add(rateMapWX);
        Map rateMapAL=new HashMap();
        rateMapAL.put("name","支付宝费率");
        rateMapAL.put("rate",appAuUser==null||appAuUser.getAlipayRate()==null?0:appAuUser.getAlipayRate());
        rateList.add(rateMapAL);
        Map rateMapF=new HashMap();
        rateMapF.put("name","快捷费率");
        rateMapF.put("rate",appAuUser==null||appAuUser.getFastRate()==null?0:appAuUser.getFastRate());
        rateList.add(rateMapF);

        Map map=new HashMap();
        map.put("appBizShop",appBizShop);
        map.put("rateList",rateList);

        gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
                return new JsonPrimitive(date.getTime());
            }
        }).registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();
        return gson.toJson(map);
    }

    /**HSY001041 查询支行列表*/
    public String findBankBranchList(String dataParam,AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppBizBankBranch bankBranch=null;
        try{
            bankBranch=gson.fromJson(dataParam, AppBizBankBranch.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(bankBranch.getBankName()!=null&&!bankBranch.getBankName().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"开户行");
        if(!(bankBranch.getDistrictCode()!=null&&!bankBranch.getDistrictCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"地区代码");
        if(!(bankBranch.getCurrentPage()!=null&&!bankBranch.getCurrentPage().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"当前页数");
        if(bankBranch.getCurrentPage()<=0)
            throw new ApiHandleException(ResultCode.CURRENT_PAGE_MUST_BE_BIGGER_THAN_ZERO);

        if(bankBranch.getDistrictCode().substring(0,2).equals("11")||bankBranch.getDistrictCode().substring(0,2).equals("12")||bankBranch.getDistrictCode().substring(0,2).equals("31")||bankBranch.getDistrictCode().substring(0,2).equals("50"))
            bankBranch.setDistrictCode(bankBranch.getDistrictCode().substring(0,2)+"0000");
        else
            bankBranch.setDistrictCode(bankBranch.getDistrictCode().substring(0,4)+"00");

        PageUtils page=new PageUtils();
        page.setCurrentPage(bankBranch.getCurrentPage());
        page.setPageSize(AppConstant.PAGE_SIZE);
        Page<AppBizBankBranch> pageAll=new Page<AppBizBankBranch>();
        pageAll.setObjectT(bankBranch);
        pageAll.setPage(page);
        pageAll.getPage().setTotalRecord(hsyShopDao.findBankBranchListByPageCount(pageAll.getObjectT()));
        pageAll.setList(hsyShopDao.findBankBranchListByPage(pageAll));

        gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            public boolean shouldSkipField(FieldAttributes f) {
                boolean flag=false;
                if(f.getName().contains("objectT"))
                    return true;
                if(f.getName().contains("viewpagecount"))
                    return true;
                if(f.getName().contains("startPageIndex"))
                    return true;
                if(f.getName().contains("endPageIndex"))
                    return true;
                return flag;
            }
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
                return new JsonPrimitive(date.getTime());
            }
        }).registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();
        return gson.toJson(pageAll);
    }

    /**HSY001042 查询银行列表*/
    public String findBankList(String dataParam,AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppBizBankBranch bankBranch=null;
        try{
            bankBranch=gson.fromJson(dataParam, AppBizBankBranch.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(bankBranch.getCurrentPage()!=null&&!bankBranch.getCurrentPage().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"当前页数");
        if(bankBranch.getCurrentPage()<=0)
            throw new ApiHandleException(ResultCode.CURRENT_PAGE_MUST_BE_BIGGER_THAN_ZERO);

        PageUtils page=new PageUtils();
        page.setCurrentPage(bankBranch.getCurrentPage());
        page.setPageSize(AppConstant.PAGE_SIZE);
        Page<AppBizBankBranch> pageAll=new Page<AppBizBankBranch>();
        pageAll.setObjectT(bankBranch);
        pageAll.setPage(page);
        pageAll.getPage().setTotalRecord(hsyShopDao.findBankListByPageCount(pageAll.getObjectT()));
        pageAll.setList(hsyShopDao.findBankListByPage(pageAll));
        gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            public boolean shouldSkipField(FieldAttributes f) {
                boolean flag=false;
                if(f.getName().contains("objectT"))
                    return true;
                if(f.getName().contains("viewpagecount"))
                    return true;
                if(f.getName().contains("startPageIndex"))
                    return true;
                if(f.getName().contains("endPageIndex"))
                    return true;
                return flag;
            }
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
                return new JsonPrimitive(date.getTime());
            }
        }).registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();
        return gson.toJson(pageAll);
    }

    @Override
    public List<AppBizBankBranch> getBankNameList(String bankName) {
        List<AppBizBankBranch> list = this.hsyShopDao.getBankNameList(bankName);
        return list;
    }
}
