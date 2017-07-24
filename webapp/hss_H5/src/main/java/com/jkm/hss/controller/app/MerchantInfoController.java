package com.jkm.hss.controller.app;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.enums.EnumGlobalIDPro;
import com.jkm.base.common.enums.EnumGlobalIDType;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.base.common.util.GlobalID;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.entity.OemInfo;
import com.jkm.hss.dealer.enums.EnumInviteBtn;
import com.jkm.hss.dealer.enums.EnumRecommendBtn;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.dealer.service.OemInfoService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.DirectLoginRequest;
import com.jkm.hss.helper.request.MerchantLoginRequest;
import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.Recommend;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.enums.*;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.RecommendService;
import com.jkm.hss.merchant.service.UserInfoService;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by xingliujie on 2017/7/24.
 */
@Slf4j
@Controller
@RequestMapping(value = "merchantInfo")
public class MerchantInfoController extends BaseController {
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private SmsAuthService smsAuthService;
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private ProductChannelDetailService productChannelDetailService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private DealerChannelRateService dealerChannelRateService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MerchantChannelRateService merchantChannelRateService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private PartnerRuleSettingService partnerRuleSettingService;
    @Autowired
    private OemInfoService oemInfoService;
    /**
     * 注册
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public CommonResponse register(HttpServletRequest request,HttpServletResponse response,@RequestBody MerchantLoginRequest loginRequest) {
        long oemId = 0;
        if(loginRequest.getOemNo()!=null&&!"".equals(loginRequest.getOemNo())){
            Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(loginRequest.getOemNo());
            if(!oemInfoOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "分公司不存在");
            }
            oemId = oemInfoOptional.get().getDealerId();
        }
        final String mobile = loginRequest.getMobile();
        final String verifyCode = loginRequest.getCode();
        if (StringUtils.isBlank(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号不能为空");
        }
        if (!ValidateUtils.isMobile(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        if (StringUtils.isBlank(verifyCode)) {
            return CommonResponse.simpleResponse(-1, "验证码不能为空");
        }
        if (!ValidateUtils.verifyCodeCheck(verifyCode)) {
            return CommonResponse.simpleResponse(-1, "请输入正确的6位数字验证码");
        }
        if (StringUtils.isBlank(loginRequest.getInviteCode())) {
            return CommonResponse.simpleResponse(-1, "邀请码不能为空");
        }
        if (!StringUtils.isBlank(loginRequest.getInviteCode())) {
            if (loginRequest.getInviteCode().length()!=6&&loginRequest.getInviteCode().length()!=11) {
                return CommonResponse.simpleResponse(-1, "邀请码必须是6位或11位");
            }
            if(loginRequest.getInviteCode().length()==6){
                Optional<Dealer> dealerOptional = dealerService.getDealerByInviteCode(loginRequest.getInviteCode());
                if(!dealerOptional.isPresent()){
                    return CommonResponse.simpleResponse(-1, "您的朋友没有开通分享功能");
                }
                if(StringUtils.isBlank(dealerOptional.get().getInviteCode())){
                    return CommonResponse.simpleResponse(-1, "您的朋友没有开通分享功能");
                }
                if(dealerOptional.get().getInviteBtn()== EnumInviteBtn.OFF.getId()){
                    return CommonResponse.simpleResponse(-1, "您的朋友没有开通分享功能");
                }
                if(dealerOptional.get().getRecommendBtn()== EnumRecommendBtn.OFF.getId()){
                    return CommonResponse.simpleResponse(-1, "邀请码无效");
                }
            }else{
                if(loginRequest.getInviteCode().equals(loginRequest.getMobile())){
                    return CommonResponse.simpleResponse(-1, "不能邀请自己");
                }
                Optional<MerchantInfo> miOptional = merchantInfoService.selectByMobileAndOemId(MerchantSupport.encryptMobile(loginRequest.getInviteCode()),oemId);
                if(!miOptional.isPresent()){
                    return CommonResponse.simpleResponse(-1, "您的朋友没有开通分享功能");
                }
                if(miOptional.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&miOptional.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
                    return CommonResponse.simpleResponse(-1, "您的朋友没有开通分享功能");
                }
                if(miOptional.get().getIsUpgrade()== EnumIsUpgrade.CANNOTUPGRADE.getId()){
                    return CommonResponse.simpleResponse(-1, "您的朋友没有开通分享功能");
                }
            }
        }
        final Pair<Integer, String> checkResult =
                this.smsAuthService.checkVerifyCode(mobile, verifyCode, EnumVerificationCodeType.REGISTER_MERCHANT);
        if (1 != checkResult.getLeft()) {
            return CommonResponse.simpleResponse(-1, checkResult.getRight());
        }
        Optional<Product> productOptional = productService.selectByType(EnumProductType.HSS.getId());
        if(!productOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "产品信息有误");
        }
        long productId = productOptional.get().getId();
        Optional<MerchantInfo> merchantInfoOptional1 = merchantInfoService.selectByMobileAndOemId(MerchantSupport.encryptMobile(mobile),oemId);
        if(merchantInfoOptional1.isPresent()){
            return CommonResponse.simpleResponse(2, "该商户已注册，请直接登录");
        }else{
            MerchantInfo mi = new MerchantInfo();
            mi.setStatus(EnumMerchantStatus.INIT.getId());
            mi.setMobile(MerchantSupport.encryptMobile(mobile));
            mi.setMdMobile(MerchantSupport.passwordDigest(mobile,"JKM"));
            mi.setProductId(productId);
            if(loginRequest.getInviteCode().length()==6){
                mi.setSource(EnumSource.APPDEALERRECOMMEND.getId());
                Optional<Dealer> dealerOptional = dealerService.getDealerByInviteCode(loginRequest.getInviteCode());
                if(!dealerOptional.isPresent()){
                    return CommonResponse.simpleResponse(-1, "邀请码(代理商)不存在");
                }
                if(dealerOptional.get().getLevel()==EnumDealerLevel.FIRST.getId()){
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
                List<DealerChannelRate> dealerChannelRateList = dealerChannelRateService.selectByDealerIdAndProductId(dealerOptional.get().getId(),productId);
                if(dealerChannelRateList.size()>0){
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
                        if(!basicChannelOptionalTemp.isPresent()){
                            log.info("基本通道配置{}有误",dealerChannelRateList.get(i).getChannelTypeSign());
                        }
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
                }else{
                    log.info("代理商产品费率配置有误");
                    return CommonResponse.simpleResponse(-1, "代理商产品费率配置有误");
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
                return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "注册成功",mi.getId());
            }else{
                mi.setSource(EnumSource.APPRECOMMEND.getId());
                //初始化代理商和商户
                Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectByMobileAndOemId(MerchantSupport.encryptMobile(loginRequest.getInviteCode()),oemId);
                if(!merchantInfoOptional.isPresent()){
                    return CommonResponse.simpleResponse(-1, "您的朋友没有开通分享功能");
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
                List<ProductChannelDetail> productChannelDetailList = productChannelDetailService.selectByProductId(productId);
                if(productChannelDetailList.size()>0){
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
                        if(!basicChannelOptionalTemp.isPresent()){
                            log.info("基本通道配置{}有误",productChannelDetailList.get(i).getChannelTypeSign());
                        }
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
                }else{
                    log.info("基础产品费率配置有误");
                    return CommonResponse.simpleResponse(-1, "基础产品费率配置有误");
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
                return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "注册成功",mi.getId());
            }

        }
    }

    /**
     * 登录
     * @param directLoginRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public CommonResponse login(@RequestBody DirectLoginRequest directLoginRequest) {
        long oemId = 0;
        if(directLoginRequest.getOemNo()!=null&&!"".equals(directLoginRequest.getOemNo())){
            Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(directLoginRequest.getOemNo());
            if(!oemInfoOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "分公司不存在");
            }
            oemId = oemInfoOptional.get().getDealerId();
        }else{
            List<MerchantInfo> merchantInfoList = merchantInfoService.selectByMobile(MerchantSupport.encryptMobile(directLoginRequest.getMobile()));
            if(merchantInfoList.size()>0){
                for(int i=0;i<merchantInfoList.size();i++){
                    if(merchantInfoList.get(i).getOemId()>0){
                        Optional<OemInfo> oemInfoOptional1 = oemInfoService.selectOemInfoByDealerId(merchantInfoList.get(i).getOemId());
                        if((WxConstants.APP_ID).equals(oemInfoOptional1.get().getAppId())){
                            oemId = merchantInfoList.get(i).getOemId();
                        }
                    }
                }
            }
        }
        if (StringUtils.isBlank(directLoginRequest.getMobile())) {
            return CommonResponse.simpleResponse(-1, "手机号不能为空");
        }
        if (StringUtils.isBlank(directLoginRequest.getCode())) {
            return CommonResponse.simpleResponse(-1, "验证码不能为空");
        }
        if (!ValidateUtils.isMobile(directLoginRequest.getMobile())) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        if (!ValidateUtils.verifyCodeCheck(directLoginRequest.getCode())) {
            return CommonResponse.simpleResponse(-1, "请输入正确的6位数字验证码");
        }
        final Pair<Integer, String> checkResult =
                this.smsAuthService.checkVerifyCode(directLoginRequest.getMobile(), directLoginRequest.getCode(), EnumVerificationCodeType.LOGIN_MERCHANT);
        if (1 != checkResult.getLeft()) {
            return CommonResponse.simpleResponse(-1, checkResult.getRight());
        }
        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectByMobileAndOemId(MerchantSupport.encryptMobile(directLoginRequest.getMobile()),oemId);
        if(!merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "该账户不存在，请确定手机号是否正确");
        }
        Optional<UserInfo> userInfoOptional = userInfoService.selectByMerchantId(merchantInfoOptional.get().getId());
        if(userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "该账户不存在，请确定手机号是否正确");
        }else{
            log.info("手机号不正确");
            return CommonResponse.simpleResponse(-1, "该账户不存在，请确定手机号是否正确");
        }
    }
}
