package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jkm.base.common.enums.EnumGlobalIDPro;
import com.jkm.base.common.enums.EnumGlobalIDType;
import com.jkm.base.common.util.DateUtil;
import com.jkm.base.common.util.GlobalID;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.entity.OemInfo;
import com.jkm.hss.dealer.enums.EnumInviteBtn;
import com.jkm.hss.dealer.enums.EnumRecommendBtn;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.dealer.service.OemInfoService;
import com.jkm.hss.merchant.constant.AppConstant;
import com.jkm.hss.merchant.dao.AppAuTokenDao;
import com.jkm.hss.merchant.entity.*;
import com.jkm.hss.merchant.enums.*;
import com.jkm.hss.merchant.exception.ApiHandleException;
import com.jkm.hss.merchant.exception.ResultCode;
import com.jkm.hss.merchant.helper.AppParam;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.helper.request.AppLoginRequest;
import com.jkm.hss.merchant.helper.request.AppRegisterRequest;
import com.jkm.hss.merchant.helper.request.LoginCodeRequest;
import com.jkm.hss.merchant.service.*;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.notifier.service.SmsAuthService;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.entity.PartnerRuleSetting;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.enums.EnumUpGradeType;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.PartnerRuleSettingService;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import com.jkm.hss.product.servcie.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by xingliujie on 2017/7/24.
 */
@Slf4j
@Service("appMerchantInfoService")
public class AppMerchantInfoServiceImpl implements AppMerchantInfoService {
    @Autowired
    private OemInfoService oemInfoService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private SmsAuthService smsAuthService;
    @Autowired
    private SendMessageService sendMessageService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private DealerChannelRateService dealerChannelRateService;
    @Autowired
    private MerchantChannelRateService merchantChannelRateService;
    @Autowired
    private PartnerRuleSettingService partnerRuleSettingService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ProductChannelDetailService productChannelDetailService;
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private AppAuTokenDao appAuTokenDao;
    @Autowired
    private DealerRecommendService dealerRecommendService;

    /**
     * 获取验证码 HSS001005
     * @param dataParam
     * @param appParam
     * @return
     * @throws ApiHandleException
     */
    @Override
    public String getCode(String dataParam, AppParam appParam) throws ApiHandleException {
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        LoginCodeRequest loginCodeRequest=null;
        try{
            loginCodeRequest=gson.fromJson(dataParam, LoginCodeRequest.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }
        final String mobile = loginCodeRequest.getMobile();
        if (StringUtils.isBlank(mobile)) {
            throw new ApiHandleException(ResultCode.PARAM_LACK,"手机号");
        }
        if (!ValidateUtils.isMobile(mobile)) {
            throw new ApiHandleException(ResultCode.CELLPHONE_NOT_CORRECT_FORMAT);
        }
        if(loginCodeRequest.getType()==1){
            long oemId = 0;
            if(loginCodeRequest.getOemNo()!=null&&!"".equals(loginCodeRequest.getOemNo())){
                Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(loginCodeRequest.getOemNo());
                if(!oemInfoOptional.isPresent()){
                    throw new ApiHandleException(ResultCode.OEM_NOT_EXSIT);
                }
                oemId = oemInfoOptional.get().getDealerId();
            }else{
                List<MerchantInfo> merchantInfoList = merchantInfoService.selectByMobile(MerchantSupport.encryptMobile(mobile));
                if(merchantInfoList.size()>0){
                    for(int i=0;i<merchantInfoList.size();i++){
                        if(merchantInfoList.get(i).getOemId()==0){
                            throw new ApiHandleException(ResultCode.CELLPHONE_HAS_BEEN_REGISTERED);
                        }else{
                            Optional<OemInfo> oemInfoOptional1 = oemInfoService.selectOemInfoByDealerId(merchantInfoList.get(i).getOemId());
                            if(WxConstants.APP_ID.equals(oemInfoOptional1.get().getAppId())){
                                throw new ApiHandleException(ResultCode.CELLPHONE_HAS_BEEN_REGISTERED);
                            }
                        }
                    }
                }

            }
            Optional<MerchantInfo> userInfoOptional = merchantInfoService.selectByMobileAndOemId(MerchantSupport.encryptMobile(mobile),oemId);
            if(userInfoOptional.isPresent()){
                throw new ApiHandleException(ResultCode.CELLPHONE_HAS_BEEN_REGISTERED);
            }
            final Pair<Integer, String> verifyCode = this.smsAuthService.getVerifyCode(mobile, EnumVerificationCodeType.REGISTER_MERCHANT);
            if (1 == verifyCode.getLeft()) {
                final Map<String, String> params = ImmutableMap.of("code", verifyCode.getRight());
                this.sendMessageService.sendMessage(SendMessageParams.builder()
                        .mobile(mobile)
                        .uid("")
                        .data(params)
                        .userType(EnumUserType.BACKGROUND_USER)
                        .noticeType(EnumNoticeType.REGISTER_MERCHANT)
                        .build()
                );
                return "";

            }
            throw new ApiHandleException(verifyCode.getRight());
        }
        if(loginCodeRequest.getType()==2){
            final Pair<Integer, String> verifyCode = this.smsAuthService.getVerifyCode(mobile, EnumVerificationCodeType.LOGIN_MERCHANT);
            if (1 == verifyCode.getLeft()) {
                final Map<String, String> params = ImmutableMap.of("code", verifyCode.getRight());
                this.sendMessageService.sendMessage(SendMessageParams.builder()
                        .mobile(mobile)
                        .uid("")
                        .data(params)
                        .userType(EnumUserType.BACKGROUND_USER)
                        .noticeType(EnumNoticeType.LOGIN_MERCHANT)
                        .build()
                );
                return "";
            }
            throw new ApiHandleException(verifyCode.getRight());
        }
        return "";
    }

    /**
     * 商户注册 HSS001006
     * @param dataParam
     * @param appParam
     * @return
     * @throws ApiHandleException
     */
    @Override
    public String register(String dataParam, AppParam appParam) throws ApiHandleException {
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        AppRegisterRequest appRegisterRequest=null;
        try{
            appRegisterRequest=gson.fromJson(dataParam, AppRegisterRequest.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }
        long oemId = 0;
        final String mobile = appRegisterRequest.getMobile();
        final String verifyCode = appRegisterRequest.getCode();
        final String inviteCode = appRegisterRequest.getInviteCode();
        if (StringUtils.isBlank(mobile)) {
            throw new ApiHandleException(ResultCode.PARAM_LACK,"手机号");
        }
        if (!ValidateUtils.isMobile(mobile)) {
            throw new ApiHandleException(ResultCode.CELLPHONE_NOT_CORRECT_FORMAT);
        }
        if (StringUtils.isBlank(verifyCode)) {
            throw new ApiHandleException(ResultCode.PARAM_LACK,"验证码");
        }
        if (!ValidateUtils.verifyCodeCheck(verifyCode)) {
            throw new ApiHandleException(ResultCode.VERIFICATIONCODE_ERROR);
        }
        if (StringUtils.isBlank(inviteCode)) {
            throw new ApiHandleException(ResultCode.PARAM_LACK,"邀请码");
        }
        if (!StringUtils.isBlank(inviteCode)) {
            if (inviteCode.length()!=6&&inviteCode.length()!=11) {
                throw new ApiHandleException(ResultCode.INVITECODE_NOT_EXSIT);
            }
            if(inviteCode.length()==6){
                Optional<Dealer> dealerOptional = dealerService.getDealerByInviteCode(inviteCode);
                if(!dealerOptional.isPresent()){
                    throw new ApiHandleException(ResultCode.FRIEND_NOT_OPEN_SHARE);
                }
                if(StringUtils.isBlank(dealerOptional.get().getInviteCode())){
                    throw new ApiHandleException(ResultCode.FRIEND_NOT_OPEN_SHARE);
                }
                if(dealerOptional.get().getInviteBtn()== EnumInviteBtn.OFF.getId()){
                    throw new ApiHandleException(ResultCode.FRIEND_NOT_OPEN_SHARE);
                }
                if(dealerOptional.get().getRecommendBtn()== EnumRecommendBtn.OFF.getId()){
                    throw new ApiHandleException(ResultCode.INVITECODE_NOT_EXSIT);
                }
                oemId = dealerOptional.get().getOemId();
            }else{
                if(inviteCode.equals(mobile)){
                    throw new ApiHandleException(ResultCode.CANT_NOT_SHARE_TO_SELF);
                }
                Optional<MerchantInfo> miOptional = merchantInfoService.selectByMobileAndOemId(MerchantSupport.encryptMobile(inviteCode),oemId);
                if(!miOptional.isPresent()){
                    throw new ApiHandleException(ResultCode.INVITECODE_NOT_EXSIT);
                }
                if(miOptional.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&miOptional.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
                    throw new ApiHandleException(ResultCode.INVITECODE_NOT_EXSIT);
                }
                if(miOptional.get().getIsUpgrade()== EnumIsUpgrade.CANNOTUPGRADE.getId()){
                    throw new ApiHandleException(ResultCode.INVITECODE_NOT_EXSIT);
                }
                oemId = miOptional.get().getOemId();
            }
        }
        final Pair<Integer, String> checkResult =
                this.smsAuthService.checkVerifyCode(mobile, verifyCode, EnumVerificationCodeType.REGISTER_MERCHANT);
        if (1 != checkResult.getLeft()) {
            throw new ApiHandleException(checkResult.getRight());
        }
        Optional<Product> productOptional = productService.selectByType(EnumProductType.HSS.getId());
        if(!productOptional.isPresent()){
            throw new ApiHandleException(ResultCode.PRODUCT_NOT_EXIST);
        }
        long productId = productOptional.get().getId();
        Optional<MerchantInfo> merchantInfoOptional1 = merchantInfoService.selectByMobileAndOemId(MerchantSupport.encryptMobile(mobile),oemId);
        if(merchantInfoOptional1.isPresent()){
            throw new ApiHandleException(ResultCode.CELLPHONE_HAS_BEEN_REGISTERED);
        }else{
            MerchantInfo mi = new MerchantInfo();
            mi.setStatus(EnumMerchantStatus.INIT.getId());
            mi.setMobile(MerchantSupport.encryptMobile(mobile));
            mi.setMdMobile(MerchantSupport.passwordDigest(mobile,"JKM"));
            mi.setProductId(productId);
            if(inviteCode.length()==6){
                mi.setSource(EnumSource.DEALERRECOMMEND.getId());
                Optional<Dealer> dealerOptional = dealerService.getDealerByInviteCode(inviteCode);
                if(!dealerOptional.isPresent()){
                    throw new ApiHandleException(ResultCode.INVITECODE_NOT_EXSIT);
                }
                List<DealerChannelRate> dealerChannelRateList = dealerChannelRateService.selectByDealerIdAndProductId(dealerOptional.get().getId(),productId);
                if(dealerChannelRateList.size()<=0){
                    throw new ApiHandleException(ResultCode.DEALER_RATE_ERROR);
                }
                if(dealerOptional.get().getLevel()== EnumDealerLevel.FIRST.getId()){
                    mi.setDealerId(dealerOptional.get().getId());
                    mi.setFirstDealerId(dealerOptional.get().getId());
                    mi.setSecondDealerId(0);
                }
                if(dealerOptional.get().getLevel()==EnumDealerLevel.SECOND.getId()){
                    mi.setDealerId(dealerOptional.get().getId());
                    mi.setFirstDealerId(dealerOptional.get().getFirstLevelDealerId());
                    mi.setSecondDealerId(dealerOptional.get().getId());
                }
                mi.setFirstMerchantId(0);
                mi.setSecondMerchantId(0);
                mi.setAccountId(0);
                mi.setLevel(EnumUpGradeType.COMMON.getId());
                mi.setHierarchy(0);
                mi.setIsUpgrade(EnumIsUpgrade.CANUPGRADE.getId());
                mi.setOemId(oemId);
                merchantInfoService.regByWx(mi);
                //初始化费率
                for(int i=0;i<dealerChannelRateList.size();i++){
                    MerchantChannelRate merchantChannelRate = new MerchantChannelRate();
                    merchantChannelRate.setMerchantId(mi.getId());
                    merchantChannelRate.setProductId(productId);
                    merchantChannelRate.setMarkCode(mi.getMarkCode());
                    merchantChannelRate.setSysType(EnumProductType.HSS.getId());
                    merchantChannelRate.setChannelTypeSign(dealerChannelRateList.get(i).getChannelTypeSign());
                    merchantChannelRate.setMerchantBalanceType(dealerChannelRateList.get(i).getDealerBalanceType());

                    if(mi.getIsUpgrade()==EnumIsUpgrade.CANUPGRADE.getId()){
                        Optional<PartnerRuleSetting> partnerRuleSettingOptional = partnerRuleSettingService.selectByProductIdAndChannelTypeSign(productId,dealerChannelRateList.get(i).getChannelTypeSign());
                        if(partnerRuleSettingOptional.isPresent()&&partnerRuleSettingOptional.get().getCommonRate()!=null){
                            merchantChannelRate.setMerchantPayRate(partnerRuleSettingOptional.get().getCommonRate());
                        }else{
                            merchantChannelRate.setMerchantPayRate(dealerChannelRateList.get(i).getDealerMerchantPayRate());
                        }
                    }else{
                        merchantChannelRate.setMerchantPayRate(dealerChannelRateList.get(i).getDealerMerchantPayRate());
                    }
                    merchantChannelRate.setMerchantWithdrawFee(dealerChannelRateList.get(i).getDealerMerchantWithdrawFee());
                    Optional<BasicChannel> basicChannelOptionalTemp = basicChannelService.selectByChannelTypeSign(dealerChannelRateList.get(i).getChannelTypeSign());
                    if(basicChannelOptionalTemp.get().getIsNeed()==1){//需要入网
                        merchantChannelRate.setEnterNet(EnumEnterNet.UNENT.getId());
                    }else{
                        merchantChannelRate.setEnterNet(EnumEnterNet.UNSUPPORT.getId());
                    }
                    merchantChannelRate.setChannelCompany(basicChannelOptionalTemp.get().getChannelCompany());
                    merchantChannelRate.setThirdCompany(basicChannelOptionalTemp.get().getThirdCompany());
                    merchantChannelRate.setStatus(EnumCommonStatus.NORMAL.getId());
                    merchantChannelRateService.initMerchantChannelRate(merchantChannelRate);
                }
                //添加用户
                UserInfo uo = new UserInfo();
                uo.setMobile(MerchantSupport.encryptMobile(mobile));
                uo.setType(EnumUserInfoType.HSS.getId());
                uo.setRoleId(0);
                uo.setStatus(EnumCommonStatus.NORMAL.getId());
                uo.setMerchantId(mi.getId());
                userInfoService.insertUserInfo(uo);
                String tempMarkCode = GlobalID.GetGlobalID(EnumGlobalIDType.USER, EnumGlobalIDPro.MIN,uo.getId()+"");
                userInfoService.updatemarkCode(tempMarkCode,uo.getId());

                //添加好友
                DealerRecommend dealerRecommend = new DealerRecommend();
                dealerRecommend.setDealerId(dealerOptional.get().getId());
                dealerRecommend.setRecommendMerchantId(mi.getId());
                dealerRecommend.setType(EnumRecommendType.DIRECT.getId());
                dealerRecommend.setStatus(1);
                dealerRecommendService.insert(dealerRecommend);

                if(mi.getFirstDealerId()>0&&dealerOptional.get().getLevel()>1){//间接好友
                    DealerRecommend dealerRecommend1 = new DealerRecommend();
                    dealerRecommend1.setDealerId(mi.getFirstDealerId());
                    dealerRecommend1.setRecommendMerchantId(mi.getId());
                    dealerRecommend1.setType(EnumRecommendType.INDIRECT.getId());
                    dealerRecommend1.setStatus(1);
                    dealerRecommendService.insert(dealerRecommend1);
                }
            }else{
                mi.setSource(EnumSource.RECOMMEND.getId());
                //初始化代理商和商户
                Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectByMobileAndOemId(MerchantSupport.encryptMobile(inviteCode),oemId);
                if(!merchantInfoOptional.isPresent()){
                    throw new ApiHandleException(ResultCode.FRIEND_NOT_OPEN_SHARE);
                }
                List<ProductChannelDetail> productChannelDetailList = productChannelDetailService.selectByProductId(productId);
                if(productChannelDetailList.size()<=0){
                    throw new ApiHandleException(ResultCode.PRODUCT_RATE_ERROR);
                }
                mi.setDealerId(0);
                mi.setFirstDealerId(merchantInfoOptional.get().getFirstDealerId());
                mi.setSecondDealerId(merchantInfoOptional.get().getSecondDealerId());
                mi.setFirstMerchantId(merchantInfoOptional.get().getId());
                mi.setSecondMerchantId(merchantInfoOptional.get().getFirstMerchantId());
                mi.setAccountId(0);
                mi.setLevel(EnumUpGradeType.COMMON.getId());
                mi.setHierarchy(merchantInfoOptional.get().getHierarchy()+1);
                mi.setIsUpgrade(EnumIsUpgrade.CANUPGRADE.getId());
                mi.setOemId(oemId);
                merchantInfoService.regByWx(mi);
                for(int i=0;i<productChannelDetailList.size();i++){
                    MerchantChannelRate merchantChannelRate = new MerchantChannelRate();
                    merchantChannelRate.setMerchantId(mi.getId());
                    merchantChannelRate.setProductId(productId);
                    merchantChannelRate.setMarkCode(mi.getMarkCode());
                    merchantChannelRate.setSysType(EnumProductType.HSS.getId());
                    merchantChannelRate.setChannelTypeSign(productChannelDetailList.get(i).getChannelTypeSign());
                    merchantChannelRate.setMerchantBalanceType(productChannelDetailList.get(i).getProductBalanceType());
                    if(mi.getIsUpgrade()==EnumIsUpgrade.CANUPGRADE.getId()){
                        Optional<PartnerRuleSetting> partnerRuleSettingOptional = partnerRuleSettingService.selectByProductIdAndChannelTypeSign(productId,productChannelDetailList.get(i).getChannelTypeSign());
                        if(partnerRuleSettingOptional.isPresent()&&partnerRuleSettingOptional.get().getCommonRate()!=null){
                            merchantChannelRate.setMerchantPayRate(partnerRuleSettingOptional.get().getCommonRate());
                        }else{
                            merchantChannelRate.setMerchantPayRate(productChannelDetailList.get(i).getProductMerchantPayRate());
                        }
                    }else{
                        merchantChannelRate.setMerchantPayRate(productChannelDetailList.get(i).getProductMerchantPayRate());
                    }
                    merchantChannelRate.setMerchantWithdrawFee(productChannelDetailList.get(i).getProductMerchantWithdrawFee());
                    Optional<BasicChannel> basicChannelOptionalTemp = basicChannelService.selectByChannelTypeSign(productChannelDetailList.get(i).getChannelTypeSign());
                    if(basicChannelOptionalTemp.get().getIsNeed()==1){//需要入网
                        merchantChannelRate.setEnterNet(EnumEnterNet.UNENT.getId());
                    }else{
                        merchantChannelRate.setEnterNet(EnumEnterNet.UNSUPPORT.getId());
                    }
                    merchantChannelRate.setChannelCompany(basicChannelOptionalTemp.get().getChannelCompany());
                    merchantChannelRate.setThirdCompany(basicChannelOptionalTemp.get().getThirdCompany());
                    merchantChannelRate.setStatus(EnumCommonStatus.NORMAL.getId());
                    merchantChannelRateService.initMerchantChannelRate(merchantChannelRate);
                }

                //添加用户
                UserInfo uo = new UserInfo();
                uo.setMobile(MerchantSupport.encryptMobile(mobile));
                uo.setType(EnumUserInfoType.HSS.getId());
                uo.setRoleId(0);
                uo.setStatus(EnumCommonStatus.NORMAL.getId());
                uo.setMerchantId(mi.getId());
                userInfoService.insertUserInfo(uo);
                String tempMarkCode = GlobalID.GetGlobalID(EnumGlobalIDType.USER,EnumGlobalIDPro.MIN,uo.getId()+"");
                userInfoService.updatemarkCode(tempMarkCode,uo.getId());
                //添加好友
                if(mi.getFirstMerchantId()!=0){//直接好友
                    Recommend recommend = new Recommend();
                    recommend.setMerchantId(mi.getId());
                    recommend.setRecommendMerchantId(mi.getFirstMerchantId());
                    recommend.setType(EnumRecommendType.DIRECT.getId());
                    recommend.setStatus(1);
                    recommendService.insert(recommend);
                }
                if(mi.getSecondMerchantId()!=0){//间接好友
                    Recommend recommend = new Recommend();
                    recommend.setMerchantId(mi.getId());
                    recommend.setRecommendMerchantId(mi.getSecondMerchantId());
                    recommend.setType(EnumRecommendType.INDIRECT.getId());
                    recommend.setStatus(1);
                    recommendService.insert(recommend);
                }
            }

            List<AppAuToken> tokenList=appAuTokenDao.findAppAuTokenByAccessToken(appParam.getAccessToken());
            if (tokenList != null && tokenList.size() != 0)
            {
                String clientId=tokenList.get(0).getClientId();
                if(clientId!=null&&!clientId.trim().equals("")) {
                    List<AppAuToken> tokenFindList = appAuTokenDao.findAppAuTokenByClientId(clientId);
                    for (AppAuToken token : tokenFindList) {
                        if(!token.getAccessToken().equals(appParam.getAccessToken()))
                            appAuTokenDao.updateAppAuUserTokenStatusByTid(token.getId());
                    }
                }
                appAuTokenDao.updateAppAuUserTokenStatusByTidExceptUid(tokenList.get(0).getId(),mi.getId());
                AppAuUserToken appAuUserToken=new AppAuUserToken();
                appAuUserToken.setUid(mi.getId());
                appAuUserToken.setTid(tokenList.get(0).getId());
                List<AppAuUserToken> appAuUserTokenList=appAuTokenDao.findAppAuUserTokenByParam(appAuUserToken);
                Date dateToken=new Date();
                if(appAuUserTokenList!=null&&appAuUserTokenList.size()!=0)
                {
                    AppAuUserToken appAuUserTokenUpdate=appAuUserTokenList.get(0);
                    appAuUserTokenUpdate.setStatus(1);
                    appAuUserTokenUpdate.setLoginTime(dateToken);
                    appAuUserTokenUpdate.setOutTime(DateUtil.changeDate(dateToken, Calendar.MONTH,1));
                    appAuTokenDao.updateAppAuUserTokenByUidAndTid(appAuUserTokenUpdate);
                }
                else
                {
                    appAuUserToken.setStatus(1);
                    appAuUserToken.setLoginTime(dateToken);
                    appAuUserToken.setOutTime(DateUtil.changeDate(dateToken,Calendar.MONTH,1));
                    appAuTokenDao.insertAppAuUserToken(appAuUserToken);
                }
            }
            else
                throw new ApiHandleException(ResultCode.ACCESSTOKEN_NOT_FOUND);
            gson = new GsonBuilder().create();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("mid",mi.getId());
            return gson.toJson(map);
        }
    }
    /**
     * 商户登录 HSS001007
     * @param dataParam
     * @param appParam
     * @return
     * @throws ApiHandleException
     */
    @Override
    public String login(String dataParam, AppParam appParam) throws ApiHandleException {
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        AppLoginRequest appLoginRequest=null;
        try{
            appLoginRequest=gson.fromJson(dataParam, AppLoginRequest.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }
        String mobile = appLoginRequest.getMobile();
        String verifyCode = appLoginRequest.getCode();
        String oemNo = appLoginRequest.getOemNo();
        long oemId = 0;
        if(!StringUtils.isBlank(oemNo)){
            Optional<OemInfo> oemInfoOptional = oemInfoService.selectByOemNo(oemNo);
            if(!oemInfoOptional.isPresent()){
                throw new ApiHandleException(ResultCode.USER_NOT_EXIST);
            }
            oemId = oemInfoOptional.get().getDealerId();
        }
        final Pair<Integer, String> checkResult =
                this.smsAuthService.checkVerifyCode(mobile, verifyCode, EnumVerificationCodeType.LOGIN_MERCHANT);
        if (1 != checkResult.getLeft()) {
            throw new ApiHandleException(checkResult.getRight());
        }
        if (StringUtils.isBlank(mobile)) {
            throw new ApiHandleException(ResultCode.PARAM_LACK,"手机号");
        }
        if (!ValidateUtils.isMobile(mobile)) {
            throw new ApiHandleException(ResultCode.CELLPHONE_NOT_CORRECT_FORMAT);
        }
        if (StringUtils.isBlank(verifyCode)) {
            throw new ApiHandleException(ResultCode.PARAM_LACK,"验证码");
        }
        if (!ValidateUtils.verifyCodeCheck(verifyCode)) {
            throw new ApiHandleException(ResultCode.VERIFICATIONCODE_ERROR);
        }
        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectByMobileAndOemId(MerchantSupport.encryptMobile(mobile),oemId);
        if(!merchantInfoOptional.isPresent()){
            throw new ApiHandleException(ResultCode.USER_NOT_EXIST);
        }
        List<AppAuToken> tokenList=appAuTokenDao.findAppAuTokenByAccessToken(appParam.getAccessToken());
        if (tokenList != null && tokenList.size() != 0)
        {
            String clientID=tokenList.get(0).getClientId();
            if(clientID!=null&&!clientID.trim().equals("")) {
                List<AppAuToken> tokenFindList = appAuTokenDao.findAppAuTokenByClientId(clientID);
                for (AppAuToken token : tokenFindList) {
                    if(!token.getAccessToken().equals(appParam.getAccessToken()))
                        appAuTokenDao.updateAppAuUserTokenStatusByTid(token.getId());
                }
            }
            appAuTokenDao.updateAppAuUserTokenStatusByTidExceptUid(tokenList.get(0).getId(),merchantInfoOptional.get().getId());
            AppAuUserToken appAuUserToken=new AppAuUserToken();
            appAuUserToken.setUid(merchantInfoOptional.get().getId());
            appAuUserToken.setTid(tokenList.get(0).getId());
            List<AppAuUserToken> appAuUserTokenList=appAuTokenDao.findAppAuUserTokenByParam(appAuUserToken);
            Date dateToken=new Date();
            if(appAuUserTokenList!=null&&appAuUserTokenList.size()!=0)
            {
                AppAuUserToken appAuUserTokenUpdate=appAuUserTokenList.get(0);
                appAuUserTokenUpdate.setStatus(1);
                appAuUserTokenUpdate.setLoginTime(dateToken);
                appAuUserTokenUpdate.setOutTime(DateUtil.changeDate(dateToken,Calendar.MONTH,1));
                appAuTokenDao.updateAppAuUserTokenByUidAndTid(appAuUserTokenUpdate);
            }
            else
            {
                appAuUserToken.setStatus(1);
                appAuUserToken.setLoginTime(dateToken);
                appAuUserToken.setOutTime(DateUtil.changeDate(dateToken,Calendar.MONTH,1));
                appAuTokenDao.insertAppAuUserToken(appAuUserToken);
            }
        }
        else
            throw new ApiHandleException(ResultCode.ACCESSTOKEN_NOT_FOUND);
        gson = new GsonBuilder().create();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("mid",merchantInfoOptional.get().getId());
        return gson.toJson(map);
    }

    /**
     * HSS001008 退出登录
     * @param dataParam
     * @param appParam
     * @return
     * @throws ApiHandleException
     */
    public String logout(String dataParam, AppParam appParam)throws ApiHandleException{
        if(!(appParam.getAccessToken()!=null&&!appParam.getAccessToken().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"令牌（公参）");
        List<AppAuToken> tokenList=appAuTokenDao.findAppAuTokenByAccessToken(appParam.getAccessToken());
        if (tokenList != null && tokenList.size() != 0)
        {
            appAuTokenDao.updateAppAuUserTokenStatusByTid(tokenList.get(0).getId());
        }
        else
            throw new ApiHandleException(ResultCode.ACCESSTOKEN_NOT_FOUND);
        return "";
    }
}
