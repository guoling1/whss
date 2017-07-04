package com.jkm.hss.controller.pc;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.admin.helper.AdminUserSupporter;
import com.jkm.hss.admin.helper.responseparam.QRCodeList;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hsy.user.Enum.EnumPolicyType;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.constant.IndustryCodeType;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.dao.UserTradeRateDao;
import com.jkm.hsy.user.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong.zhang on 2017/7/4.
 */
@Slf4j
@Controller
@RequestMapping(value = "/pc/trade")
public class PcTradeController {

    @Autowired
    private HsyUserDao hsyUserDao;
    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private UserTradeRateDao userTradeRateDao;
    @Autowired
    private QRCodeService qRCodeService;

    @ResponseBody
    @RequestMapping(value = "login")
    public CommonResponse login(@RequestBody AppAuUser appAuUser) {
        /**参数验证*/
        if(!(appAuUser.getCellphone()!=null&&!appAuUser.getCellphone().equals("")))
            CommonResponse.simpleResponse(-1, "参数缺失：手机号");
        if(!(appAuUser.getPassword()!=null&&!appAuUser.getPassword().equals("")))
            CommonResponse.simpleResponse(-1, "参数缺失：密码");

        /**查询用户*/
        List<AppAuUser> list = hsyUserDao.findAppAuUserByParam(appAuUser);
        if (!(list != null && list.size() != 0))
            CommonResponse.simpleResponse(-1, "该手机号没有注册");
        AppAuUser appAuUserFind=list.get(0);
        if(!appAuUserFind.getPassword().equals(appAuUser.getPassword()))
            CommonResponse.simpleResponse(-1, "密码错误");
        if(appAuUserFind.getStatus().equals(AppConstant.USER_STATUS_FORBID))
            CommonResponse.simpleResponse(-1, "您已被禁止登陆");

        AppBizShop appBizShop=new AppBizShop();
        appBizShop.setUid(appAuUserFind.getId());
        if(appAuUserFind.getParentID()==null||(appAuUserFind.getParentID()!=null&&appAuUserFind.getParentID()==0L))
            appBizShop.setType(AppConstant.ROLE_TYPE_PRIMARY);
        else
        {
            List<AppAuUser> parentList=hsyUserDao.findAppAuUserByID(appAuUserFind.getParentID());
            if(parentList!=null&&parentList.size()!=0) {
                appAuUserFind.setAccountID(parentList.get(0).getAccountID());
                appAuUserFind.setDealerID(parentList.get(0).getDealerID());
            }
        }
        List<AppBizShop> shopList=hsyShopDao.findPrimaryAppBizShopByUserID(appBizShop);
        if(shopList!=null&&shopList.size()!=0)
            appBizShop=shopList.get(0);
//        if(AppConstant.USER_STATUS_NORMAL!=appAuUserFind.getStatus())
//            throw new ApiHandleException(ResultCode.USER_NO_CEHCK);
        if(appBizShop.getCheckErrorInfo()==null)
            appBizShop.setCheckErrorInfo("");
        if(appBizShop.getDistrictCode()!=null&&!appBizShop.getDistrictCode().equals("")){
            String districtName="";
            String parentCode="";
            String districtCode=appBizShop.getDistrictCode();
            while(!parentCode.equals("0")) {
                List<AppBizDistrict> appBizDistrictList = hsyShopDao.findDistrictByCode(districtCode);
                parentCode=appBizDistrictList.get(0).getParentCode();
                if(!districtName.equals(""))
                    districtName=appBizDistrictList.get(0).getAname()+"|"+districtName;
                else
                    districtName=appBizDistrictList.get(0).getAname();
                districtCode=parentCode;
            }
            appBizShop.setDistrictName(districtName);
        }
        if(appBizShop.getIndustryCode()!=null&&!appBizShop.getIndustryCode().equals(""))
            appBizShop.setIndustryName(IndustryCodeType.getValue(Integer.parseInt(appBizShop.getIndustryCode())));

        AppBizCard appBizCard=new AppBizCard();
        appBizCard.setSid(appBizShop.getId());
        List<AppBizCard> appBizCardList=hsyShopDao.findAppBizCardByParam(appBizCard);
        if(appBizCardList!=null&&appBizCardList.size()!=0)
            appBizCard=appBizCardList.get(0);
        if(appBizCard.getBranchDistrictCode()!=null&&!appBizCard.getBranchDistrictCode().equals("")){
            String districtName="";
            String parentCode="";
            String districtCode=appBizCard.getBranchDistrictCode();
            while(!parentCode.equals("0")) {
                List<AppBizDistrict> appBizDistrictList = hsyShopDao.findDistrictByCode(districtCode);
                parentCode=appBizDistrictList.get(0).getParentCode();
                if(!districtName.equals(""))
                    districtName=appBizDistrictList.get(0).getAname()+"|"+districtName;
                else
                    districtName=appBizDistrictList.get(0).getAname();
                districtCode=parentCode;
            }
            appBizCard.setBranchDistrictName(districtName);
        }
        if(appBizCard.getBranchCode()==null)
            appBizCard.setBranchCode("-1");
        List<UserTradeRate> userTradeRateList=userTradeRateDao.selectAllByUserId(appAuUserFind.getId());
        AppChannelRate appChannelRate=new AppChannelRate();
        appChannelRate.setIsOpenD0(appAuUserFind.getIsOpenD0());
        appChannelRate.setWithdrawAmount(new BigDecimal("0.01"));
        if(userTradeRateList!=null&&userTradeRateList.size()!=0){
            for(UserTradeRate userTradeRate:userTradeRateList){
                if(userTradeRate.getPolicyType()!=null&&userTradeRate.getPolicyType().equals(EnumPolicyType.ALIPAY.getId()))
                {
                    appChannelRate.setAlipayTradeRateT1(userTradeRate.getTradeRateT1());
                    appChannelRate.setAlipayIsOpen(userTradeRate.getIsOpen());
                    appAuUserFind.setAlipayRate(userTradeRate.getTradeRateT1());
                }
                if(userTradeRate.getPolicyType()!=null&&userTradeRate.getPolicyType().equals(EnumPolicyType.WECHAT.getId()))
                {
                    appChannelRate.setWechatTradeRateT1(userTradeRate.getTradeRateT1());
                    appChannelRate.setWechatIsOpen(userTradeRate.getIsOpen());
                    appAuUserFind.setWeixinRate(userTradeRate.getTradeRateT1());
                }
            }
        }else{
            if(appAuUserFind.getAlipayRate()==null)
                appAuUserFind.setAlipayRate(BigDecimal.ZERO);
            if(appAuUserFind.getWeixinRate()==null)
                appAuUserFind.setWeixinRate(BigDecimal.ZERO);
        }

        if(appAuUserFind.getPassword().equals("888888"))
            appAuUserFind.setIsNeededAltingPassword(1);
        else
            appAuUserFind.setIsNeededAltingPassword(0);

        List<AdminUser> adminUserList=hsyUserDao.findAdminUserByUID(appAuUserFind.getId());
        if(adminUserList!=null&&adminUserList.size()!=0)
            if(adminUserList.get(0).getMobile()!=null)
                appAuUserFind.setAuCellphone(AdminUserSupporter.decryptMobile(0,adminUserList.get(0).getMobile()));


        Map map=new HashMap();

        List<AppBizShop> userShopList=hsyShopDao.findShopListByUID(appAuUserFind.getId());
        List shopQRList=new ArrayList();
        if(userShopList!=null&&userShopList.size()!=0) {
            for (AppBizShop shopQR : userShopList) {
                Map qrMap=new HashMap();
                List<QRCodeList> qrList = qRCodeService.bindShopList(shopQR.getId(), AppConstant.FIlE_ROOT);
                qrMap.put("shortName",shopQR.getShortName());
                qrMap.put("name",shopQR.getName());
                qrMap.put("type",shopQR.getType());
                if(qrList!=null&&qrList.size()!=0)
                    qrMap.put("qrList",qrList);
                shopQRList.add(qrMap);
            }
        }
        else
        {
            shopQRList=null;
        }
        appAuUserFind.setPassword("");
        Map qr=new HashMap();
        qr.put("shopQRList",shopQRList);
        qr.put("qrUrl",AppConstant.QR_URL);
        map.put("qr",qr);

        map.put("appAuUser",appAuUserFind);
        map.put("appBizShop",appBizShop);
        map.put("appBizCard",appBizCard);
        map.put("appChannelRate",appChannelRate);

        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE,"success",map);
    }

}
