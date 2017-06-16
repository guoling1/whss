package com.jkm.hss.controller.hsyMerchant;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.admin.helper.AdminUserSupporter;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.helper.MerchantConsts;
import com.jkm.hss.merchant.helper.SmPost;
import com.jkm.hss.merchant.enums.EnumStatus;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.notifier.service.SmsAuthService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.push.sevice.PushService;
import com.jkm.hsy.user.Enum.*;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.dao.HsyCmbcDao;
import com.jkm.hsy.user.dao.HsyMerchantAuditDao;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.entity.*;
import com.jkm.hsy.user.help.requestparam.AddWxChannelRequest;
import com.jkm.hsy.user.help.requestparam.CmbcResponse;
import com.jkm.hsy.user.help.requestparam.XmmsResponse;
import com.jkm.hsy.user.service.HsyCmbcService;
import com.jkm.hsy.user.service.HsyMerchantAuditService;
import com.jkm.hsy.user.service.UserChannelPolicyService;
import com.jkm.hsy.user.service.UserTradeRateService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private UserChannelPolicyService userChannelPolicyService;

    @Autowired
    private UserTradeRateService userTradeRateService;

    @ResponseBody
    @RequestMapping(value = "/throughAudit",method = RequestMethod.POST)
    public CommonResponse throughAudit(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){
        final HsyMerchantAuditResponse hsyMerchantAudit = this.hsyMerchantAuditService.selectById(hsyMerchantAuditRequest.getId());
        if (hsyMerchantAudit==null) {
            return CommonResponse.simpleResponse(-1, "商户不存在");
        }
        if ("".equals(hsyMerchantAuditRequest.getBranchCode())||hsyMerchantAuditRequest.getBranchCode()==null){
            return CommonResponse.simpleResponse(-1, "联行号不能为空");
        }
        List<UserTradeRate> userTradeRateList =  userTradeRateService.selectAllByUserId(hsyMerchantAuditRequest.getUid());
        if(userTradeRateList.size()==0){
            return CommonResponse.simpleResponse(-1, "商户费率为空");
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
        //入网
        merchantIn(hsyMerchantAuditRequest.getUid(),hsyMerchantAuditRequest.getId());
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
            String name = hsyMerchantAuditRequest.getName();
            String shopName="："+name+"，";
            Map map = new HashMap();
            map.put("name",shopName);
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
        AppBizShop appBizShop = hsyCmbcDao.selectByShopId(appUserAndShopRequest.getShopId());
        if(appBizShop.getStatus()!=AppConstant.SHOP_STATUS_NORMAL){
            return CommonResponse.simpleResponse(-1,"该商户未通过审核，不能重新入网");
        }
        Optional<UserChannelPolicy> userChannelPolicyOptional = userChannelPolicyService.selectByUserIdAndChannelTypeSign(appUserAndShopRequest.getUserId(),EnumPayChannelSign.SYJ_WECHAT.getId());
        if(userChannelPolicyOptional.isPresent()){
            if(userChannelPolicyOptional.get().getNetStatus()!=null&&userChannelPolicyOptional.get().getNetStatus()==EnumNetStatus.SUCCESS.getId()){
                if(userChannelPolicyOptional.get().getOpenProductStatus()!=null&&userChannelPolicyOptional.get().getNetStatus()==EnumOpenProductStatus.PASS.getId()){

                }else{
                    CmbcResponse cmbcResponse1 = hsyCmbcService.merchantBindChannel(appUserAndShopRequest.getUserId(),appUserAndShopRequest.getShopId());
                    if(cmbcResponse1.getCode()==1){
                        UserChannelPolicy openProduct = new UserChannelPolicy();
                        openProduct.setUserId(appUserAndShopRequest.getUserId());
                        openProduct.setOpenProductStatus(EnumOpenProductStatus.PASS.getId());
                        openProduct.setOpenProductMarks(cmbcResponse1.getMsg());
                        openProduct.setExchannelEventCode(cmbcResponse1.getResult());
                        userChannelPolicyService.updateHxOpenProduct(openProduct);
                    }else{
                        UserChannelPolicy openProduct = new UserChannelPolicy();
                        openProduct.setUserId(appUserAndShopRequest.getUserId());
                        openProduct.setOpenProductStatus(EnumOpenProductStatus.UNPASS.getId());
                        openProduct.setOpenProductMarks(cmbcResponse1.getMsg());
                        openProduct.setExchannelEventCode(cmbcResponse1.getResult());
                        userChannelPolicyService.updateHxOpenProduct(openProduct);
                    }
                }
            }else{
                CmbcResponse cmbcResponse = hsyCmbcService.merchantBaseInfoReg(appUserAndShopRequest.getUserId(),appUserAndShopRequest.getShopId());
                if(cmbcResponse.getCode()==1){
                    UserChannelPolicy netInfo = new UserChannelPolicy();
                    netInfo.setUserId(appUserAndShopRequest.getUserId());
                    netInfo.setNetStatus(EnumNetStatus.SUCCESS.getId());
                    netInfo.setNetMarks(cmbcResponse.getMsg());
                    netInfo.setExchannelCode(cmbcResponse.getResult());
                    userChannelPolicyService.updateHxNetInfo(netInfo);
                    CmbcResponse cmbcResponse1 = hsyCmbcService.merchantBindChannel(appUserAndShopRequest.getUserId(),appUserAndShopRequest.getShopId());
                    if(cmbcResponse1.getCode()==1){
                        UserChannelPolicy openProduct = new UserChannelPolicy();
                        openProduct.setUserId(appUserAndShopRequest.getUserId());
                        openProduct.setOpenProductStatus(EnumOpenProductStatus.PASS.getId());
                        openProduct.setOpenProductMarks(cmbcResponse1.getMsg());
                        openProduct.setExchannelEventCode(cmbcResponse1.getResult());
                        userChannelPolicyService.updateHxOpenProduct(openProduct);
                    }else{
                        UserChannelPolicy openProduct = new UserChannelPolicy();
                        openProduct.setUserId(appUserAndShopRequest.getUserId());
                        openProduct.setOpenProductStatus(EnumOpenProductStatus.UNPASS.getId());
                        openProduct.setOpenProductMarks(cmbcResponse1.getMsg());
                        openProduct.setExchannelEventCode(cmbcResponse1.getResult());
                        userChannelPolicyService.updateHxOpenProduct(openProduct);
                    }
                }else{
                    UserChannelPolicy netInfo = new UserChannelPolicy();
                    netInfo.setUserId(appUserAndShopRequest.getUserId());
                    netInfo.setNetStatus(EnumNetStatus.FAIL.getId());
                    netInfo.setNetMarks(cmbcResponse.getMsg());
                    netInfo.setExchannelCode(cmbcResponse.getResult());
                    userChannelPolicyService.updateHxNetInfo(netInfo);
                }
            }
        }
        List<Integer> list=new ArrayList<Integer>(){
            {
                add(EnumPayChannelSign.XMMS_WECHAT_T1.getId());
                add(EnumPayChannelSign.XMMS_ALIPAY_T1.getId());
                add(EnumPayChannelSign.XMMS_WECHAT_D0.getId());
                add(EnumPayChannelSign.XMMS_ALIPAY_D0.getId());
            }
        };
        for(int i=0;i<list.size();i++){
            Optional<UserChannelPolicy> ucp = userChannelPolicyService.selectByUserIdAndChannelTypeSign(appUserAndShopRequest.getUserId(),list.get(i));
            if(ucp.isPresent()){
                if(ucp.get().getNetStatus()!=null&&ucp.get().getNetStatus()!=EnumNetStatus.SUCCESS.getId()&&ucp.get().getNetStatus()!=EnumNetStatus.HANDLING.getId()){
                    XmmsResponse.BaseResponse baseResponse= hsyCmbcService.merchantIn(appUserAndShopRequest.getUserId(),appUserAndShopRequest.getShopId(),list.get(i));
                    if(baseResponse.getCode()==1){
                        UserChannelPolicy netInfo = new UserChannelPolicy();
                        netInfo.setUserId(appUserAndShopRequest.getUserId());
                        if((EnumXmmsStatus.HANDLING.getId()).equals(baseResponse.getResult().getStatus())){
                            netInfo.setNetStatus(EnumNetStatus.HANDLING.getId());
                            netInfo.setNetMarks(baseResponse.getResult().getMsg());
                            netInfo.setOpenProductStatus(EnumOpenProductStatus.HANDLING.getId());
                            netInfo.setOpenProductMarks(baseResponse.getResult().getMsg());
                        }
                        if((EnumXmmsStatus.FAIL.getId()).equals(baseResponse.getResult().getStatus())){
                            netInfo.setNetStatus(EnumNetStatus.FAIL.getId());
                            netInfo.setNetMarks(baseResponse.getResult().getMsg());
                            netInfo.setOpenProductStatus(EnumOpenProductStatus.UNPASS.getId());
                            netInfo.setOpenProductMarks(baseResponse.getResult().getMsg());
                        }
                        netInfo.setChannelTypeSign(list.get(i));
                        userChannelPolicyService.updateByUserIdAndChannelTypeSign(netInfo);
                    }else{
                        UserChannelPolicy netInfo = new UserChannelPolicy();
                        netInfo.setUserId(appUserAndShopRequest.getUserId());
                        netInfo.setNetStatus(EnumNetStatus.FAIL.getId());
                        netInfo.setNetMarks(baseResponse.getMsg());
                        netInfo.setOpenProductStatus(EnumOpenProductStatus.UNPASS.getId());
                        netInfo.setOpenProductMarks(baseResponse.getMsg());
                        netInfo.setChannelTypeSign(list.get(i));
                        userChannelPolicyService.updateByUserIdAndChannelTypeSign(netInfo);
                    }
                }
            }
        }

        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"重新入网成功");
    }

    private void merchantIn(long userId,long shopId){
        CmbcResponse cmbcResponse = hsyCmbcService.merchantBaseInfoReg(userId,shopId);
        if(cmbcResponse.getCode()==1){
            UserChannelPolicy netInfo = new UserChannelPolicy();
            netInfo.setUserId(userId);
            netInfo.setNetStatus(EnumNetStatus.SUCCESS.getId());
            netInfo.setNetMarks(cmbcResponse.getMsg());
            netInfo.setExchannelCode(cmbcResponse.getResult());
            userChannelPolicyService.updateHxNetInfo(netInfo);
            CmbcResponse cmbcResponse1 = hsyCmbcService.merchantBindChannel(userId,shopId);
            if(cmbcResponse1.getCode()==1){//开通产品成功
                UserChannelPolicy openProduct = new UserChannelPolicy();
                openProduct.setUserId(userId);
                openProduct.setOpenProductStatus(EnumOpenProductStatus.PASS.getId());
                openProduct.setOpenProductMarks(cmbcResponse1.getMsg());
                openProduct.setExchannelEventCode(cmbcResponse1.getResult());
                userChannelPolicyService.updateHxOpenProduct(openProduct);
            }else{//开通产品失败
                UserChannelPolicy openProduct = new UserChannelPolicy();
                openProduct.setUserId(userId);
                openProduct.setOpenProductStatus(EnumOpenProductStatus.UNPASS.getId());
                openProduct.setOpenProductMarks(cmbcResponse1.getMsg());
                openProduct.setExchannelEventCode(cmbcResponse1.getResult());
                userChannelPolicyService.updateHxOpenProduct(openProduct);
            }

        }else{
            UserChannelPolicy netInfo = new UserChannelPolicy();
            netInfo.setUserId(userId);
            netInfo.setNetStatus(EnumNetStatus.FAIL.getId());
            netInfo.setNetMarks(cmbcResponse.getMsg());
            netInfo.setExchannelCode(cmbcResponse.getResult());
            userChannelPolicyService.updateHxNetInfo(netInfo);
        }

        XmmsResponse xmmsResponse = hsyCmbcService.merchantIn(userId,shopId);
        XmmsResponse.BaseResponse wxT1 = xmmsResponse.getWxT1();
        if(wxT1.getCode()==1){
            UserChannelPolicy netInfo = new UserChannelPolicy();
            netInfo.setUserId(userId);
            if((EnumXmmsStatus.HANDLING.getId()).equals(wxT1.getResult().getStatus())){
                netInfo.setNetStatus(EnumNetStatus.HANDLING.getId());
                netInfo.setNetMarks(wxT1.getResult().getMsg());
                netInfo.setOpenProductStatus(EnumOpenProductStatus.HANDLING.getId());
                netInfo.setOpenProductMarks(wxT1.getResult().getMsg());
            }
            if((EnumXmmsStatus.FAIL.getId()).equals(wxT1.getResult().getStatus())){
                netInfo.setNetStatus(EnumNetStatus.FAIL.getId());
                netInfo.setNetMarks(wxT1.getResult().getMsg());
                netInfo.setOpenProductStatus(EnumOpenProductStatus.UNPASS.getId());
                netInfo.setOpenProductMarks(wxT1.getResult().getMsg());
            }
            netInfo.setChannelTypeSign(EnumPayChannelSign.XMMS_WECHAT_T1.getId());
            userChannelPolicyService.updateByUserIdAndChannelTypeSign(netInfo);
        }else{
            UserChannelPolicy netInfo = new UserChannelPolicy();
            netInfo.setUserId(userId);
            netInfo.setNetStatus(EnumNetStatus.FAIL.getId());
            netInfo.setNetMarks(wxT1.getMsg());
            netInfo.setOpenProductStatus(EnumOpenProductStatus.UNPASS.getId());
            netInfo.setOpenProductMarks(wxT1.getMsg());
            netInfo.setChannelTypeSign(EnumPayChannelSign.XMMS_WECHAT_T1.getId());
            userChannelPolicyService.updateByUserIdAndChannelTypeSign(netInfo);
        }

        XmmsResponse.BaseResponse zfbT1 = xmmsResponse.getZfbT1();
        if(zfbT1.getCode()==1){
            UserChannelPolicy netInfo = new UserChannelPolicy();
            netInfo.setUserId(userId);
            if((EnumXmmsStatus.HANDLING.getId()).equals(zfbT1.getResult().getStatus())){
                netInfo.setNetStatus(EnumNetStatus.HANDLING.getId());
                netInfo.setNetMarks(zfbT1.getResult().getMsg());
                netInfo.setOpenProductStatus(EnumOpenProductStatus.HANDLING.getId());
                netInfo.setOpenProductMarks(zfbT1.getResult().getMsg());
            }
            if((EnumXmmsStatus.FAIL.getId()).equals(zfbT1.getResult().getStatus())){
                netInfo.setNetStatus(EnumNetStatus.FAIL.getId());
                netInfo.setNetMarks(zfbT1.getResult().getMsg());
                netInfo.setOpenProductStatus(EnumOpenProductStatus.UNPASS.getId());
                netInfo.setOpenProductMarks(zfbT1.getResult().getMsg());
            }
            netInfo.setChannelTypeSign(EnumPayChannelSign.XMMS_ALIPAY_T1.getId());
            userChannelPolicyService.updateByUserIdAndChannelTypeSign(netInfo);
        }else{
            UserChannelPolicy netInfo = new UserChannelPolicy();
            netInfo.setUserId(userId);
            netInfo.setNetStatus(EnumNetStatus.FAIL.getId());
            netInfo.setNetMarks(zfbT1.getMsg());
            netInfo.setOpenProductStatus(EnumOpenProductStatus.UNPASS.getId());
            netInfo.setOpenProductMarks(zfbT1.getMsg());
            netInfo.setChannelTypeSign(EnumPayChannelSign.XMMS_ALIPAY_T1.getId());
            userChannelPolicyService.updateByUserIdAndChannelTypeSign(netInfo);
        }

        XmmsResponse.BaseResponse wxD0 = xmmsResponse.getWxD0();
        if(wxD0.getCode()==1){
            UserChannelPolicy netInfo = new UserChannelPolicy();
            netInfo.setUserId(userId);
            if((EnumXmmsStatus.HANDLING.getId()).equals(wxD0.getResult().getStatus())){
                netInfo.setNetStatus(EnumNetStatus.HANDLING.getId());
                netInfo.setNetMarks(wxD0.getResult().getMsg());
                netInfo.setOpenProductStatus(EnumOpenProductStatus.HANDLING.getId());
                netInfo.setOpenProductMarks(wxD0.getResult().getMsg());
            }
            if((EnumXmmsStatus.FAIL.getId()).equals(wxD0.getResult().getStatus())){
                netInfo.setNetStatus(EnumNetStatus.FAIL.getId());
                netInfo.setNetMarks(wxD0.getResult().getMsg());
                netInfo.setOpenProductStatus(EnumOpenProductStatus.UNPASS.getId());
                netInfo.setOpenProductMarks(wxD0.getResult().getMsg());
            }
            netInfo.setChannelTypeSign(EnumPayChannelSign.XMMS_WECHAT_D0.getId());
            userChannelPolicyService.updateByUserIdAndChannelTypeSign(netInfo);
        }else{
            UserChannelPolicy netInfo = new UserChannelPolicy();
            netInfo.setUserId(userId);
            netInfo.setNetStatus(EnumNetStatus.FAIL.getId());
            netInfo.setNetMarks(wxD0.getMsg());
            netInfo.setOpenProductStatus(EnumOpenProductStatus.UNPASS.getId());
            netInfo.setOpenProductMarks(wxD0.getMsg());
            netInfo.setChannelTypeSign(EnumPayChannelSign.XMMS_WECHAT_D0.getId());
            userChannelPolicyService.updateByUserIdAndChannelTypeSign(netInfo);
        }

        XmmsResponse.BaseResponse zfbD0 = xmmsResponse.getZfbD0();
        if(zfbD0.getCode()==1){
            UserChannelPolicy netInfo = new UserChannelPolicy();
            netInfo.setUserId(userId);
            if((EnumXmmsStatus.HANDLING.getId()).equals(zfbD0.getResult().getStatus())){
                netInfo.setNetStatus(EnumNetStatus.HANDLING.getId());
                netInfo.setNetMarks(zfbD0.getResult().getMsg());
                netInfo.setOpenProductStatus(EnumOpenProductStatus.HANDLING.getId());
                netInfo.setOpenProductMarks(zfbD0.getResult().getMsg());
            }
            if((EnumXmmsStatus.FAIL.getId()).equals(zfbD0.getResult().getStatus())){
                netInfo.setNetStatus(EnumNetStatus.FAIL.getId());
                netInfo.setNetMarks(zfbD0.getResult().getMsg());
                netInfo.setOpenProductStatus(EnumOpenProductStatus.UNPASS.getId());
                netInfo.setOpenProductMarks(zfbD0.getResult().getMsg());
            }
            netInfo.setChannelTypeSign(EnumPayChannelSign.XMMS_ALIPAY_D0.getId());
            userChannelPolicyService.updateByUserIdAndChannelTypeSign(netInfo);
        }else{
            UserChannelPolicy netInfo = new UserChannelPolicy();
            netInfo.setUserId(userId);
            netInfo.setNetStatus(EnumNetStatus.FAIL.getId());
            netInfo.setNetMarks(zfbD0.getMsg());
            netInfo.setOpenProductStatus(EnumOpenProductStatus.UNPASS.getId());
            netInfo.setOpenProductMarks(zfbD0.getMsg());
            netInfo.setChannelTypeSign(EnumPayChannelSign.XMMS_ALIPAY_D0.getId());
            userChannelPolicyService.updateByUserIdAndChannelTypeSign(netInfo);
        }
    }


    /**
     * 添加微信官方通道
     * @param addWxChannelRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addWxChannel",method = RequestMethod.POST)
    public CommonResponse addWxChannel(@RequestBody AddWxChannelRequest addWxChannelRequest){
        Optional<UserChannelPolicy> userChannelPolicyOptional =  userChannelPolicyService.selectByUserIdAndChannelTypeSign(addWxChannelRequest.getUserId(),EnumPayChannelSign.WECHAT_PAY.getId());
        if(userChannelPolicyOptional.isPresent()){
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"此通道已存在");
        }
        UserChannelPolicy userChannelPolicy = new UserChannelPolicy();
        userChannelPolicy.setUserId(addWxChannelRequest.getUserId());
        userChannelPolicy.setChannelName("微信官方");
        userChannelPolicy.setChannelTypeSign(EnumPayChannelSign.WECHAT_PAY.getId());
        userChannelPolicy.setPolicyType(EnumPolicyType.WECHAT.getId());
        userChannelPolicy.setSettleType(EnumPayChannelSign.WECHAT_PAY.getSettleType().getType());
        userChannelPolicy.setNetStatus(EnumNetStatus.UNENT.getId());
        userChannelPolicy.setOpenProductStatus(0);
        userChannelPolicy.setExchannelCode(addWxChannelRequest.getExchannelCode());
        userChannelPolicy.setAppId(addWxChannelRequest.getAppId());
        userChannelPolicy.setStatus(EnumStatus.NORMAL.getId());
        userChannelPolicyService.insert(userChannelPolicy);
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"添加成功");
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
        if(appBizShop.getLicenceNO()!=null&&"".equals(appBizShop.getLicenceNO())){
            paramsMap.put("businessLicense",appBizShop.getLicenceNO());//证据编号
            paramsMap.put("businessLicenseType","NATIONAL_LEGAL");//证件类型

        }else{
            paramsMap.put("businessLicense","");//证据编号
            paramsMap.put("businessLicenseType","");//证件类型
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
