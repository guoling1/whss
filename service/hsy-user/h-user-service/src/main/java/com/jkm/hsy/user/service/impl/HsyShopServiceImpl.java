package com.jkm.hsy.user.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppBizCard;
import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.entity.AppBizShopUserRole;
import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.jkm.hsy.user.service.HsyShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Allen on 2017/1/10.
 */
@Service("hsyShopService")
public class HsyShopServiceImpl implements HsyShopService {
    @Autowired
    private HsyShopDao hsyShopDao;
    /**HSY001005 保存店铺资料*/
    public String insertHsyShop(String dataParam,AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppBizShop appBizShop=null;
        try{
            appBizShop=gson.fromJson(dataParam, AppBizShop.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(appBizShop.getName()!=null&&!appBizShop.getName().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"店铺名称");
        if(!(appBizShop.getShortName()!=null&&!appBizShop.getShortName().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"店铺简称");
        if(!(appBizShop.getIndustryCode()!=null&&!appBizShop.getIndustryCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"所属行业");
        if(!(appBizShop.getDistrictCode()!=null&&!appBizShop.getDistrictCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"所在地");
        if(!(appBizShop.getAddress()!=null&&!appBizShop.getAddress().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"街道");
        if(!(appBizShop.getIsPublic()!=null&&!appBizShop.getIsPublic().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"结算类型");
        if(appBizShop.getUid()==null)
            throw new ApiHandleException(ResultCode.PARAM_LACK,"用户ID");

        /**商铺保存*/
        try {
            Date date=new Date();
            appBizShop.setCreateTime(date);
            appBizShop.setUpdateTime(date);
            appBizShop.setStatus(AppConstant.SHOP_STATUS_NORMAL);
            appBizShop.setParentID(0L);
            hsyShopDao.insert(appBizShop);
            AppBizShopUserRole appBizShopUserRole=new AppBizShopUserRole();
            appBizShopUserRole.setSid(appBizShop.getId());
            appBizShopUserRole.setUid(appBizShop.getUid());
            appBizShopUserRole.setRole(AppConstant.ROLE_CORPORATION);
            appBizShopUserRole.setStatus(AppConstant.ROLE_STATUS_NORMAL);
            hsyShopDao.insertAppBizShopUserRole(appBizShopUserRole);
        }catch(Exception e){
            throw new ApiHandleException(ResultCode.INSERT_ERROR);
        }
        return "{\"id\":"+appBizShop.getId()+"}";
    }

    /**HSY001006 更新店铺资料联系人*/
    public String updateHsyShopContact(String dataParam,AppParam appParam)throws ApiHandleException{
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
        if (!ValidateUtils.isMobile(appBizShop.getContactCellphone()))
            throw new ApiHandleException(ResultCode.CELLPHONE_NOT_CORRECT_FORMAT);

        /**商铺修改*/
        try {
            Date date=new Date();
            appBizShop.setUpdateTime(date);
            hsyShopDao.update(appBizShop);
        }catch(Exception e){
            throw new ApiHandleException(ResultCode.INSERT_ERROR);
        }
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
        try {
            Date date=new Date();
            appBizCard.setCreateTime(date);
            appBizCard.setUpdateTime(date);
            appBizCard.setStatus(AppConstant.CARD_STATUS_NORMAL);
            hsyShopDao.insertAppBizCard(appBizCard);
        }catch(Exception e){
            throw new ApiHandleException(ResultCode.INSERT_ERROR);
        }
        return "{\"id\":"+appBizCard.getId()+"}";
    }
}
