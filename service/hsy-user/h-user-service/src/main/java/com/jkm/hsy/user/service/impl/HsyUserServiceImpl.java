package com.jkm.hsy.user.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.base.sms.service.SmsSendMessageService;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.constant.VerificationCodeType;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.dao.HsyVerificationDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppAuVerification;
import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.jkm.hsy.user.service.HsyUserService;
import com.jkm.hsy.user.util.AppDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("hsyUserService")
public class HsyUserServiceImpl implements HsyUserService {
    @Autowired
    private HsyUserDao hsyUserDao;
    @Autowired
    private HsyVerificationDao hsyVerificationDao;
    @Autowired
    private SmsSendMessageService smsSendMessageService;

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

        /**用户保存*/
        try {
            Date date=new Date();
            appAuUser.setStatus(AppConstant.USER_STATUS_NO_CHECK);
            appAuUser.setCreateTime(date);
            appAuUser.setUpdateTime(date);
            hsyUserDao.insert(appAuUser);
        }catch(Exception e){
            throw new ApiHandleException(ResultCode.INSERT_ERROR);
        }
        return "{\"id\":"+appAuUser.getId()+"}";
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

        /**查询用户*/
        List<AppAuUser> list = hsyUserDao.findAppAuUserByParam(appAuUser);
        if (!(list != null && list.size() != 0))
            throw new ApiHandleException(ResultCode.CEELLPHONE_HAS_NOT_BEEN_REGISTERED);
        AppAuUser appAuUserFind=list.get(0);
        if(!appAuUserFind.getPassword().equals(appAuUser.getPassword()))
            throw new ApiHandleException(ResultCode.PASSWORD_NOT_CORRECT);
        if(AppConstant.USER_STATUS_NORMAL!=appAuUserFind.getStatus())
            throw new ApiHandleException(ResultCode.USER_NO_CEHCK);
        return "{\"id\":"+appAuUserFind.getId()+"}";
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
        appAuVerification.setCreateTime(AppDateUtil.changeDate(date, Calendar.MINUTE,-1));
        appAuVerification.setUpdateTime(date);
        Integer countFrequent=hsyVerificationDao.findVCodeCountWithinTime(appAuVerification);
        if(countFrequent>0)
            throw new ApiHandleException(ResultCode.VERIFICATIONCODE_FREQUENT);

        /**发送验证码*/
        String sn="";
        String code=(int)((Math.random()*9+1)*100000)+"";
        String content=AppConstant.REGISTER_VERIFICATION_MESSAGE.replace("${code}",code).replace("${type}",VerificationCodeType.getValue(appAuVerification.getType())).replace("+", "%2B").replace("%", "%25");
        try {
            sn = smsSendMessageService.sendMessage(appAuVerification.getCellphone(), content);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiHandleException(ResultCode.VERIFICATIONCODE_SEND_FAIL);
        }

        /**验证码保存*/
        appAuVerification.setSn(sn);
        appAuVerification.setTimeout(AppDateUtil.changeDate(date, Calendar.MINUTE,5));
        Date dateN=new Date();
        appAuVerification.setCreateTime(dateN);
        appAuVerification.setUpdateTime(dateN);
        appAuVerification.setCode(code);
        try {
            hsyVerificationDao.insert(appAuVerification);
        }catch(Exception e){
            throw new ApiHandleException(ResultCode.INSERT_ERROR);
        }
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
        try {
            Date date=new Date();
            appAuUser.setUpdateTime(date);
            hsyUserDao.update(appAuUser);
        }catch(Exception e){
            throw new ApiHandleException(ResultCode.INSERT_ERROR);
        }
        return "";
    }
}
