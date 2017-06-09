package com.jkm.hss.controller.hsyMerchant;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.admin.helper.AdminUserSupporter;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.notifier.service.SmsAuthService;
import com.jkm.hss.push.sevice.PushService;
import com.jkm.hsy.user.Enum.EnumHxbsOpenProductStatus;
import com.jkm.hsy.user.Enum.EnumHxbsStatus;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.dao.HsyCmbcDao;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.entity.*;
import com.jkm.hsy.user.help.requestparam.CmbcResponse;
import com.jkm.hsy.user.service.HsyCmbcService;
import com.jkm.hsy.user.service.HsyMerchantAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2017/1/20.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/hsyMerchantAudit")
public class HsyMerchantAuditController extends BaseController {

    @Autowired
    private HsyMerchantAuditService hsyMerchantAuditService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PushService pushService;

    @Autowired
    private HsyCmbcService hsyCmbcService;

    @Autowired
    private HsyUserDao hsyUserDao;

    @Autowired
    private HsyCmbcDao hsyCmbcDao;

    @Autowired
    private SmsAuthService smsAuthService;

    @Autowired
    private SendMessageService sendMessageService;

    @ResponseBody
    @RequestMapping(value = "/throughAudit",method = RequestMethod.POST)
    public CommonResponse throughAudit(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){
        final HsyMerchantAuditResponse hsyMerchantAudit = this.hsyMerchantAuditService.selectById(hsyMerchantAuditRequest.getId());
        if (hsyMerchantAudit==null) {
            return CommonResponse.simpleResponse(-1, "商户不存在");
        }
        AppAuUser acct = this.hsyMerchantAuditService.getAccId(hsyMerchantAuditRequest.getId());
        if (acct!=null){
            accountService.delAcct(acct.getAccountID());
        }
        final long accountId = this.accountService.initAccount(hsyMerchantAuditRequest.getName());
        hsyMerchantAuditRequest.setAccountID(accountId);
        hsyMerchantAuditService.updateAccount(hsyMerchantAuditRequest.getAccountID(),hsyMerchantAuditRequest.getUid());
        hsyMerchantAuditRequest.setStatus(AppConstant.SHOP_STATUS_NORMAL);
        hsyMerchantAuditService.auditPass(hsyMerchantAuditRequest);
        pushService.pushAuditMsg(hsyMerchantAuditRequest.getUid(),true);

        //审核通过发短信
        this.sendMessageService.sendMessage(SendMessageParams.builder() .mobile(hsyMerchantAuditRequest.getCellphone())
                .uid("")
                .userType(EnumUserType.BACKGROUND_USER)
                .noticeType(EnumNoticeType.PASS_MESSAGE)
                .build()
        );

        CmbcResponse cmbcResponse = hsyCmbcService.merchantBaseInfoReg(hsyMerchantAuditRequest.getUid(),hsyMerchantAuditRequest.getId());
        if(cmbcResponse.getCode()==1){
            hsyUserDao.updateHxbsStatus(EnumHxbsStatus.PASS.getId(),cmbcResponse.getMsg(),hsyMerchantAuditRequest.getUid());
            AppAuUser appAuUser = hsyCmbcDao.selectByUserId(hsyMerchantAuditRequest.getUid());
            //入驻成功再开通产品
            if(appAuUser.getWeixinRate()!=null&&!"".equals(appAuUser.getWeixinRate())){//添加产品
                CmbcResponse cmbcResponse1 = hsyCmbcService.merchantBindChannel(hsyMerchantAuditRequest.getUid(),hsyMerchantAuditRequest.getId());
                if(cmbcResponse1.getCode()==1){//开通产品成功
                    hsyCmbcDao.updateHxbUserById(EnumHxbsOpenProductStatus.PASS.getId(),cmbcResponse1.getMsg(),hsyMerchantAuditRequest.getUid());
                }else{//开通产品失败
                    hsyCmbcDao.updateHxbUserById(EnumHxbsOpenProductStatus.UNPASS.getId(),cmbcResponse1.getMsg(),hsyMerchantAuditRequest.getUid());
                }
            }
        }else{
            hsyUserDao.updateHxbsStatus(EnumHxbsStatus.UNPASS.getId(),cmbcResponse.getMsg(),hsyMerchantAuditRequest.getUid());
        }
        hsyMerchantAuditRequest.setStat(0);
        this.hsyMerchantAuditService.saveLog(super.getAdminUser().getUsername(),hsyMerchantAuditRequest.getId(),hsyMerchantAuditRequest.getCheckErrorInfo(),hsyMerchantAuditRequest.getStat());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"审核通过");

    }


    @ResponseBody
    @RequestMapping(value = "/rejectToExamine",method = RequestMethod.POST)
    public CommonResponse rejectToExamine(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){
        if (hsyMerchantAuditRequest.getCheckErrorInfo()==null||hsyMerchantAuditRequest.getCheckErrorInfo().equals("")){
            return CommonResponse.simpleResponse(-1,"请填写错误信息");
        }
        hsyMerchantAuditRequest.setStatus(AppConstant.SHOP_STATUS_REJECT);
        hsyMerchantAuditService.auditNotPass(hsyMerchantAuditRequest);
        long uid=hsyMerchantAuditService.getUid(hsyMerchantAuditRequest.getId());
        if (uid!=0){
            hsyMerchantAuditService.stepChange(uid);
        }
        pushService.pushAuditMsg(hsyMerchantAuditRequest.getUid(),false);

        //审核未通过发短信
        this.sendMessageService.sendMessage(SendMessageParams.builder() .mobile(hsyMerchantAuditRequest.getCellphone())
                .uid("")
                .userType(EnumUserType.BACKGROUND_USER)
                .noticeType(EnumNoticeType.NOT_PASS_MESSAGE)
                .build()
        );
        if(!"".equals(hsyMerchantAuditRequest.getMobile())&&hsyMerchantAuditRequest.getMobile()!=null){
            String mobile = hsyMerchantAuditRequest.getMobile();
//            MerchantSupport.decryptMobile(mobile);
//            AdminUserSupporter.decryptMobile(0, mobile);
            Map map = new HashMap();
            map.put("name",hsyMerchantAuditRequest.getName());
            //审核未通过给报单员发短信
            this.sendMessageService.sendMessage(SendMessageParams.builder() .mobile(AdminUserSupporter.decryptMobile(0, mobile))
                    .uid("")
                    .data(map)
                    .userType(EnumUserType.BACKGROUND_USER)
                    .noticeType(EnumNoticeType.NOT_PASS_MESSAGE_EMPLOYEE)
                    .build()
            );
        }


        hsyMerchantAuditRequest.setStat(1);
        this.hsyMerchantAuditService.saveLog(super.getAdminUser().getUsername(),hsyMerchantAuditRequest.getId(),hsyMerchantAuditRequest.getCheckErrorInfo(),hsyMerchantAuditRequest.getStat());
        return CommonResponse.simpleResponse(1,"审核未通过");

    }




    /**
     * 重新入网
     * @param appUserAndShopRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/reenter",method = RequestMethod.POST)
    public CommonResponse reenter(@RequestBody final AppUserAndShopRequest appUserAndShopRequest){
        log.info("进入重新入网");
        AppBizShop appBizShop = hsyCmbcDao.selectByShopId(appUserAndShopRequest.getShopId());
        if(appBizShop.getStatus()!=AppConstant.SHOP_STATUS_NORMAL){
            return CommonResponse.simpleResponse(-1,"该商户未通过审核，不能重新入网");
        }
        AppAuUser appAuUser = hsyCmbcDao.selectByUserId(appUserAndShopRequest.getUserId());
        if(appAuUser.getHxbStatus()!=null&&appAuUser.getHxbStatus()==EnumHxbsStatus.PASS.getId()){
            if(appAuUser.getHxbOpenProduct()==null||appAuUser.getHxbOpenProduct()==EnumHxbsOpenProductStatus.UNPASS.getId()){
                CmbcResponse cmbcResponse1 = hsyCmbcService.merchantBindChannel(appUserAndShopRequest.getUserId(),appUserAndShopRequest.getShopId());
                if(cmbcResponse1.getCode()==1){//开通产品成功
                    hsyCmbcDao.updateHxbUserById(EnumHxbsOpenProductStatus.PASS.getId(),cmbcResponse1.getMsg(),appUserAndShopRequest.getUserId());
                    return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"该商户已经入网且开通产品成功");
                }else{//开通产品失败
                    hsyCmbcDao.updateHxbUserById(EnumHxbsOpenProductStatus.UNPASS.getId(),cmbcResponse1.getMsg(),appUserAndShopRequest.getUserId());
                    return CommonResponse.simpleResponse(-1,"该商户已经入网但,开通产品失败");
                }
            }else{
                return CommonResponse.simpleResponse(-1,"该商户已经入网，不能重复入网");
            }
        }else{
            CmbcResponse cmbcResponse = hsyCmbcService.merchantBaseInfoReg(appUserAndShopRequest.getUserId(),appUserAndShopRequest.getShopId());
            if(cmbcResponse.getCode()!=1){
                return CommonResponse.simpleResponse(-1,cmbcResponse.getMsg());
            }else{
                hsyUserDao.updateHxbsStatus(EnumHxbsStatus.PASS.getId(),cmbcResponse.getMsg(),appUserAndShopRequest.getUserId());
                if(appAuUser.getWeixinRate()!=null&&!"".equals(appAuUser.getWeixinRate())){//添加产品
                    CmbcResponse cmbcResponse1 = hsyCmbcService.merchantBindChannel(appUserAndShopRequest.getUserId(),appUserAndShopRequest.getShopId());
                    if(cmbcResponse1.getCode()==1){//开通产品成功
                        hsyCmbcDao.updateHxbUserById(EnumHxbsOpenProductStatus.PASS.getId(),cmbcResponse1.getMsg(),appUserAndShopRequest.getUserId());
                        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"入网成功");
                    }else{//开通产品失败
                        hsyCmbcDao.updateHxbUserById(EnumHxbsOpenProductStatus.UNPASS.getId(),cmbcResponse1.getMsg(),appUserAndShopRequest.getUserId());
                        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"开通产品失败");
                    }
                }else{
                    return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"入网成功");
                }
            }
        }

    }

    /**
     * 驳回充填
     * @param hsyMerchantAuditRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/reject",method = RequestMethod.POST)
    public CommonResponse reject(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){
        if(hsyMerchantAuditRequest.getUid()==null||hsyMerchantAuditRequest.getUid()<=0){
            return CommonResponse.simpleResponse(-1,"商户编码有误");
        }
        if(hsyMerchantAuditRequest.getId()==null||hsyMerchantAuditRequest.getId()<=0){
            return CommonResponse.simpleResponse(-1,"店铺编码有误");
        }
        AppAuUser appAuUser = hsyCmbcDao.selectByUserId(hsyMerchantAuditRequest.getUid());
        if(appAuUser.getHxbStatus()==null){
            return CommonResponse.simpleResponse(-1,"商户未通过审核，不能驳回充填");
        }
        if(appAuUser.getHxbStatus()==EnumHxbsStatus.PASS.getId()){
            return CommonResponse.simpleResponse(-1,"只有全部通道都入网失败的才可以驳回");
        }
        if(appAuUser!=null&&appAuUser.getAccountID()>0){
            accountService.delAcct(appAuUser.getAccountID());
        }
        hsyMerchantAuditRequest.setCheckErrorInfo("驳回充填");
        hsyMerchantAuditRequest.setStatus(AppConstant.SHOP_STATUS_REJECT);
        hsyMerchantAuditService.auditNotPass(hsyMerchantAuditRequest);
        hsyMerchantAuditService.stepChange(hsyMerchantAuditRequest.getUid());
        hsyMerchantAuditRequest.setStat(1);
        this.hsyMerchantAuditService.saveLog(super.getAdminUser().getUsername(),hsyMerchantAuditRequest.getId(),hsyMerchantAuditRequest.getCheckErrorInfo(),hsyMerchantAuditRequest.getStat());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"驳回充填成功");
    }
}
