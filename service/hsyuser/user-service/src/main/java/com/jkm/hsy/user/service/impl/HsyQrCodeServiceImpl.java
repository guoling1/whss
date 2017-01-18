package com.jkm.hsy.user.service.impl;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.enums.EnumQRCodeActivateStatus;
import com.jkm.hss.admin.enums.EnumQRCodeSysType;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppBindShop;
import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.jkm.hsy.user.service.HsyQrCodeService;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Thinkpad on 2017/1/17.
 */
@Service("hsyQrCodeService")
public class HsyQrCodeServiceImpl implements HsyQrCodeService{
    @Autowired
    private QRCodeService qrCodeService;
    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private HsyUserDao hsyUserDao;

    /**
     * 绑定二维码
     *
     * @param dataParam
     * @param appParam
     * @return
     */
    @Transactional
    @Override
    public String bindQrCode(String dataParam, AppParam appParam) throws ApiHandleException {
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        AppBindShop appBindShop=null;
        try{
            appBindShop=gson.fromJson(dataParam, AppBindShop.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }
        /**参数验证*/
        if(!(appBindShop.getCode()!=null&&!appBindShop.getCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"二维码不能为空");
        if(!(appBindShop.getUserId()!=null&&!appBindShop.getUserId().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"用户编码");
        if(!(appBindShop.getShopId()!=null&&!appBindShop.getShopId().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"店铺编码");
        /**数据验证*/
        Optional<QRCode> qrCodeOptional =  qrCodeService.getByCode(appBindShop.getCode(), EnumQRCodeSysType.HSY.getId());
        if(!qrCodeOptional.isPresent())
            throw new ApiHandleException(ResultCode.RESULT_FAILE,"二维码不存在");
        AppBizShop appBizShop = new AppBizShop();
        appBizShop.setId(appBindShop.getShopId());
        List<AppBizShop> shops =  hsyShopDao.findShopDetail(appBizShop);
        if(shops==null||shops.size()==0)
            throw new ApiHandleException(ResultCode.RESULT_FAILE,"该店铺不存在");
        List<AppAuUser> appAuUsers = hsyUserDao.findAppAuUserByID(appBindShop.getUserId());
        if(appAuUsers==null||appAuUsers.size()==0)
            throw new ApiHandleException(ResultCode.RESULT_FAILE,"该用户不存在");
        if(qrCodeOptional.get().getActivateStatus()== EnumQRCodeActivateStatus.ACTIVATE.getCode())
            throw new ApiHandleException(ResultCode.RESULT_FAILE,"该二维码已经被激活，不能再次绑定");
        //是否在同一代理商下，是否在同一产品下
        Triple<Long, Long, Long> triple = qrCodeService.getCurrentAndFirstAndSecondByCode(appBindShop.getCode());
        long currentDealerId = triple.getLeft();
        long productId = qrCodeOptional.get().getProductId();
        // TODO: 2017/1/17 调用查询用户接口
        if(currentDealerId==1)
            throw new ApiHandleException(ResultCode.RESULT_FAILE,"二维码必须绑定在同一代理商下");
        if(productId==1)
            throw new ApiHandleException(ResultCode.RESULT_FAILE,"必须有相同的产品");
        //绑定并激活
        qrCodeService.markAsActivate(appBindShop.getCode(),appBindShop.getShopId());
        // TODO: 2017/1/17 调用查询费率接口

        // TODO: 2017/1/17 保存费率

        return "";

    }


}
