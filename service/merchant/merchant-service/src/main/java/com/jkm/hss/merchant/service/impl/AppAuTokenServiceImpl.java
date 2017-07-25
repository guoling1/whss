package com.jkm.hss.merchant.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jkm.hss.merchant.constant.AppConstant;
import com.jkm.hss.merchant.dao.AppAuTokenDao;
import com.jkm.hss.merchant.entity.AppAuToken;
import com.jkm.hss.merchant.exception.ApiHandleException;
import com.jkm.hss.merchant.exception.ResultCode;
import com.jkm.hss.merchant.helper.AppParam;
import com.jkm.hss.merchant.helper.ShaUtil;
import com.jkm.hss.merchant.service.AppAuTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by xingliujie on 2017/7/24.
 */
@Slf4j
@Service("appAuTokenService")
public class AppAuTokenServiceImpl implements AppAuTokenService {
    @Autowired
    private AppAuTokenDao appAuTokenDao;
    /**
     * HSS001001 保存设备相关信息并返回token
     * @param dataParam
     * @param appParam
     * @return
     * @throws ApiHandleException
     */
    public String insertTokenDeviceClientInfoAndReturnKey(String dataParam, AppParam appParam)throws ApiHandleException {
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        AppAuToken appAuToken=null;
        try{
            appAuToken=gson.fromJson(dataParam, AppAuToken.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }
        if(!(appAuToken.getDeviceid()!=null&&!appAuToken.getDeviceid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"设备号");
        if(!(appAuToken.getDeviceName()!=null&&!appAuToken.getDeviceName().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"设备名");
        if(!(appAuToken.getOsVersion()!=null&&!appAuToken.getOsVersion().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"系统版本号");
        if(!(appAuToken.getAppVersion()!=null&&!appAuToken.getAppVersion().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"app版本号");
        appAuToken.setAppType(appParam.getAppType());
        Date date=new Date();
        appAuToken.setAppCode("hss");
        appAuToken.setIsAvoidingTone(0);
        appAuToken.setCreateTime(date);
        appAuToken.setUpdateTime(date);
        appAuTokenDao.insert(appAuToken);
        appAuToken.setAccessToken(ShaUtil.shaEncode(appAuToken.getId()+AppConstant.SHA_KEY).substring(0, 32));
        appAuToken.setEncryptKey(RandomStringUtils.randomAlphanumeric(32));
        appAuTokenDao.update(appAuToken);
        return "{\"accessToken\":\""+appAuToken.getAccessToken()+"\",\"encryptKey\":\""+appAuToken.getEncryptKey()+"\"}";
    }

    /**
     * HSS001002 更新clientid
     * @param dataParam
     * @param appParam
     * @return
     * @throws ApiHandleException
     */
    public String updateClientID(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        AppAuToken appAuToken=null;
        try{
            appAuToken=gson.fromJson(dataParam, AppAuToken.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }
        if(!(appAuToken.getClientid()!=null&&!appAuToken.getClientid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"推送号");
        if(!(appParam.getAccessToken()!=null&&!appParam.getAccessToken().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"令牌（公参）");

        List<AppAuToken> tokenList=appAuTokenDao.findAppAuTokenByAccessToken(appParam.getAccessToken());
        if (tokenList != null && tokenList.size() != 0)
        {
            AppAuToken appAuTokenUpdate=tokenList.get(0);
            appAuTokenUpdate.setClientid(appAuToken.getClientid());
            appAuTokenUpdate.setUpdateTime(new Date());
            appAuTokenDao.updateAppAuToken(appAuTokenUpdate);
        }
        else
            throw new ApiHandleException(ResultCode.ACCESSTOKEN_NOT_FOUND);
        return "";
    }
}
