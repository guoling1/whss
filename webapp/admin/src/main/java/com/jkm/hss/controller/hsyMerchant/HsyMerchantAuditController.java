package com.jkm.hss.controller.hsyMerchant;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.helper.MerchantConsts;
import com.jkm.hss.merchant.helper.SmPost;
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
import com.jkm.hsy.user.dao.HsyMerchantAuditDao;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.entity.*;
import com.jkm.hsy.user.help.requestparam.CmbcResponse;
import com.jkm.hsy.user.service.HsyCmbcService;
import com.jkm.hsy.user.service.HsyMerchantAuditService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
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


    @Autowired
    private HsyMerchantAuditDao hsyMerchantAuditDao;
    /**
     *  修改入网信息
     * @param appUserAndShopRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/modify",method = RequestMethod.POST)
    public CommonResponse modify(@RequestBody final AppUserAndShopRequest appUserAndShopRequest){

        final long userId = appUserAndShopRequest.getUserId();
        final long shopId = appUserAndShopRequest.getShopId();
        AppAuUser appAuUser = hsyCmbcDao.selectByUserId(userId);
        AppBizShop appBizShop = hsyCmbcDao.selectByShopId(shopId);
        AppBizCard appBizCard = hsyCmbcDao.selectByCardId(shopId);

        CmbcResponse cmbcResponse = new CmbcResponse();
        Map<String, String> paramsMap = new HashMap<String, String>();
        //商户信息-MerchantInfo
        paramsMap.put("merchantNo", appAuUser.getGlobalID());//商户编号
        paramsMap.put("fullName", appBizShop.getName());//商户全称
        paramsMap.put("shortName", appBizShop.getShortName());//商户简称
        paramsMap.put("servicePhone","4006226233");//客服电话
        if(appBizShop.getLicenceNO()==null||"".equals(appBizShop.getLicenceNO())){
            paramsMap.put("businessLicense","");//证据编号
            paramsMap.put("businessLicenseType","");//证件类型
        }else{
            paramsMap.put("businessLicense",appBizShop.getLicenceNO());//证据编号
            paramsMap.put("businessLicenseType","NATIONAL_LEGAL");//证件类型
        }


        //联系人信息-contactInfo
        paramsMap.put("contactName",appAuUser.getRealname());//联系人名称
        paramsMap.put("contactPhone",appAuUser.getCellphone());//联系人手机号
        paramsMap.put("contactPersonType","LEGAL_PERSON");//联系人类型
        paramsMap.put("contactIdCard",appAuUser.getIdcard());//身份证号
        paramsMap.put("contactEmail","");//邮箱
        //结算卡信息-bankCardInfo
        paramsMap.put("bankAccountNo",appBizCard.getCardNO());//银行账号
        paramsMap.put("bankAccountName",appBizCard.getCardAccountName());//开户名称
        paramsMap.put("idCard",appBizCard.getIdcardNO());//开户身份证号
        if(appBizShop.getIsPublic()==1){//对公
            paramsMap.put("bankAccountType","2");//账户类型
        }else{
            paramsMap.put("bankAccountType","1");//账户类型
        }
        paramsMap.put("bankAccountLineNo",appBizCard.getBranchCode());//银行联行号
        paramsMap.put("bankAccountAddress",appBizCard.getBankAddress());//开户行地址
        //联系人地址信息-addressInfo
        HsyMerchantAuditResponse district = hsyMerchantAuditDao.getCode(appBizShop.getDistrictCode());
        paramsMap.put("district",district.getAName());//商户地址区
        HsyMerchantAuditResponse city = hsyMerchantAuditDao.getCode(district.getParentCode());
        paramsMap.put("city",city.getAName());//商户地址市
        if("110000,120000,310000,500000".contains(city.getCode())){
            paramsMap.put("province",city.getAName().replace("市",""));//商户地址省
        }else{
            HsyMerchantAuditResponse province = hsyMerchantAuditDao.getCode(city.getParentCode());
            paramsMap.put("province",province.getAName());//商户地址省
        }
        paramsMap.put("address",appBizShop.getAddress());//详细地址
        log.info("民生银行商户基础信息注册参数为："+ JSONObject.fromObject(paramsMap).toString());
        String result = SmPost.post("http://pay.qianbaojiajia.com/syj/merchant/modify", paramsMap);
        if (result != null && !"".equals(result)) {
            JSONObject jo = JSONObject.fromObject(result);
            log.info("民生银行商户基础信息注册返回结果为："+jo.toString());
            cmbcResponse.setCode(jo.getInt("code"));
            cmbcResponse.setMsg(jo.getString("msg"));
        } else {//超时
            cmbcResponse.setCode(-1);
            cmbcResponse.setMsg("进件超时");
        }
        return CommonResponse.simpleResponse(1,"SUCCESS");
    }
}
