package com.jkm.hsy.user.service.impl;

import com.google.common.base.Optional;
import com.google.gson.*;
import com.jkm.base.common.enums.EnumGlobalIDPro;
import com.jkm.base.common.enums.EnumGlobalIDType;
import com.jkm.base.common.util.GlobalID;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.base.sms.service.SmsSendMessageService;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.notifier.dao.MessageTemplateDao;
import com.jkm.hss.notifier.dao.SendMessageRecordDao;
import com.jkm.hss.notifier.entity.SendMessageRecord;
import com.jkm.hss.notifier.entity.SmsTemplate;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.constant.VerificationCodeType;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.dao.HsyVerificationDao;
import com.jkm.hsy.user.entity.*;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.jkm.hsy.user.service.HsyUserService;
import com.jkm.hsy.user.util.AppDateUtil;
import com.jkm.hsy.user.util.ShaUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

@Service("hsyUserService")
public class HsyUserServiceImpl implements HsyUserService {
    @Autowired
    private HsyUserDao hsyUserDao;
    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private HsyVerificationDao hsyVerificationDao;
    @Autowired
    private SmsSendMessageService smsSendMessageService;
    @Autowired
    private MessageTemplateDao messageTemplateDao;
    @Autowired
    private SendMessageRecordDao sendMessageRecordDao;
    @Autowired
    private AccountService accountService;

    /**HSY001001 注册用户*/
    public String insertHsyUser(String dataParam,AppParam appParam)throws ApiHandleException {
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppAuUser appAuUser=null;
        try{
            appAuUser=gson.fromJson(dataParam, AppAuUser.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appAuUser.getCellphone()!=null&&!appAuUser.getCellphone().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"手机号");
        if(!(appAuUser.getPassword()!=null&&!appAuUser.getPassword().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"密码");
        if (!ValidateUtils.isMobile(appAuUser.getCellphone()))
            throw new ApiHandleException(ResultCode.CELLPHONE_NOT_CORRECT_FORMAT);
        if(!(appAuUser.getCode()!=null&&!appAuUser.getCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"验证码");
        if(!(appAuUser.getShopName()!=null&&!appAuUser.getShopName().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"店铺名称");
        if(!(appAuUser.getIndustryCode()!=null&&!appAuUser.getIndustryCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"所属行业");

        /**数据验证*/
        List<AppAuUser> list = hsyUserDao.findAppAuUserByParam(appAuUser);
        if (list != null && list.size() != 0)
            throw new ApiHandleException(ResultCode.CELLPHONE_HAS_BEEN_REGISTERED);

        AppAuVerification appAuVerification=new AppAuVerification();
        appAuVerification.setCellphone(appAuUser.getCellphone());
        appAuVerification.setTimeout(new Date());
        appAuVerification.setType(VerificationCodeType.REGISTER.verificationCodeKey);
        List<AppAuVerification> vlist=hsyVerificationDao.findVCodeWithinTime(appAuVerification);
        if(vlist==null||(vlist!=null&&vlist.size()==0))
            throw new ApiHandleException(ResultCode.VERIFICATIONCODE_NO_EFFICACY);
        if(!vlist.get(0).getCode().equals(appAuUser.getCode()))
            throw new ApiHandleException(ResultCode.VERIFICATIONCODE_ERROR);

        /**用户 商铺 中间表 保存*/
        Date date=new Date();
        appAuUser.setStatus(AppConstant.USER_STATUS_NORMAL);
        appAuUser.setAuStep("0");
        appAuUser.setParentID(0L);
        appAuUser.setCreateTime(date);
        appAuUser.setUpdateTime(date);
        hsyUserDao.insert(appAuUser);
        AppAuUser appAuUserUp=new AppAuUser();
        appAuUserUp.setId(appAuUser.getId());
        appAuUserUp.setGlobalID(GlobalID.GetGlobalID(EnumGlobalIDType.MERCHANT, EnumGlobalIDPro.MAX,appAuUser.getId().toString()));
        hsyUserDao.updateByID(appAuUserUp);
        AppBizShop appBizShop=new AppBizShop();
        appBizShop.setName(appAuUser.getShopName());
        appBizShop.setIndustryCode(appAuUser.getIndustryCode());
        appBizShop.setCreateTime(date);
        appBizShop.setUpdateTime(date);
        appBizShop.setStatus(AppConstant.SHOP_STATUS_REGISTERED);
        appBizShop.setParentID(0L);
        hsyShopDao.insert(appBizShop);
        AppBizShop appBizShopUp=new AppBizShop();
        appBizShopUp.setId(appBizShop.getId());
        appBizShopUp.setGlobalID(GlobalID.GetGlobalID(EnumGlobalIDType.MERCHANT, EnumGlobalIDPro.MAX,appBizShop.getId().toString()));
        hsyShopDao.update(appBizShopUp);
        AppBizShopUserRole appBizShopUserRole=new AppBizShopUserRole();
        appBizShopUserRole.setSid(appBizShop.getId());
        appBizShopUserRole.setUid(appAuUser.getId());
        appBizShopUserRole.setRole(AppConstant.ROLE_CORPORATION);
        appBizShopUserRole.setStatus(AppConstant.ROLE_STATUS_NORMAL);
        appBizShopUserRole.setType(AppConstant.ROLE_TYPE_PRIMARY);
        hsyShopDao.insertAppBizShopUserRole(appBizShopUserRole);
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
        Map map=new HashMap();
        appAuUser.setCode(null);
        map.put("appAuUser",appAuUser);
        map.put("appBizShop",appBizShop);
        return gson.toJson(map);
    }

    /**HSY001002 用户登录*/
    public String login(String dataParam,AppParam appParam)throws ApiHandleException {
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppAuUser appAuUser=null;
        try{
            appAuUser=gson.fromJson(dataParam, AppAuUser.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appAuUser.getCellphone()!=null&&!appAuUser.getCellphone().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"手机号");
        if(!(appAuUser.getPassword()!=null&&!appAuUser.getPassword().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"密码");
        if(!(appParam.getAccessToken()!=null&&!appParam.getAccessToken().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"令牌（公参）");

        /**查询用户*/
        List<AppAuUser> list = hsyUserDao.findAppAuUserByParam(appAuUser);
        if (!(list != null && list.size() != 0))
            throw new ApiHandleException(ResultCode.CEELLPHONE_HAS_NOT_BEEN_REGISTERED);
        AppAuUser appAuUserFind=list.get(0);
        if(!appAuUserFind.getPassword().equals(appAuUser.getPassword()))
            throw new ApiHandleException(ResultCode.PASSWORD_NOT_CORRECT);
        if(appAuUserFind.getStatus().equals(AppConstant.USER_STATUS_FORBID))
            throw new ApiHandleException(ResultCode.USER_FORBID);

        List<AppAuToken> tokenList=hsyUserDao.findAppAuTokenByAccessToken(appParam.getAccessToken());
        if (tokenList != null && tokenList.size() != 0)
        {
            hsyUserDao.updateAppAuUserTokenStatus(appAuUserFind.getId());

            AppAuUserToken appAuUserToken=new AppAuUserToken();
            appAuUserToken.setUid(appAuUserFind.getId());
            appAuUserToken.setTid(tokenList.get(0).getId());
            List<AppAuUserToken> appAuUserTokenList=hsyUserDao.findAppAuUserTokenByParam(appAuUserToken);
            if(appAuUserTokenList!=null&&appAuUserTokenList.size()!=0)
            {
                AppAuUserToken appAuUserTokenUpdate=appAuUserTokenList.get(0);
                appAuUserTokenUpdate.setStatus(1);
                appAuUserTokenUpdate.setLoginTime(new Date());
                hsyUserDao.updateAppAuUserTokenByUidAndTid(appAuUserTokenUpdate);
            }
            else
            {
                appAuUserToken.setStatus(1);
                appAuUserToken.setLoginTime(new Date());
                hsyUserDao.insertAppAuUserToken(appAuUserToken);
            }
        }
        else
            throw new ApiHandleException(ResultCode.ACCESSTOKEN_NOT_FOUND);

        AppBizShop appBizShop=new AppBizShop();
        appBizShop.setUid(appAuUserFind.getId());
        if(appAuUserFind.getParentID()==null||(appAuUserFind.getParentID()!=null&&appAuUserFind.getParentID()==0L))
            appBizShop.setType(AppConstant.ROLE_TYPE_PRIMARY);
        else
        {
            List<AppAuUser> parentList=hsyUserDao.findAppAuUserByID(appAuUserFind.getParentID());
            if(parentList!=null&&parentList.size()!=0)
                appAuUserFind.setAccountID(parentList.get(0).getAccountID());
        }
        List<AppBizShop> shopList=hsyShopDao.findPrimaryAppBizShopByUserID(appBizShop);
        if(shopList!=null&&shopList.size()!=0)
            appBizShop=shopList.get(0);
//        if(AppConstant.USER_STATUS_NORMAL!=appAuUserFind.getStatus())
//            throw new ApiHandleException(ResultCode.USER_NO_CEHCK);
        if(appBizShop.getCheckErrorInfo()==null)
            appBizShop.setCheckErrorInfo("");
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
        Map map=new HashMap();
        map.put("appAuUser",appAuUserFind);
        map.put("appBizShop",appBizShop);

        return gson.toJson(map);
    }

    /**HSY001003 发送验证码*/
    public String insertAndSendVerificationCode(String dataParam,AppParam appParam)throws ApiHandleException{
        Date date=new Date();
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppAuVerification appAuVerification=null;
        try{
            appAuVerification=gson.fromJson(dataParam, AppAuVerification.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appAuVerification.getCellphone()!=null&&!appAuVerification.getCellphone().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"手机号");
        if (!ValidateUtils.isMobile(appAuVerification.getCellphone()))
            throw new ApiHandleException(ResultCode.CELLPHONE_NOT_CORRECT_FORMAT);
        if(!(appAuVerification.getType()!=null))
            throw new ApiHandleException(ResultCode.VERIFICATIONCODE_TYPE_NULL);
        if(!VerificationCodeType.contains(appAuVerification.getType()))
            throw new ApiHandleException(ResultCode.VERIFICATIONCODE_TYPE_NOT_EXSIT);

        /**数据验证*/
        appAuVerification.setCreateTime(AppDateUtil.changeDate(date, Calendar.MINUTE,-1));
        appAuVerification.setUpdateTime(date);
        Integer countFrequent=hsyVerificationDao.findVCodeCountWithinTime(appAuVerification);
        if(countFrequent>0)
            throw new ApiHandleException(ResultCode.VERIFICATIONCODE_FREQUENT);

        AppAuUser appAuUser=new AppAuUser();
        appAuUser.setCellphone(appAuVerification.getCellphone());
        List<AppAuUser> list = hsyUserDao.findAppAuUserByParam(appAuUser);
        if(appAuVerification.getType()==1) {
            if (list != null && list.size() != 0)
                throw new ApiHandleException(ResultCode.CELLPHONE_HAS_BEEN_REGISTERED);
        } else if(appAuVerification.getType()==2){
            if (!(list != null && list.size() != 0))
                throw new ApiHandleException(ResultCode.CEELLPHONE_HAS_NOT_BEEN_REGISTERED);
        }


        SmsTemplate messageTemplate = messageTemplateDao.getTemplateByType(AppConstant.REGISTER_VERIFICATION_NOTICE_TYPE_ID);
        String template="";
        if(messageTemplate!=null&&messageTemplate.getMessageTemplate()!=null&&!messageTemplate.getMessageTemplate().trim().equals(""))
            template=messageTemplate.getMessageTemplate();
        else
            template=AppConstant.REGISTER_VERIFICATION_MESSAGE;
        /**发送验证码*/
        String sn="";
        String code=(int)((Math.random()*9+1)*100000)+"";
        String content=template.replace("${code}",code).replace("${type}",VerificationCodeType.getValue(appAuVerification.getType())).replace("+", "%2B").replace("%", "%25");
        try {
            sn = smsSendMessageService.sendMessage(appAuVerification.getCellphone(), content);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiHandleException(ResultCode.VERIFICATIONCODE_SEND_FAIL);
        }

        SendMessageRecord sendRecord = new SendMessageRecord();
        sendRecord.setUserType(1);
        sendRecord.setMobile(appAuVerification.getCellphone());
        sendRecord.setContent(content);
        sendRecord.setMessageTemplateId(messageTemplate.getId());
        sendRecord.setSn(sn);
        sendRecord.setStatus(1);
        sendMessageRecordDao.insert(sendRecord);

        /**验证码保存*/
        appAuVerification.setSn(sn);
        appAuVerification.setTimeout(AppDateUtil.changeDate(date, Calendar.MINUTE,5));
        Date dateN=new Date();
        appAuVerification.setCreateTime(dateN);
        appAuVerification.setUpdateTime(dateN);
        appAuVerification.setCode(code);
        hsyVerificationDao.insert(appAuVerification);
        return "{\"sn\":\""+sn+"\"}";
    }

    /**HSY001004 找回密码*/
    public String updateHsyUserForSetPassword(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppAuUser appAuUser=null;
        try{
            appAuUser=gson.fromJson(dataParam, AppAuUser.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appAuUser.getCellphone()!=null&&!appAuUser.getCellphone().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"手机号");
        if(!(appAuUser.getPassword()!=null&&!appAuUser.getPassword().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"密码");
        if (!ValidateUtils.isMobile(appAuUser.getCellphone()))
            throw new ApiHandleException(ResultCode.CELLPHONE_NOT_CORRECT_FORMAT);
        if(!(appAuUser.getCode()!=null&&!appAuUser.getCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"验证码");

        /**数据验证*/
        List<AppAuUser> list = hsyUserDao.findAppAuUserByParam(appAuUser);
        if (!(list != null && list.size() != 0))
            throw new ApiHandleException(ResultCode.CEELLPHONE_HAS_NOT_BEEN_REGISTERED);

        AppAuVerification appAuVerification=new AppAuVerification();
        appAuVerification.setCellphone(appAuUser.getCellphone());
        appAuVerification.setTimeout(new Date());
        appAuVerification.setType(VerificationCodeType.ALT_PASSWORD.verificationCodeKey);
        List<AppAuVerification> vlist=hsyVerificationDao.findVCodeWithinTime(appAuVerification);
        if(vlist==null||(vlist!=null&&vlist.size()==0))
            throw new ApiHandleException(ResultCode.VERIFICATIONCODE_NO_EFFICACY);
        if(!vlist.get(0).getCode().equals(appAuUser.getCode()))
            throw new ApiHandleException(ResultCode.VERIFICATIONCODE_ERROR);

        /**用户修改*/
        Date date=new Date();
        appAuUser.setUpdateTime(date);
        hsyUserDao.update(appAuUser);
        return "";
    }

    /**HSY001016 保存设备相关信息并返回token*/
    public String insertTokenDeviceClientInfoAndReturnKey(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppAuToken appAuToken=null;
        try{
            appAuToken=gson.fromJson(dataParam, AppAuToken.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appAuToken.getDeviceid()!=null&&!appAuToken.getDeviceid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"设备号");
//        if(!(appAuToken.getClientid()!=null&&!appAuToken.getClientid().equals("")))
//            throw new ApiHandleException(ResultCode.PARAM_LACK,"推送号");
        if(!(appAuToken.getDeviceName()!=null&&!appAuToken.getDeviceName().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"设备名");
//        if(!(appAuDevice.getAppType()!=null&&!appAuDevice.getAppType().equals("")))
//            throw new ApiHandleException(ResultCode.PARAM_LACK,"app类型");
        if(!(appAuToken.getOsVersion()!=null&&!appAuToken.getOsVersion().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"系统版本号");
        if(!(appAuToken.getAppCode()!=null&&!appAuToken.getAppCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"app编号");
        if(!(appAuToken.getAppVersion()!=null&&!appAuToken.getAppVersion().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"app版本号");

        appAuToken.setAppType(appParam.getAppType());
        Date date=new Date();
        appAuToken.setCreateTime(date);
        appAuToken.setUpdateTime(date);
        hsyUserDao.insertAppAuToken(appAuToken);
        appAuToken.setAccessToken(ShaUtil.shaEncode(appAuToken.getId()+AppConstant.SHA_KEY).substring(0, 32));
        appAuToken.setEncryptKey(RandomStringUtils.randomAlphanumeric(32));
        hsyUserDao.updateAppAuToken(appAuToken);
        return "{\"accessToken\":\""+appAuToken.getAccessToken()+"\",\"encryptKey\":\""+appAuToken.getEncryptKey()+"\"}";
    }

    /**HSY001017 更新clientid*/
    public String updateClientID(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppAuToken appAuToken=null;
        try{
            appAuToken=gson.fromJson(dataParam, AppAuToken.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appAuToken.getClientid()!=null&&!appAuToken.getClientid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"推送号");
        if(!(appParam.getAccessToken()!=null&&!appParam.getAccessToken().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"令牌（公参）");

        List<AppAuToken> tokenList=hsyUserDao.findAppAuTokenByAccessToken(appParam.getAccessToken());
        if (tokenList != null && tokenList.size() != 0)
        {
            AppAuToken appAuTokenUpdate=tokenList.get(0);
            appAuTokenUpdate.setClientid(appAuToken.getClientid());
            appAuTokenUpdate.setUpdateTime(new Date());
            hsyUserDao.updateAppAuToken(appAuTokenUpdate);
        }
        else
            throw new ApiHandleException(ResultCode.ACCESSTOKEN_NOT_FOUND);
        return "";
    }

    /**HSY001018 登出*/
    public String logout(String dataParam, AppParam appParam)throws ApiHandleException{
        if(!(appParam.getAccessToken()!=null&&!appParam.getAccessToken().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"令牌（公参）");

        List<AppAuToken> tokenList=hsyUserDao.findAppAuTokenByAccessToken(appParam.getAccessToken());
        if (tokenList != null && tokenList.size() != 0)
        {
            hsyUserDao.updateAppAuUserTokenStatusByTid(tokenList.get(0).getId());
        }
        else
            throw new ApiHandleException(ResultCode.ACCESSTOKEN_NOT_FOUND);
        return "";
    }

    /**HSY001019 新增店员*/
    public String inserHsyUserViaCorporation(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppAuUser appAuUser=null;
        try{
            appAuUser=gson.fromJson(dataParam, AppAuUser.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appAuUser.getRole()!=null&&!appAuUser.getRole().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"角色类型");
        else if(!(appAuUser.getRole()!=null&&(appAuUser.getRole()==AppConstant.ROLE_MANAGER||appAuUser.getRole()==AppConstant.ROLE_EMPLOYEE)))
            throw new ApiHandleException(ResultCode.ROLE_TYPE_NOT_EXSIT);
        if(!(appAuUser.getCellphone()!=null&&!appAuUser.getCellphone().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"手机号");
        if (!ValidateUtils.isMobile(appAuUser.getCellphone()))
            throw new ApiHandleException(ResultCode.CELLPHONE_NOT_CORRECT_FORMAT);
        if(!(appAuUser.getRealname()!=null&&!appAuUser.getRealname().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"姓名");
        if(!(appAuUser.getParentID()!=null&&!appAuUser.getParentID().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"登录用户ID");

        /**数据验证*/
        List<AppAuUser> list = hsyUserDao.findAppAuUserByParam(appAuUser);
        if (list != null && list.size() != 0)
            throw new ApiHandleException(ResultCode.CELLPHONE_HAS_BEEN_REGISTERED);

        /**用户 中间表 保存*/
        Date date=new Date();
        appAuUser.setStatus(AppConstant.USER_STATUS_NORMAL);
        appAuUser.setCreateTime(date);
        appAuUser.setUpdateTime(date);
        String password=(int)((Math.random()*9+1)*10000000)+"";
        SmsTemplate messageTemplate = messageTemplateDao.getTemplateByType(AppConstant.SEND_PASSWORD_NOTICE_TYPE_ID);
        String template="";
        if(messageTemplate!=null&&messageTemplate.getMessageTemplate()!=null&&!messageTemplate.getMessageTemplate().trim().equals(""))
            template=messageTemplate.getMessageTemplate();
        else
            template=AppConstant.SEND_PASSWORD_MESSAGE;
        /**发送密码*/
        String sn="";
        String content=template.replace("${password}",password).replace("+", "%2B").replace("%", "%25");
        try {
            sn = smsSendMessageService.sendMessage(appAuUser.getCellphone(), content);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiHandleException(ResultCode.PASSWORD_SEND_FAIL);
        }
        SendMessageRecord sendRecord = new SendMessageRecord();
        sendRecord.setUserType(1);
        sendRecord.setMobile(appAuUser.getCellphone());
        sendRecord.setContent(content);
        sendRecord.setMessageTemplateId(messageTemplate.getId());
        sendRecord.setSn(sn);
        sendRecord.setStatus(1);
        sendMessageRecordDao.insert(sendRecord);

        appAuUser.setPassword(password);
        appAuUser.setAuStep("0");
        hsyUserDao.insert(appAuUser);

        AppAuUser appAuUserUp=new AppAuUser();
        appAuUserUp.setId(appAuUser.getId());
        appAuUserUp.setGlobalID(GlobalID.GetGlobalID(EnumGlobalIDType.MERCHANT, EnumGlobalIDPro.MAX,appAuUser.getId().toString()));
        if(appAuUser.getSid()!=null&&!appAuUser.getSid().equals("")) {
            AppBizShopUserRole appBizShopUserRole = new AppBizShopUserRole();
            appBizShopUserRole.setSid(appAuUser.getSid());
            appBizShopUserRole.setUid(appAuUser.getId());
            appBizShopUserRole.setRole(appAuUser.getRole());
            appBizShopUserRole.setStatus(AppConstant.ROLE_STATUS_NORMAL);

            AppBizShopUserRole absur=new AppBizShopUserRole();
            absur.setSid(appAuUser.getSid());
            absur.setUid(appAuUser.getParentID());
            List<AppBizShopUserRole> surList=hsyShopDao.findAppBizShopUserRoleBySidAndUid(absur);
            if(surList!=null&&surList.size()!=0)
                appBizShopUserRole.setType(surList.get(0).getType());
            else
                appBizShopUserRole.setType(AppConstant.ROLE_TYPE_PRIMARY);
            hsyShopDao.insertAppBizShopUserRole(appBizShopUserRole);
        }else{
            appAuUserUp.setRoleTemp(appAuUser.getRole());
        }
        hsyUserDao.updateByID(appAuUserUp);
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
        Map map=new HashMap();
        map.put("appAuUser",appAuUser);
        return gson.toJson(map);
    }

    /**HSY001021 查找店员*/
    public String findHsyUserViaCorporation(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppAuUser appAuUser=null;
        try{
            appAuUser=gson.fromJson(dataParam, AppAuUser.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }
        /**参数验证*/
        if(!(appAuUser.getId()!=null&&!appAuUser.getId().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"店员ID");
        if(!(appAuUser.getSid()!=null&&!appAuUser.getSid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"登录用户主店ID");

        List<AppAuUser> list=hsyUserDao.findAppAuUserByIDAndParentSID(appAuUser);
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
        Map map=new HashMap();
        if(list!=null&&list.size()!=0)
            map.put("appAuUser",list.get(0));
        else
            map.put("appAuUser",null);
        return gson.toJson(map);
    }

    /**HSY001022 查找店员列表*/
    public String findHsyUserListViaCorporation(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppAuUser appAuUser=null;
        try{
            appAuUser=gson.fromJson(dataParam, AppAuUser.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appAuUser.getParentID()!=null&&!appAuUser.getParentID().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"当前登录用户ID");
        if(!(appAuUser.getSid()!=null&&!appAuUser.getSid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"登录用户主店ID");

        List<AppAuUser> list=hsyUserDao.findAppAuUserListByParentID(appAuUser);
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
        Map map=new HashMap();
        if(list!=null&&list.size()!=0)
            map.put("appAuUserList",list);
        else
            map.put("appAuUserList",null);
        return gson.toJson(map);
    }

    /**HSY001023 更新店员*/
    public String updateHsyUserViaCorporation(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppAuUser appAuUser=null;
        try{
            appAuUser=gson.fromJson(dataParam, AppAuUser.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appAuUser.getRole()!=null&&!appAuUser.getRole().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"角色类型");
        else if(!(appAuUser.getRole()!=null&&(appAuUser.getRole()==2||appAuUser.getRole()==3)))
            throw new ApiHandleException(ResultCode.ROLE_TYPE_NOT_EXSIT);
        if(!(appAuUser.getCellphone()!=null&&!appAuUser.getCellphone().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"手机号");
        if (!ValidateUtils.isMobile(appAuUser.getCellphone()))
            throw new ApiHandleException(ResultCode.CELLPHONE_NOT_CORRECT_FORMAT);
        if(!(appAuUser.getRealname()!=null&&!appAuUser.getRealname().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"姓名");
        if(!(appAuUser.getParentID()!=null&&!appAuUser.getParentID().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"登录用户ID");
        if(!(appAuUser.getId()!=null&&!appAuUser.getId().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"店员ID");
        if(!(appAuUser.getOriginCellphone()!=null&&!appAuUser.getOriginCellphone().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"更改前手机号");

        /**数据验证*/
        List<AppAuUser> list = hsyUserDao.findAppAuUserByParam(appAuUser);
        if (list != null && list.size() != 0 && !(list.get(0).getCellphone().equals(appAuUser.getOriginCellphone())))
            throw new ApiHandleException(ResultCode.CELLPHONE_HAS_BEEN_REGISTERED);

        hsyUserDao.deleteAppBizShopUserRole(appAuUser.getId());
        Date date=new Date();
        if(appAuUser.getSid()!=null&&!appAuUser.getSid().equals("")) {
            AppBizShopUserRole appBizShopUserRole = new AppBizShopUserRole();
            appBizShopUserRole.setSid(appAuUser.getSid());
            appBizShopUserRole.setUid(appAuUser.getId());
            appBizShopUserRole.setRole(appAuUser.getRole());
            appBizShopUserRole.setStatus(AppConstant.ROLE_STATUS_NORMAL);

            AppBizShopUserRole absur=new AppBizShopUserRole();
            absur.setSid(appAuUser.getSid());
            absur.setUid(appAuUser.getParentID());
            List<AppBizShopUserRole> surList=hsyShopDao.findAppBizShopUserRoleBySidAndUid(absur);
            if(surList!=null&&surList.size()!=0)
                appBizShopUserRole.setType(surList.get(0).getType());
            else
                appBizShopUserRole.setType(AppConstant.ROLE_TYPE_PRIMARY);
            hsyShopDao.insertAppBizShopUserRole(appBizShopUserRole);
        }else{
            appAuUser.setRoleTemp(appAuUser.getRole());
        }
        appAuUser.setUpdateTime(date);
        hsyUserDao.updateByID(appAuUser);
        return "";
    }

    /**HSY001024 分配店铺*/
    public String updateHsyShopUserViaCorporation(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppAuUser appAuUser=null;
        try{
            appAuUser=gson.fromJson(dataParam, AppAuUser.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }
        /**参数验证*/
        if(!(appAuUser.getId()!=null&&!appAuUser.getId().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"店员ID");
        if(!(appAuUser.getSid()!=null&&!appAuUser.getSid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"店铺ID");
        if(!(appAuUser.getParentID()!=null&&!appAuUser.getParentID().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"登录用户ID");

        List<AppAuUser> originList=hsyUserDao.findAppAuUserWithRoleByID(appAuUser);
        if(originList!=null&&originList.size()!=0)
        {
            if(originList.get(0).getRole()!=null)
                appAuUser.setRole(originList.get(0).getRole());
            else
            {
                if(originList.get(0).getRoleTemp()!=null)
                    appAuUser.setRole(originList.get(0).getRoleTemp());
                else
                    appAuUser.setRole(AppConstant.ROLE_EMPLOYEE);
            }
        }
        else
            appAuUser.setRole(AppConstant.ROLE_EMPLOYEE);

        hsyUserDao.deleteAppBizShopUserRole(appAuUser.getId());
        AppBizShopUserRole appBizShopUserRole = new AppBizShopUserRole();
        appBizShopUserRole.setSid(appAuUser.getSid());
        appBizShopUserRole.setUid(appAuUser.getId());
        appBizShopUserRole.setRole(appAuUser.getRole());
        appBizShopUserRole.setStatus(AppConstant.ROLE_STATUS_NORMAL);

        AppBizShopUserRole absur=new AppBizShopUserRole();
        absur.setSid(appAuUser.getSid());
        absur.setUid(appAuUser.getParentID());
        List<AppBizShopUserRole> surList=hsyShopDao.findAppBizShopUserRoleBySidAndUid(absur);
        if(surList!=null&&surList.size()!=0)
            appBizShopUserRole.setType(surList.get(0).getType());
        else
            appBizShopUserRole.setType(AppConstant.ROLE_TYPE_PRIMARY);
        hsyShopDao.insertAppBizShopUserRole(appBizShopUserRole);
        return "";
    }

    /**HSY001025 启用禁用店员*/
    public String updateHsyUserStatusViaCorporation(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppAuUser appAuUser=null;
        try{
            appAuUser=gson.fromJson(dataParam, AppAuUser.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }
        /**参数验证*/
        if(!(appAuUser.getId()!=null&&!appAuUser.getId().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"店员ID");
        if(!(appAuUser.getStatus()!=null&&!appAuUser.getStatus().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"店员状态");
        else if(!(appAuUser.getStatus()!=null&&(appAuUser.getStatus()==AppConstant.USER_STATUS_NORMAL||appAuUser.getStatus()==AppConstant.USER_STATUS_FORBID)))
            throw new ApiHandleException(ResultCode.ROLE_STATUS_NOT_EXSIT);

        Date date=new Date();
        appAuUser.setUpdateTime(date);
        hsyUserDao.updateByID(appAuUser);
        return "";
    }

    /**HSY001028 查找登录用户的信息*/
    public String findLoginInfo(String dataParam,AppParam appParam)throws ApiHandleException {
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppAuUser appAuUser=null;
        try{
            appAuUser=gson.fromJson(dataParam, AppAuUser.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appAuUser.getId()!=null&&!appAuUser.getId().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"查询用户ID");

        /**查询用户*/
        List<AppAuUser> list = hsyUserDao.findAppAuUserByID(appAuUser.getId());
        if(!(list!=null&&list.size()!=0))
            throw new ApiHandleException(ResultCode.USER_CAN_NOT_BE_FOUND);

        AppAuUser appAuUserFind=list.get(0);
        AppBizShop appBizShop=new AppBizShop();
        appBizShop.setUid(appAuUserFind.getId());
        if(appAuUserFind.getParentID()==null||(appAuUserFind.getParentID()!=null&&appAuUserFind.getParentID()!=0L))
            appBizShop.setType(AppConstant.ROLE_TYPE_PRIMARY);
        List<AppBizShop> shopList=hsyShopDao.findPrimaryAppBizShopByUserID(appBizShop);
        if(shopList!=null&&shopList.size()!=0)
            appBizShop=shopList.get(0);
        if(appBizShop.getCheckErrorInfo()==null)
            appBizShop.setCheckErrorInfo("");
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

        Map map = new HashMap();
        if(appAuUserFind.getAccountID()!=null) {
            final Optional<Account> accountOptional = this.accountService.getById(appAuUserFind.getAccountID().longValue());
            if (accountOptional.isPresent()) {
                final Account account = accountOptional.get();
                map.put("totalAmount", account.getTotalAmount().toPlainString());
                map.put("available", account.getAvailable().toPlainString());
                map.put("dueSettleAmount", account.getDueSettleAmount().toPlainString());
                map.put("frozenAmount", account.getFrozenAmount().toPlainString());
            } else {
                map.put("totalAmount", "");
                map.put("available", "");
                map.put("dueSettleAmount", "");
                map.put("frozenAmount", "");
            }
        }else{
            map.put("totalAmount", "");
            map.put("available", "");
            map.put("dueSettleAmount", "");
            map.put("frozenAmount", "");
        }
        map.put("appAuUser",appAuUserFind);
        map.put("appBizShop",appBizShop);
        return gson.toJson(map);
    }

    /**HSY001029 查找登录用户的细节*/
    public String findLoginInfoShort(String dataParam,AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppAuUser appAuUser=null;
        try{
            appAuUser=gson.fromJson(dataParam, AppAuUser.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appAuUser.getId()!=null&&!appAuUser.getId().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"查询用户ID");

        /**查询用户*/
        List<AppAuUser> list = hsyUserDao.findAppAuUserByID(appAuUser.getId());
        if(!(list!=null&&list.size()!=0))
            throw new ApiHandleException(ResultCode.USER_CAN_NOT_BE_FOUND);

        AppAuUser appAuUserFind=list.get(0);
        AppBizShop appBizShop=new AppBizShop();
        appBizShop.setUid(appAuUserFind.getId());
        if(appAuUserFind.getParentID()==null||(appAuUserFind.getParentID()!=null&&appAuUserFind.getParentID()!=0L))
            appBizShop.setType(AppConstant.ROLE_TYPE_PRIMARY);
        List<AppBizShop> shopList=hsyShopDao.findPrimaryAppBizShopByUserID(appBizShop);
        if(shopList!=null&&shopList.size()!=0)
            appBizShop=shopList.get(0);
        if(appBizShop.getCheckErrorInfo()==null)
            appBizShop.setCheckErrorInfo("");
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
        Map map = new HashMap();
        if(appAuUserFind.getAccountID()!=null) {
            final Optional<Account> accountOptional = this.accountService.getById(appAuUserFind.getAccountID().longValue());
            if (accountOptional.isPresent()) {
                final Account account = accountOptional.get();
                map.put("totalAmount", account.getTotalAmount().toPlainString());
                map.put("available", account.getAvailable().toPlainString());
                map.put("dueSettleAmount", account.getDueSettleAmount().toPlainString());
                map.put("frozenAmount", account.getFrozenAmount().toPlainString());
            } else {
                map.put("totalAmount", "");
                map.put("available", "");
                map.put("dueSettleAmount", "");
                map.put("frozenAmount", "");
            }
        }else{
            map.put("totalAmount", "");
            map.put("available", "");
            map.put("dueSettleAmount", "");
            map.put("frozenAmount", "");
        }
        AppAuUser user=new AppAuUser();
        user.setAccountID(appAuUserFind.getAccountID());
        map.put("appAuUser",user);
        AppBizShop shop=new AppBizShop();
        shop.setStatus(appBizShop.getStatus());
        map.put("appBizShop",shop);
        return gson.toJson(map);
    }

}
