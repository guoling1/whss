package com.jkm.hss.controller.hsyMerchant;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.push.sevice.PushService;
import com.jkm.hsy.user.Enum.EnumHxbsOpenProductStatus;
import com.jkm.hsy.user.Enum.EnumHxbsStatus;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.dao.HsyCmbcDao;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.entity.*;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
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
        CmbcResponse cmbcResponse = hsyCmbcService.merchantBaseInfoReg(hsyMerchantAuditRequest.getUid(),hsyMerchantAuditRequest.getId());
        if(cmbcResponse.getCode()==1){
            hsyUserDao.updateHxbsStatus(EnumHxbsStatus.PASS.getId(),cmbcResponse.getMsg(),hsyMerchantAuditRequest.getUid());
            //入驻成功再开通产品
            if(hsyMerchantAudit.getWeixinRate()!=null&&!"".equals(hsyMerchantAudit.getWeixinRate())){//添加产品
                CmbcResponse cmbcResponse1 = hsyCmbcService.merchantBindChannel(hsyMerchantAuditRequest.getUid(),hsyMerchantAuditRequest.getId());
                if(cmbcResponse1.getCode()==1){//开通产品成功
                    hsyCmbcDao.updateHxbUserById(EnumHxbsOpenProductStatus.PASS.getId(),hsyMerchantAuditRequest.getUid());
                }else{//开通产品失败
                    hsyCmbcDao.updateHxbUserById(EnumHxbsOpenProductStatus.UNPASS.getId(),hsyMerchantAuditRequest.getUid());
                }
            }
        }else{
            hsyUserDao.updateHxbsStatus(EnumHxbsStatus.UNPASS.getId(),cmbcResponse.getMsg(),hsyMerchantAuditRequest.getUid());
        }
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
        return CommonResponse.simpleResponse(-1,"审核未通过");

    }

    /**
     * 重新入网
     * @param appUserAndShopRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/reenter",method = RequestMethod.POST)
    public CommonResponse reenter(@RequestBody final AppUserAndShopRequest appUserAndShopRequest){
        AppBizShop appBizShop = hsyCmbcDao.selectByShopId(appUserAndShopRequest.getShopId());
        if(appBizShop.getStatus()!=AppConstant.SHOP_STATUS_NORMAL){
            return CommonResponse.simpleResponse(-1,"该商户未通过审核，不能重新入网");
        }
        AppAuUser appAuUser = hsyCmbcDao.selectByUserId(appUserAndShopRequest.getUserId());
        if(appAuUser.getHxbStatus()!=null&&appAuUser.getHxbStatus()==EnumHxbsStatus.PASS.getId()){
            return CommonResponse.simpleResponse(-1,"该商户已经入网，不能重复入网");
        }
        CmbcResponse cmbcResponse = hsyCmbcService.merchantBaseInfoReg(appUserAndShopRequest.getUserId(),appUserAndShopRequest.getShopId());
        if(cmbcResponse.getCode()!=1){
            return CommonResponse.simpleResponse(-1,cmbcResponse.getMsg());
        }
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"入网成功");
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
        if(appAuUser.getAccountID()>0){
            accountService.delAcct(appAuUser.getAccountID());
        }
        hsyMerchantAuditRequest.setStatus(AppConstant.SHOP_STATUS_REJECT_FILL);
        hsyMerchantAuditService.auditPass(hsyMerchantAuditRequest);
        hsyMerchantAuditService.stepChange(hsyMerchantAuditRequest.getUid());
        return CommonResponse.simpleResponse(-1,"驳回充填成功");
    }
}
