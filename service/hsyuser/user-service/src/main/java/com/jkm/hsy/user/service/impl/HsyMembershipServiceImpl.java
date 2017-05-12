package com.jkm.hsy.user.service.impl;

import com.google.gson.*;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.dao.HsyMembershipDao;
import com.jkm.hsy.user.entity.*;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.jkm.hsy.user.service.HsyMembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Allen on 2017/5/12.
 */
@Service("hsyMembershipService")
public class HsyMembershipServiceImpl implements HsyMembershipService {
    @Autowired
    private HsyMembershipDao hsyMembershipDao;

    /**HSY001046 创建会员卡*/
    public String insertMemshipCard(String dataParam, AppParam appParam)throws ApiHandleException {
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppPolicyMembershipCard appPolicyMembershipCard=null;
        try{
            appPolicyMembershipCard=gson.fromJson(dataParam, AppPolicyMembershipCard.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appPolicyMembershipCard.getMembershipName()!=null&&!appPolicyMembershipCard.getMembershipName().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"会员卡名称");
        if(!(appPolicyMembershipCard.getMembershipShopName()!=null&&!appPolicyMembershipCard.getMembershipShopName().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"显示店铺名");
        if(!(appPolicyMembershipCard.getUid()!=null&&!appPolicyMembershipCard.getUid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"法人ID");
        if(!(appPolicyMembershipCard.getSids()!=null&&!appPolicyMembershipCard.getSids().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"选择店铺相关ID");
        if(!(appPolicyMembershipCard.getDiscount()!=null&&!appPolicyMembershipCard.getDiscount().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"会员卡折扣");
        if(appPolicyMembershipCard.getDiscount().compareTo(BigDecimal.ZERO)==-1||appPolicyMembershipCard.getDiscount().compareTo(new BigDecimal(10))==1)
            throw new ApiHandleException(ResultCode.DISCOUNT_NOT_RIGHT);
        if(!(appPolicyMembershipCard.getIsDeposited()!=null&&!appPolicyMembershipCard.getIsDeposited().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"储值功能");
        if(appPolicyMembershipCard.getIsDeposited()==1)
            if(!(appPolicyMembershipCard.getDepositAmount()!=null&&!appPolicyMembershipCard.getDepositAmount().equals("")))
                throw new ApiHandleException(ResultCode.PARAM_LACK,"开卡储值金额");
        if(!(appPolicyMembershipCard.getIsPresentedViaActivate()!=null&&!appPolicyMembershipCard.getIsPresentedViaActivate().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"开卡送功能");
        if(appPolicyMembershipCard.getIsPresentedViaActivate()==1)
            if(!(appPolicyMembershipCard.getPresentAmount()!=null&&!appPolicyMembershipCard.getPresentAmount().equals("")))
                throw new ApiHandleException(ResultCode.PARAM_LACK,"开卡赠送金额");
        if(!(appPolicyMembershipCard.getIsPresentedViaRecharge()!=null&&!appPolicyMembershipCard.getIsPresentedViaRecharge().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"充值送功能");
        if(appPolicyMembershipCard.getIsPresentedViaRecharge()==1) {
            if (!(appPolicyMembershipCard.getRechargeLimitAmount() != null && !appPolicyMembershipCard.getRechargeLimitAmount().equals("")))
                throw new ApiHandleException(ResultCode.PARAM_LACK, "单笔充值限额");
            if (!(appPolicyMembershipCard.getRechargePresentAmount() != null && !appPolicyMembershipCard.getRechargePresentAmount().equals("")))
                throw new ApiHandleException(ResultCode.PARAM_LACK, "单笔充值赠送金额");
        }

        Date date=new Date();
        appPolicyMembershipCard.setStatus(1);
        appPolicyMembershipCard.setWeight(10);
        appPolicyMembershipCard.setCreateTime(date);
        appPolicyMembershipCard.setUpdateTime(date);
        hsyMembershipDao.insertMembershipCard(appPolicyMembershipCard);
        String[] sidStrs=appPolicyMembershipCard.getSids().split(",");
        for(String sidStr:sidStrs)
        {
            Long sid=Long.parseLong(sidStr);
            AppPolicyMembershipCardShop appPolicyMembershipCardShop=new AppPolicyMembershipCardShop();
            appPolicyMembershipCardShop.setSid(sid);
            appPolicyMembershipCardShop.setMcid(appPolicyMembershipCard.getId());
        }
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
        map.put("appPolicyMembershipCard",appPolicyMembershipCard);
        return gson.toJson(map);
    }
}
