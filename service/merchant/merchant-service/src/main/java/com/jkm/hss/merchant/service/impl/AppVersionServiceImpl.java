package com.jkm.hss.merchant.service.impl;

import com.google.gson.*;
import com.jkm.hss.merchant.constant.AppConstant;
import com.jkm.hss.merchant.dao.AppVersionDao;
import com.jkm.hss.merchant.entity.AppVersion;
import com.jkm.hss.merchant.entity.AppVersionCommon;
import com.jkm.hss.merchant.exception.ApiHandleException;
import com.jkm.hss.merchant.exception.ResultCode;
import com.jkm.hss.merchant.helper.AppParam;
import com.jkm.hss.merchant.service.AppVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * Created by xingliujie on 2017/7/25.
 */
@Slf4j
@Service("appVersionService")
public class AppVersionServiceImpl implements AppVersionService {
    @Autowired
    private AppVersionDao appVersionDao;

    /**
     * HSS001003 获得ios审核版本
     * @param dataParam
     * @param appParam
     * @return
     * @throws ApiHandleException
     */
    public String findVersionDetail(String dataParam,AppParam appParam)throws ApiHandleException {
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        AppVersionCommon appVersion=null;
        try{
            appVersion=gson.fromJson(dataParam, AppVersionCommon.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }
        if(!(appVersion.getVersionCode()!=null&&!appVersion.getVersionCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"当前版本号");
        appVersion.setAppType(appParam.getAppType());
        appVersion.setAppSort(appVersion.getAppSort());
        List<AppVersionCommon> versionList=appVersionDao.findVersionDetail(appVersion);
        gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
                return new JsonPrimitive(date.getTime());
            }
        }).registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();
        if(versionList!=null&&versionList.size()!=0)
            return gson.toJson(versionList.get(0));
        else
            return gson.toJson(appVersion);
    }

    /**
     * HSS001004 获得App版本
     * @param dataParam
     * @param appParam
     * @return
     * @throws ApiHandleException
     */
    public String getAppVersion(String dataParam,AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        AppVersionCommon appVersion=null;
        try{
            appVersion=gson.fromJson(dataParam, AppVersionCommon.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }
        if(!(appVersion.getVersionCode()!=null&&!appVersion.getVersionCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"当前版本号");
        appVersion.setAppType(appParam.getAppType());
        appVersion.setAppSort("inventoryHsy");

        List<AppVersionCommon> versionList=appVersionDao.getAppVersion(appVersion);
        if(versionList!=null&&versionList.size()!=0)
        {
            appVersion=versionList.get(0);
            for(AppVersionCommon a:versionList)
            {
                if(a.getIsUpgrade()==2)
                    appVersion.setIsUpgrade(2);
            }
        }
        else
        {
            appVersion=null;
        }
        gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
                return new JsonPrimitive(date.getTime());
            }
        }).registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();
        return gson.toJson(appVersion);
    }
}
