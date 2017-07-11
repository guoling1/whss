package com.jkm.hsy.user.service.impl;

import com.google.common.base.Optional;
import com.google.gson.*;
import com.jkm.hss.account.entity.MemberAccount;
import com.jkm.hss.account.sevice.MemberAccountService;
import com.jkm.hss.product.enums.EnumPaymentChannel;
import com.jkm.hsy.user.Enum.EnumPolicyType;
import com.jkm.hsy.user.constant.*;
import com.jkm.hsy.user.dao.HsyMembershipDao;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.dao.HsyVerificationDao;
import com.jkm.hsy.user.dao.UserCurrentChannelPolicyDao;
import com.jkm.hsy.user.entity.*;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.jkm.hsy.user.service.HsyMembershipService;
import com.jkm.hsy.user.util.AppAesUtil;
import com.jkm.hsy.user.util.AppDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URLEncoder;
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
    @Autowired
    private HsyUserDao hsyUserDao;
    @Autowired
    private UserCurrentChannelPolicyDao userCurrentChannelPolicyDao;
    @Autowired
    private MemberAccountService memberAccountService;

    /**HSY001047 创建会员卡*/
    public String insertMembershipCard(String dataParam, AppParam appParam)throws ApiHandleException {
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
        Integer count=hsyMembershipDao.findMembershipCardCountOfUserByUID(appPolicyMembershipCard.getUid());
        if(count>= AppPolicyConstant.CARD_LIMIT)
            throw new ApiHandleException(ResultCode.MEMBERSHIP_CARD_ABOVE_LIMIT);
        if(!(appPolicyMembershipCard.getSids()!=null&&!appPolicyMembershipCard.getSids().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"选择店铺相关ID");
        if(!(appPolicyMembershipCard.getDiscount()!=null&&!appPolicyMembershipCard.getDiscount().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"会员卡折扣");
        if(appPolicyMembershipCard.getDiscount().compareTo(BigDecimal.ZERO)==-1||appPolicyMembershipCard.getDiscount().compareTo(new BigDecimal(10))==1)
            throw new ApiHandleException(ResultCode.DISCOUNT_NOT_RIGHT);
        if(!(appPolicyMembershipCard.getIsDeposited()!=null&&!appPolicyMembershipCard.getIsDeposited().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"储值功能");
        if(appPolicyMembershipCard.getIsDeposited()==1) {
            if (!(appPolicyMembershipCard.getDepositAmount() != null && !appPolicyMembershipCard.getDepositAmount().equals("")))
                throw new ApiHandleException(ResultCode.PARAM_LACK, "开卡储值金额");
            if(appPolicyMembershipCard.getDepositAmount().compareTo(BigDecimal.ZERO)!=1)
                throw new ApiHandleException(ResultCode.DEPOSIT_AMOUNT_MUST_BE_ABOVE_ZERO);
        }
        if(!(appPolicyMembershipCard.getIsPresentedViaActivate()!=null&&!appPolicyMembershipCard.getIsPresentedViaActivate().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"开卡送功能");
        if(appPolicyMembershipCard.getIsPresentedViaActivate()==1)
            if(!(appPolicyMembershipCard.getPresentAmount()!=null&&!appPolicyMembershipCard.getPresentAmount().equals("")))
                throw new ApiHandleException(ResultCode.PARAM_LACK,"开卡赠送金额");
        if(!(appPolicyMembershipCard.getCanRecharge()!=null&&!appPolicyMembershipCard.getCanRecharge().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"自助充值功能");
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

    /**HSY001051 查询会员卡列表和统计值*/
    public String findMembershipCards(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppPolicyMembershipCard appPolicyMembershipCard=null;
        try{
            appPolicyMembershipCard=gson.fromJson(dataParam, AppPolicyMembershipCard.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appPolicyMembershipCard.getUid()!=null&&!appPolicyMembershipCard.getUid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"法人ID");
        List<AppPolicyMembershipCard> cardList=hsyMembershipDao.findMemberCardList(appPolicyMembershipCard);

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
        map.put("cardList",cardList);
        return gson.toJson(map);
    }

    /**HSY001052 返回开卡和储值二维码*/
    public String findMemberQr(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        Map map=null;
        BigDecimal id=null;
        try{
            map=gson.fromJson(dataParam, Map.class);
            id=new BigDecimal(map.get("id")+"");
        } catch(Exception e){
            e.printStackTrace();
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(id!=null&&!id.equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"用户ID");

        List<AppAuUser> userList=hsyUserDao.findAppAuUserByID(id.longValue());
        Long uid=null;
        if(userList.get(0).getParentID()!=null&&userList.get(0).getParentID()!=0)
            uid=userList.get(0).getParentID();
        else
            uid=userList.get(0).getId();

        String uidEncode=null;
        try {
            String uidAES=AppAesUtil.encryptCBC_NoPaddingToBase64String(uid+"", AppPolicyConstant.enc, AppPolicyConstant.secretKey, AppPolicyConstant.ivKey);
            uidEncode= URLEncoder.encode(uidAES,"utf-8");
        } catch (Exception e) {
            throw new ApiHandleException(ResultCode.ESCAPE_FAIL);
        }
        Map result=new HashMap();
        result.put("createQR",AppPolicyConstant.MEMBER_QR+OperateType.CREATE.key+"/"+uidEncode);
        result.put("rechargeQR",AppPolicyConstant.MEMBER_QR+OperateType.RECHARGE.key+"/"+uidEncode);
        return gson.toJson(result);
    }

    /**HSY001053 查询会员卡详细信息和统计值*/
    public String findMembershipCardsInfo(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().create();
        /**参数转化*/
        AppPolicyMembershipCard appPolicyMembershipCard=null;
        try{
            appPolicyMembershipCard=gson.fromJson(dataParam, AppPolicyMembershipCard.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appPolicyMembershipCard.getId()!=null&&!appPolicyMembershipCard.getId().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"ID");

        List<AppPolicyMembershipCard> list= hsyMembershipDao.findMemberCardByID(appPolicyMembershipCard.getId());
        Integer cardCount=hsyMembershipDao.findMemberCardCountByMCID(appPolicyMembershipCard.getId());
        Integer cardTotalCount=hsyMembershipDao.findMemberCardCascadeCountByUID(list.get(0).getUid(),null,null);
        List<AppBizShop> shopList=hsyMembershipDao.findSuitShopByMCID(appPolicyMembershipCard.getId());

        BigDecimal proportion = new BigDecimal(cardCount).divide(new BigDecimal(cardTotalCount),4,BigDecimal.ROUND_HALF_UP);
        BigDecimal proportionEx = proportion.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);

        Map result=new HashMap();
        result.put("appPolicyMembershipCard",list.get(0));
        result.put("shopList",shopList);
        result.put("cardCount",cardCount);
        result.put("cardTotalCount",cardTotalCount);
        result.put("proportion",proportionEx+"%");
        gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
                if(date==null)
                    return null;
                return new JsonPrimitive(date.getTime());
            }
        }).registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if(json.getAsJsonPrimitive()==null)
                    return null;
                return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();
        return gson.toJson(result);
    }

    /**HSY001054 停止(启用)开通会员卡*/
    public String updateMembershipCardsStatus(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppPolicyMembershipCard appPolicyMembershipCard=null;
        try{
            appPolicyMembershipCard=gson.fromJson(dataParam, AppPolicyMembershipCard.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appPolicyMembershipCard.getId()!=null&&!appPolicyMembershipCard.getId().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"ID");
        if(!(appPolicyMembershipCard.getStatus()!=null&&!appPolicyMembershipCard.getStatus().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"开通状态");
        if(!(appPolicyMembershipCard.getStatus()==CardStatus.USING.key||appPolicyMembershipCard.getStatus()==CardStatus.HALT_USING.key))
            throw new ApiHandleException(ResultCode.MEMBERSHIP_CARD_STATUS_NOT_EXSIT);
        hsyMembershipDao.updateMembershipCard(appPolicyMembershipCard);
        return "";
    }

    /**HSY001055 修改会员卡*/
    public String updateMembershipCard(String dataParam, AppParam appParam)throws ApiHandleException {
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppPolicyMembershipCard appPolicyMembershipCard=null;
        try{
            appPolicyMembershipCard=gson.fromJson(dataParam, AppPolicyMembershipCard.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appPolicyMembershipCard.getId()!=null&&!appPolicyMembershipCard.getId().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"ID");
        if(appPolicyMembershipCard.getIsPresentedViaActivate()!=null&&appPolicyMembershipCard.getIsPresentedViaActivate()==1)
            if(!(appPolicyMembershipCard.getPresentAmount()!=null&&!appPolicyMembershipCard.getPresentAmount().equals("")))
                throw new ApiHandleException(ResultCode.PARAM_LACK,"开卡赠送金额");
        if(appPolicyMembershipCard.getIsPresentedViaRecharge()!=null&&appPolicyMembershipCard.getIsPresentedViaRecharge()==1) {
            if (!(appPolicyMembershipCard.getRechargeLimitAmount() != null && !appPolicyMembershipCard.getRechargeLimitAmount().equals("")))
                throw new ApiHandleException(ResultCode.PARAM_LACK, "单笔充值限额");
            if (!(appPolicyMembershipCard.getRechargePresentAmount() != null && !appPolicyMembershipCard.getRechargePresentAmount().equals("")))
                throw new ApiHandleException(ResultCode.PARAM_LACK, "单笔充值赠送金额");
        }
        appPolicyMembershipCard.setDiscount(null);
        appPolicyMembershipCard.setIsDeposited(null);
        appPolicyMembershipCard.setCanRecharge(null);

        if(appPolicyMembershipCard.getSids()!=null&&!appPolicyMembershipCard.getSids().equals(""))
        {
            hsyMembershipDao.deleteMembershipCardShop(appPolicyMembershipCard.getId());
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
        }
        hsyMembershipDao.updateMembershipCard(appPolicyMembershipCard);
        return "";
    }

    /**HSY001066 查找会员列表*/
    public String findMemberList(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppPolicyMember appPolicyMember=null;
        try{
            appPolicyMember=gson.fromJson(dataParam, AppPolicyMember.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appPolicyMember.getUid()!=null&&!appPolicyMember.getUid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"用户法人ID");
//        if(!(appPolicyMember.getParam()!=null&&!appPolicyMember.getParam().equals("")))
//            throw new ApiHandleException(ResultCode.PARAM_LACK,"搜索条件");
        if(!(appPolicyMember.getCurrentPage()!=null&&!appPolicyMember.getCurrentPage().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"当前页数");
        if(appPolicyMember.getCurrentPage()<=0)
            throw new ApiHandleException(ResultCode.CURRENT_PAGE_MUST_BE_BIGGER_THAN_ZERO);

        PageUtils page=new PageUtils();
        page.setCurrentPage(appPolicyMember.getCurrentPage());
        page.setPageSize(AppConstant.PAGE_SIZE);
        Page<AppPolicyMember> pageAll=new Page<AppPolicyMember>();
        pageAll.setObjectT(appPolicyMember);
        pageAll.setPage(page);
        pageAll.getPage().setTotalRecord(hsyMembershipDao.findMemberListByPageCount(pageAll.getObjectT()));
        pageAll.setList(hsyMembershipDao.findMemberListByPage(pageAll));
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

    /**HSY001067 查找会员详情*/
    public String findMemberInfo(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppPolicyMember appPolicyMember=null;
        try{
            appPolicyMember=gson.fromJson(dataParam, AppPolicyMember.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appPolicyMember.getId()!=null&&!appPolicyMember.getId().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"会员ID");
        List<AppPolicyMember> memberList=hsyMembershipDao.findMemberInfoByID(appPolicyMember.getId());
        appPolicyMember=memberList.get(0);
        Optional<MemberAccount> account=memberAccountService.getById(appPolicyMember.getAccountID());
        appPolicyMember.setRemainingSum(account.get().getAvailable());
        appPolicyMember.setRechargeTotalAmount(account.get().getRechargeTotalAmount());
        appPolicyMember.setConsumeTotalAmount(account.get().getConsumeTotalAmount());
        if(account.get().getUpdateTime().compareTo(account.get().getCreateTime())!=0)
            appPolicyMember.setLastConsumeTime(account.get().getUpdateTime());

        Map result=new HashMap();
        result.put("appPolicyMember",appPolicyMember);
        gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
                if(date==null)
                    return null;
                return new JsonPrimitive(date.getTime());
            }
        }).registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if(json.getAsJsonPrimitive()==null)
                    return null;
                return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();
        return gson.toJson(result);
    }

    /**HSY001070 查找充值记录*/
    public String findRechargeOrderList(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppPolicyRechargeOrder appPolicyRechargeOrder=null;
        try{
            appPolicyRechargeOrder=gson.fromJson(dataParam, AppPolicyRechargeOrder.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appPolicyRechargeOrder.getMcid()!=null&&!appPolicyRechargeOrder.getMcid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"会员卡ID");
        if(!(appPolicyRechargeOrder.getCurrentPage()!=null&&!appPolicyRechargeOrder.getCurrentPage().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"当前页数");
        if(appPolicyRechargeOrder.getCurrentPage()<=0)
            throw new ApiHandleException(ResultCode.CURRENT_PAGE_MUST_BE_BIGGER_THAN_ZERO);

        PageUtils page=new PageUtils();
        page.setCurrentPage(appPolicyRechargeOrder.getCurrentPage());
        page.setPageSize(AppConstant.PAGE_SIZE);
        Page<AppPolicyRechargeOrder> pageAll=new Page<AppPolicyRechargeOrder>();
        pageAll.setObjectT(appPolicyRechargeOrder);
        pageAll.setPage(page);
        pageAll.getPage().setTotalRecord(hsyMembershipDao.findRechargeOrderListByPageCount(pageAll.getObjectT()));
        pageAll.setList(hsyMembershipDao.findRechargeOrderListByPage(pageAll));
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
                if(date==null)
                    return null;
                return new JsonPrimitive(date.getTime());
            }
        }).registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if(json.getAsJsonPrimitive()==null)
                    return null;
                return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();
        return gson.toJson(pageAll);
    }

    /**HSY001071 查找充值记录详情*/
    public String findRechargeOrderInfo(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppPolicyRechargeOrder appPolicyRechargeOrder=null;
        try{
            appPolicyRechargeOrder=gson.fromJson(dataParam, AppPolicyRechargeOrder.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appPolicyRechargeOrder.getId()!=null&&!appPolicyRechargeOrder.getId().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"充值订单ID");

        List<AppPolicyRechargeOrder> appPolicyRechargeOrderList=hsyMembershipDao.findRechargeOrderInfo(appPolicyRechargeOrder.getId());
        appPolicyRechargeOrder=appPolicyRechargeOrderList.get(0);

        gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
                if(date==null)
                    return null;
                return new JsonPrimitive(date.getTime());
            }
        }).registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if(json.getAsJsonPrimitive()==null)
                    return null;
                return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();

        Map map=new HashMap();
        map.put("appPolicyRechargeOrder",appPolicyRechargeOrder);
        return gson.toJson(map);
    }

    /**HSY001072 查找统计值*/
    public String findMemberStatistic(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppPolicyMembershipCard appPolicyMembershipCard=null;
        try{
            appPolicyMembershipCard=gson.fromJson(dataParam, AppPolicyMembershipCard.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appPolicyMembershipCard.getUid()!=null&&!appPolicyMembershipCard.getUid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"法人ID");

        Date date=new Date();
        Date yesterday=AppDateUtil.changeDate(date,Calendar.DAY_OF_MONTH,-1);
        Date startTimeYesterday=AppDateUtil.parseDate(AppDateUtil.formatDate(yesterday,AppDateUtil.DATE_FORMAT_NORMAL)+" 00:00:00");
        Date endTimeYesterday=AppDateUtil.parseDate(AppDateUtil.formatDate(yesterday,AppDateUtil.DATE_FORMAT_NORMAL)+" 23:59:59");
        Integer cardYesterdayCount=hsyMembershipDao.findMemberCardCascadeCountByUID(appPolicyMembershipCard.getUid(),startTimeYesterday,endTimeYesterday);
        AppPolicyMemberStatistic appPolicyMemberStatisticYesterday=hsyMembershipDao.findMemberConsumeStatistic(appPolicyMembershipCard.getUid(),startTimeYesterday,endTimeYesterday);
        appPolicyMemberStatisticYesterday.setCardCount(cardYesterdayCount);

        Date startTimeMonth=AppDateUtil.parseDate(AppDateUtil.formatDate(date,"yyyy-MM")+"-01 00:00:00");
        Date endTimeMonth=AppDateUtil.parseDate(AppDateUtil.formatDate(AppDateUtil.changeDate(date,Calendar.MONTH,1),"yyyy-MM")+"-01 00:00:00");
        Integer cardMonthCount=hsyMembershipDao.findMemberCardCascadeCountByUID(appPolicyMembershipCard.getUid(),startTimeMonth,endTimeMonth);
        AppPolicyMemberStatistic appPolicyMemberStatisticMonth=hsyMembershipDao.findMemberConsumeStatistic(appPolicyMembershipCard.getUid(),startTimeMonth,endTimeMonth);
        appPolicyMemberStatisticMonth.setCardCount(cardMonthCount);

        List<AppPolicyMembershipCard> appPolicyMembershipCardList=hsyMembershipDao.findMemberCardAndStatistic(appPolicyMembershipCard.getUid());
        Integer totalCount=0;
        for(AppPolicyMembershipCard card:appPolicyMembershipCardList)
            totalCount+=card.getMemberCount();
        for(AppPolicyMembershipCard card:appPolicyMembershipCardList){
            if(totalCount==0){
                card.setProportion("0%");
            }else {
                BigDecimal proportion = new BigDecimal(card.getMemberCount()).divide(new BigDecimal(totalCount),4,BigDecimal.ROUND_HALF_UP);
                BigDecimal proportionEx = proportion.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                card.setProportion(proportionEx + "%");
            }
        }

        List<AppPolicyMemberStatistic> appPolicyMemberStatistic12Month=new ArrayList<AppPolicyMemberStatistic>();
        appPolicyMemberStatisticMonth.setYear(AppDateUtil.formatDate(startTimeMonth,"yyyy"));
        appPolicyMemberStatisticMonth.setMonth(AppDateUtil.formatDate(startTimeMonth,"MM"));
        appPolicyMemberStatistic12Month.add(appPolicyMemberStatisticMonth);
        Date startTimeMonthX=startTimeMonth;
        Date endTimeMonthX=endTimeMonth;
        for(int i=0;i<11;i++){
            endTimeMonthX=startTimeMonthX;
            startTimeMonthX=AppDateUtil.changeDate(startTimeMonthX,Calendar.MONTH,-1);
            AppPolicyMemberStatistic appPolicyMemberStatisticTempMonth=hsyMembershipDao.findMemberConsumeStatistic(appPolicyMembershipCard.getUid(),startTimeMonthX,endTimeMonthX);
            Integer cardTempMonthCount=hsyMembershipDao.findMemberCardCascadeCountByUID(appPolicyMembershipCard.getUid(),startTimeMonthX,endTimeMonthX);
            appPolicyMemberStatisticTempMonth.setCardCount(cardTempMonthCount);
            appPolicyMemberStatisticTempMonth.setYear(AppDateUtil.formatDate(startTimeMonthX,"yyyy"));
            appPolicyMemberStatisticTempMonth.setMonth(AppDateUtil.formatDate(startTimeMonthX,"MM"));
            appPolicyMemberStatistic12Month.add(appPolicyMemberStatisticTempMonth);
        }
        gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
                if(date==null)
                    return null;
                return new JsonPrimitive(date.getTime());
            }
        }).registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if(json.getAsJsonPrimitive()==null)
                    return null;
                return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();

        Map map=new HashMap();
        map.put("appPolicyMemberStatisticYesterday",appPolicyMemberStatisticYesterday);
        map.put("appPolicyMemberStatisticMonth",appPolicyMemberStatisticMonth);
        map.put("appPolicyMembershipCardList",appPolicyMembershipCardList);
        map.put("totalCount",totalCount);
        map.put("appPolicyMemberStatistic12Month",appPolicyMemberStatistic12Month);
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
        List<AppPolicyMembershipCard> list=hsyMembershipDao.findMemberCardByParam(appPolicyMembershipCard);
        for(AppPolicyMembershipCard card:list){
            DecimalFormat a=new DecimalFormat("0.0");
            String discountStr=a.format(card.getDiscount());
            String discountInt=discountStr.split("\\.")[0];
            String discountFloat=discountStr.split("\\.")[1];
            card.setDiscountInt(discountInt);
            card.setDiscountFloat(discountFloat);
        }
        return list;
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

    public AppPolicyMember saveMember(Long cid,Long mcid,Integer status,Long accountID,Long receiptAccountID){
        Date date=new Date();
        AppPolicyMember appPolicyMember=new AppPolicyMember();
        appPolicyMember.setCid(cid);
        appPolicyMember.setMcid(mcid);
        appPolicyMember.setStatus(status);
        appPolicyMember.setCreateTime(date);
        appPolicyMember.setUpdateTime(date);
        appPolicyMember.setAccountID(accountID);
        appPolicyMember.setReceiptAccountID(receiptAccountID);
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

    public AppPolicyRechargeOrder saveOrder(AppPolicyMember appPolicyMember,String type,String source,BigDecimal amount){
        AppPolicyRechargeOrder appPolicyRechargeOrder = new AppPolicyRechargeOrder();
        Date date=new Date();
        if(type.equals(RechargeValidType.ACTIVATE.key)) {
            String describe=RechargeValidType.ACTIVATE.value + "-充值金额为:" + appPolicyMember.getDepositAmount();
            int orderType= OrderType.ACTIVATE.key;
            BigDecimal marketingAmount=BigDecimal.ZERO;
            appPolicyRechargeOrder.setRealPayAmount(appPolicyMember.getDepositAmount());
            if (appPolicyMember.getIsPresentedViaActivate() == 1) {
                appPolicyRechargeOrder.setTradeAmount(appPolicyMember.getDepositAmount().add(appPolicyMember.getPresentAmount()));
                describe+="-赠送金额为:"+appPolicyMember.getPresentAmount();
                orderType= OrderType.ACTIVATE_PRESENT.key;
                marketingAmount=appPolicyMember.getPresentAmount();
            } else
                appPolicyRechargeOrder.setTradeAmount(appPolicyMember.getDepositAmount());
            appPolicyRechargeOrder.setGoodsName(appPolicyMember.getMembershipShopName());
            appPolicyRechargeOrder.setGoodsDescribe(describe);
            appPolicyRechargeOrder.setType(orderType);
            appPolicyRechargeOrder.setMarketingAmount(marketingAmount);
        }else if(type.equals(RechargeValidType.RECHARGE.key)){
            String describe=RechargeValidType.RECHARGE.value + "-充值金额为:" + amount;
            int orderType= OrderType.RECHARGE.key;
            appPolicyRechargeOrder.setRealPayAmount(amount);
            BigDecimal marketingAmount=BigDecimal.ZERO;
            if (appPolicyMember.getIsPresentedViaRecharge() == 1) {
                int presentCount=amount.divide(appPolicyMember.getRechargeLimitAmount()).intValue();
                marketingAmount=appPolicyMember.getRechargePresentAmount().multiply(new BigDecimal(presentCount));
                appPolicyRechargeOrder.setTradeAmount(amount.add(marketingAmount));
                describe+="-赠送金额为:"+marketingAmount;
                orderType= OrderType.RECHARGE_PRESENT.key;
            } else
                appPolicyRechargeOrder.setTradeAmount(amount);
            appPolicyRechargeOrder.setGoodsName(appPolicyMember.getMembershipShopName());
            appPolicyRechargeOrder.setGoodsDescribe(describe);
            appPolicyRechargeOrder.setType(orderType);
            appPolicyRechargeOrder.setMarketingAmount(marketingAmount);
        }

        UserCurrentChannelPolicy userCurrentChannelPolicy=userCurrentChannelPolicyDao.selectByUserId(appPolicyMember.getUid());

        if(source.equals("ZFB")) {
            appPolicyRechargeOrder.setPayChannelSign(userCurrentChannelPolicy.getAlipayChannelTypeSign());
            appPolicyRechargeOrder.setOuid(appPolicyMember.getUserID());
            List<BasicChannel> channelList=hsyMembershipDao.findChannelAccountID(appPolicyRechargeOrder.getPayChannelSign());
            appPolicyRechargeOrder.setPayeeAccountID(channelList.get(0).getAccountid());
            appPolicyRechargeOrder.setSource("alipay");
        }else {
            appPolicyRechargeOrder.setPayChannelSign(userCurrentChannelPolicy.getWechatChannelTypeSign());
            appPolicyRechargeOrder.setOuid(appPolicyMember.getOpenID());
            List<BasicChannel> channelList=hsyMembershipDao.findChannelAccountID(appPolicyRechargeOrder.getPayChannelSign());
            appPolicyRechargeOrder.setPayeeAccountID(channelList.get(0).getAccountid());
            appPolicyRechargeOrder.setSource("wechat");
        }
        appPolicyRechargeOrder.setMemberID(appPolicyMember.getId());
        appPolicyRechargeOrder.setMemberAccountID(appPolicyMember.getAccountID());
        appPolicyRechargeOrder.setCid(appPolicyMember.getCid());
        appPolicyRechargeOrder.setMcid(appPolicyMember.getMcid());
        appPolicyRechargeOrder.setUid(appPolicyMember.getUid());
        appPolicyRechargeOrder.setMerchantReceiveAccountID(appPolicyMember.getReceiptAccountID());
        List<AppAuUser> userList=hsyMembershipDao.findShopNameAndGlobalID(appPolicyMember.getUid());
        appPolicyRechargeOrder.setMerchantName(userList.get(0).getShopName());
        appPolicyRechargeOrder.setMerchantNO(userList.get(0).getGlobalID());
        appPolicyRechargeOrder.setStatus(OrderStatus.NEED_RECHARGE.key);
        appPolicyRechargeOrder.setCreateTime(date);
        appPolicyRechargeOrder.setUpdateTime(date);
        hsyMembershipDao.insertRechargeOrder(appPolicyRechargeOrder);
        DecimalFormat a=new DecimalFormat("0000000000");
        AppPolicyRechargeOrder appPolicyRechargeOrderUp=new AppPolicyRechargeOrder();
        appPolicyRechargeOrderUp.setId(appPolicyRechargeOrder.getId());
        appPolicyRechargeOrderUp.setOrderNO("RC"+ AppDateUtil.formatDate(date,AppDateUtil.TIME_FORMAT_SHORT+a.format(appPolicyRechargeOrder.getId())));
        appPolicyRechargeOrder.setOrderNO(appPolicyRechargeOrderUp.getOrderNO());
        hsyMembershipDao.updateRechargeOrder(appPolicyRechargeOrderUp);
        return appPolicyRechargeOrder;
    }

    public void updateOrder(AppPolicyRechargeOrder appPolicyRechargeOrder,String tradeNO,Long tradeID){
        AppPolicyRechargeOrder appPolicyRechargeOrderUp=new AppPolicyRechargeOrder();
        appPolicyRechargeOrderUp.setId(appPolicyRechargeOrder.getId());
        appPolicyRechargeOrderUp.setTradeNO(tradeNO);
        appPolicyRechargeOrderUp.setTradeID(tradeID);
        hsyMembershipDao.updateRechargeOrder(appPolicyRechargeOrderUp);
        appPolicyRechargeOrder.setOrderNO(tradeNO);
        appPolicyRechargeOrder.setTradeID(tradeID);
    }

    public List<AppBizShop> findSuitShopByMCID(Long mcid) {
        return hsyMembershipDao.findSuitShopByMCID(mcid);
    }

    public Page<AppPolicyRechargeOrder> findRechargeOrderListByPage(Page<AppPolicyRechargeOrder> pageAll){
        pageAll.getPage().setTotalRecord(hsyMembershipDao.findRechargeOrderListByPageCount(pageAll.getObjectT()));
        pageAll.setList(hsyMembershipDao.findRechargeOrderListByPage(pageAll));
        return pageAll;
    }

    public List<AppPolicyMember> findMemberListByOUID(AppPolicyConsumer appPolicyConsumer){
        List<AppPolicyMember> list=hsyMembershipDao.findMemberListByOUID(appPolicyConsumer);
        for(AppPolicyMember member:list){
            DecimalFormat a=new DecimalFormat("0.0");
            String discountStr=a.format(member.getDiscount());
            String discountInt=discountStr.split("\\.")[0];
            String discountFloat=discountStr.split("\\.")[1];
            member.setDiscountInt(discountInt);
            member.setDiscountFloat(discountFloat);
        }
        return list;
    }

}
