package com.jkm.hss.controller.code;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.enums.EnumQRCodeSysType;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.BindShopRequest;
import com.jkm.hss.helper.request.ConfirmBindRequest;
import com.jkm.hss.helper.response.ConfirmBindResponse;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppBizShop;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Thinkpad on 2017/1/16.
 * 二维码业务逻辑
 */
@Slf4j
@Controller
@RequestMapping(value = "/qrCode")
public class QRCodeController extends BaseController {
    @Autowired
    private QRCodeService qrCodeService;
    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private HsyUserDao hsyUserDao;

    /**
     * 店铺绑定二维码
     * @param bindShopRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "bindShop", method = RequestMethod.POST)
    public CommonResponse bindShop(@RequestBody BindShopRequest bindShopRequest) {
        String code = bindShopRequest.getCode();
        log.info("获取的二维码是{}",code);
        if (StringUtils.isBlank(code)) {
            return CommonResponse.simpleResponse(-1, "二维码不能为空");
        }
        //二维码
        Optional<QRCode> qrCodeOptional =  qrCodeService.getByCode(code, EnumQRCodeSysType.HSY.getId());
        if(!qrCodeOptional.isPresent()){//不存在
            return CommonResponse.simpleResponse(-1, "二维码不存在");
        }
        //店铺
        AppBizShop appBizShop = new AppBizShop();
        appBizShop.setId(bindShopRequest.getShopId());
        List<AppBizShop> shops =  hsyShopDao.findShopDetail(appBizShop);
        if(shops==null||shops.size()==0){
            return CommonResponse.simpleResponse(-1, "该店铺不存在");
        }
        //登录用户
        List<AppAuUser> appAuUsers = hsyShopDao.findCorporateUserByShopID(bindShopRequest.getShopId());
        if(appAuUsers==null||appAuUsers.size()==0){
            return CommonResponse.simpleResponse(-1, "登录信息有误，此店铺不属于该用户名下");
        }
        if(appAuUsers.get(0).getId()!=bindShopRequest.getUserId()){
            return CommonResponse.simpleResponse(-1, "登录信息有误");
        }
        long merchantId = qrCodeOptional.get().getMerchantId();
        if(merchantId>0){//已经绑定过
            //店铺
            AppBizShop appBizShop1 = new AppBizShop();
            appBizShop1.setId(merchantId);
            List<AppBizShop> shops1 =  hsyShopDao.findShopDetail(appBizShop1);
            if(shops1==null||shops1.size()==0){
                log.info("信息错误，不应该出现此类情况");
                return CommonResponse.simpleResponse(-1, "二维码绑定的店铺不存在");
            }
            List<AppAuUser> appAuUsers1 = hsyShopDao.findCorporateUserByShopID(merchantId);
            if(appAuUsers1==null||appAuUsers1.size()==0){
                return CommonResponse.simpleResponse(-1, "二维码关联的用户信息有误");
            }
            if(appAuUsers1.get(0).getId()!=bindShopRequest.getUserId()){//是同一个人的二维码
                //切换绑定
                return CommonResponse.simpleResponse(1, "该二维码已经绑定到门店“"+shops1.get(0).getShortName()+"”下，确认绑定到“"+shops.get(0).getShortName()+"”下吗？");
            }else{//不是同一个人的二维码
                return CommonResponse.simpleResponse(-1, "该二维码已被其他商户绑定");
            }
        }else{//没有绑定过
            return CommonResponse.simpleResponse(1, "确认将该二维码已经绑定到门店“"+shops.get(0).getShortName()+"”下吗？");
        }
    }
    /**
     * 店铺绑定二维码
     * @param confirmBindRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "confirmBind", method = RequestMethod.POST)
    public CommonResponse confirmBind(@RequestBody ConfirmBindRequest confirmBindRequest) {
        String code = confirmBindRequest.getCode();
        log.info("获取的二维码是{}",code);
        if (StringUtils.isBlank(code)) {
            return CommonResponse.simpleResponse(-1, "二维码不能为空");
        }
        //二维码
        Optional<QRCode> qrCodeOptional =  qrCodeService.getByCode(code, EnumQRCodeSysType.HSY.getId());
        if(!qrCodeOptional.isPresent()){//不存在
            return CommonResponse.simpleResponse(-1, "二维码不存在");
        }
        //店铺
        AppBizShop appBizShop = new AppBizShop();
        appBizShop.setId(confirmBindRequest.getShopId());
        List<AppBizShop> shops =  hsyShopDao.findShopDetail(appBizShop);
        if(shops==null||shops.size()==0){
            return CommonResponse.simpleResponse(-1, "该店铺不存在");
        }
        //登录用户
        List<AppAuUser> appAuUsers = hsyUserDao.findAppAuUserByID(confirmBindRequest.getUserId());
        if(appAuUsers==null||appAuUsers.size()==0){
            return CommonResponse.simpleResponse(-1, "没有该商户");
        }
        int count = qrCodeService.bindShop(confirmBindRequest.getCode(),confirmBindRequest.getShopId(),EnumQRCodeSysType.HSY.getId());
        ConfirmBindResponse confirmBindResponse = new ConfirmBindResponse();
        confirmBindResponse.setCode(confirmBindRequest.getCode());
        confirmBindResponse.setName(shops.get(0).getShortName());
        if(count>0){
            qrCodeService.markAsActivate(confirmBindRequest.getCode(),confirmBindRequest.getShopId());
            return CommonResponse.objectResponse(1, "绑定成功",confirmBindResponse);
        }else {
            return CommonResponse.simpleResponse(-1, "绑定失败，请重试");
        }
    }





}
