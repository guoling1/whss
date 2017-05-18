package com.jkm.hsy.user.service.impl;

import com.google.gson.*;
import com.jkm.base.common.util.DateUtil;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.constant.VerificationCodeType;
import com.jkm.hsy.user.dao.HsyMembershipDao;
import com.jkm.hsy.user.dao.HsyVerificationDao;
import com.jkm.hsy.user.entity.*;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.jkm.hsy.user.service.HsyMembershipService;
import com.jkm.hsy.user.util.AppDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Allen on 2017/5/12.
 */
@Service("hsyMembershipService")
public class HsyMembershipServiceImpl implements HsyMembershipService {
    @Autowired
    private HsyMembershipDao hsyMembershipDao;
    @Autowired
    private HsyVerificationDao hsyVerificationDao;

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
        List<AppPolicyMembershipCardShop> appPolicyMembershipCardShopList=new ArrayList<AppPolicyMembershipCardShop>();
        String[] sidStrs=appPolicyMembershipCard.getSids().split(",");
        for(String sidStr:sidStrs)
        {
            Long sid=Long.parseLong(sidStr);
            AppPolicyMembershipCardShop appPolicyMembershipCardShop=new AppPolicyMembershipCardShop();
            appPolicyMembershipCardShop.setSid(sid);
            appPolicyMembershipCardShop.setMcid(appPolicyMembershipCard.getId());
            appPolicyMembershipCardShopList.add(appPolicyMembershipCardShop);
        }
        hsyMembershipDao.insertMembershipCardShopBatch(appPolicyMembershipCardShopList);
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

    public AppPolicyConsumer findConsumerByOpenID(String openID){
        AppPolicyConsumer appPolicyConsumer=new AppPolicyConsumer();
        appPolicyConsumer.setOpenID(openID);
        List<AppPolicyConsumer> appPolicyConsumerList=hsyMembershipDao.findConsumerByParam(appPolicyConsumer);
        if(appPolicyConsumerList!=null&&appPolicyConsumerList.size()!=0)
            return appPolicyConsumerList.get(0);
        else
            return null;
    }

    public AppPolicyConsumer findConsumerByUserID(String userID){
        AppPolicyConsumer appPolicyConsumer=new AppPolicyConsumer();
        appPolicyConsumer.setUserID(userID);
        List<AppPolicyConsumer> appPolicyConsumerList=hsyMembershipDao.findConsumerByParam(appPolicyConsumer);
        if(appPolicyConsumerList!=null&&appPolicyConsumerList.size()!=0)
            return appPolicyConsumerList.get(0);
        else
            return null;
    }

    public AppPolicyConsumer findConsumerByCellphone(String consumerCellphone){
        AppPolicyConsumer appPolicyConsumer=new AppPolicyConsumer();
        appPolicyConsumer.setConsumerCellphone(consumerCellphone);
        List<AppPolicyConsumer> appPolicyConsumerList=hsyMembershipDao.findConsumerByParam(appPolicyConsumer);
        if(appPolicyConsumerList!=null&&appPolicyConsumerList.size()!=0)
            return appPolicyConsumerList.get(0);
        else
            return null;
    }

    public AppPolicyMember findMemberByCIDAndUID(Long cid,Long uid){
        AppPolicyMember appPolicyMember=new AppPolicyMember();
        appPolicyMember.setUid(uid);
        appPolicyMember.setCid(cid);
        List<AppPolicyMember> list=hsyMembershipDao.findMemberCascadeByParam(appPolicyMember);
        if(list!=null&&list.size()!=0)
            return list.get(0);
        else
            return null;
    }

    public AppPolicyMember findMemberByCIDAndMCID(Long cid, Long mcid){
        AppPolicyMember appPolicyMember=new AppPolicyMember();
        appPolicyMember.setMcid(mcid);
        appPolicyMember.setCid(cid);
        List<AppPolicyMember> list=hsyMembershipDao.findMemberByParam(appPolicyMember);
        if(list!=null&&list.size()!=0)
            return list.get(0);
        else
            return null;
    }

    public List findMemberCardByUID(Long uid){
        AppPolicyMembershipCard appPolicyMembershipCard=new AppPolicyMembershipCard();
        appPolicyMembershipCard.setUid(uid);
        return hsyMembershipDao.findMemberCardByParam(appPolicyMembershipCard);
    }

    public AppPolicyConsumer insertOrUpdateConsumer(AppPolicyConsumer appPolicyConsumer){
        Date date=new Date();
        if(appPolicyConsumer.getId()!=null){
            appPolicyConsumer.setUpdateTime(date);
            hsyMembershipDao.updateConsumer(appPolicyConsumer);
        }else{
            appPolicyConsumer.setCreateTime(date);
            appPolicyConsumer.setUpdateTime(date);
            hsyMembershipDao.insertConsumer(appPolicyConsumer);
        }
        return appPolicyConsumer;
    }

    public AppPolicyMember saveMember(Long cid,Long mcid){
        Date date=new Date();
        AppPolicyMember appPolicyMember=new AppPolicyMember();
        appPolicyMember.setCid(cid);
        appPolicyMember.setMcid(mcid);
        appPolicyMember.setStatus(1);
        appPolicyMember.setCreateTime(date);
        appPolicyMember.setUpdateTime(date);
        hsyMembershipDao.insertMember(appPolicyMember);
        DecimalFormat a=new DecimalFormat("00000000");
        String memberCardNO= AppDateUtil.formatDate(date,"yy")+a.format(appPolicyMember.getId());
        AppPolicyMember appPolicyMemberUp=new AppPolicyMember();
        appPolicyMemberUp.setId(appPolicyMember.getId());
        appPolicyMemberUp.setMemberCardNO(memberCardNO);
        hsyMembershipDao.updateMember(appPolicyMemberUp);
        return appPolicyMember;
    }

    public void insertVcode(String sn,String code,String cellphone,Integer type){
        AppAuVerification appAuVerification=new AppAuVerification();
        appAuVerification.setCellphone(cellphone);
        appAuVerification.setType(type);
        appAuVerification.setSn(sn);
        Date dateN=new Date();
        appAuVerification.setTimeout(AppDateUtil.changeDate(dateN, Calendar.MINUTE,5));
        appAuVerification.setCreateTime(dateN);
        appAuVerification.setUpdateTime(dateN);
        appAuVerification.setCode(code);
        hsyVerificationDao.insert(appAuVerification);
    }

    public AppAuVerification findRightVcode(String cellphone){
        AppAuVerification appAuVerification=new AppAuVerification();
        appAuVerification.setCellphone(cellphone);
        appAuVerification.setTimeout(new Date());
        appAuVerification.setType(VerificationCodeType.MEMBER_REGISTER.verificationCodeKey);
        List<AppAuVerification> vlist=hsyVerificationDao.findVCodeWithinTime(appAuVerification);
        if(vlist!=null&&vlist.size()!=0)
            return vlist.get(0);
        else
            return null;
    }

    public AppPolicyMember findMemberInfoByID(Long mid){
        List<AppPolicyMember> list=hsyMembershipDao.findMemberInfoByID(mid);
        if(list!=null&&list.size()!=0)
            return list.get(0);
        else
            return null;
    }

}
