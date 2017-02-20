package com.jkm.hsy.user.service.impl;

import com.google.gson.*;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.dao.HsyAppVersionDao;
import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.entity.AppVersion;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.jkm.hsy.user.service.HsyAppVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * Created by Allen on 2017/2/15.
 */
@Slf4j
@Service(value = "hsyAppVersionService")
public class HsyAppVersionServiceImpl implements HsyAppVersionService {

    @Autowired
    private HsyAppVersionDao hsyAppVersionDao;

    /**HSY001026 获得App版本（通用）*/
    public String getAppVersion(String dataParam,AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();

        /**参数转化*/
        AppVersion appVersion=null;
        try{
            appVersion=gson.fromJson(dataParam, AppVersion.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appVersion.getVersionCode()!=null&&!appVersion.getVersionCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"当前版本号");
        appVersion.setAppType(appParam.getAppType());

        List<AppVersion> versionList=hsyAppVersionDao.getAppVersion(appVersion);
        if(versionList!=null&&versionList.size()!=0)
        {
            appVersion=versionList.get(0);
            for(AppVersion a:versionList)
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

    /**HSY001026 获得App版本（android）*/
    public String getAppVersionAndroid(String dataParam,AppParam appParam){
        AppVersion appVersion=new AppVersion();
        appVersion.setAppType(appParam.getAppType());
        List<AppVersion> versionList=hsyAppVersionDao.getAppVersionAndroid(appVersion);
        if(versionList!=null&&versionList.size()!=0)
        {
            appVersion=versionList.get(0);
        }
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
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

    /**HSY001037 获得ios审核版本*/
    public String findVersionDetailByVersionCode(String dataParam,AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();

        /**参数转化*/
        AppVersion appVersion=null;
        try{
            appVersion=gson.fromJson(dataParam, AppVersion.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appVersion.getVersionCode()!=null&&!appVersion.getVersionCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"当前版本号");
        appVersion.setAppType(appParam.getAppType());

        List<AppVersion> versionList=hsyAppVersionDao.findVersionDetailByVersionCode(appVersion);
        gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
                return new JsonPrimitive(date.getTime());
            }
        }).registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();
        if(versionList!=null&&versionList.size()!=0)
            return gson.toJson(versionList.get(0));
        else
            return gson.toJson(appVersion);
    }
}
