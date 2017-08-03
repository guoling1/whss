package com.jkm.hss.controller.app;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.enums.EnumGlobalIDPro;
import com.jkm.base.common.enums.EnumGlobalIDType;
import com.jkm.base.common.util.DateFormatUtil;
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
import com.jkm.hss.helper.request.AppRecommendRequest;
import com.jkm.hss.helper.request.CardDetailRequest;
import com.jkm.hss.helper.request.CreditCardAuthenRequest;
import com.jkm.hss.helper.request.MerchantLoginRequest;
import com.jkm.hss.helper.response.AuthenticationResponse;
import com.jkm.hss.helper.response.CardDetailResponse;
import com.jkm.hss.merchant.entity.*;
import com.jkm.hss.merchant.enums.*;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.helper.request.ContinueBankInfoRequest;
import com.jkm.hss.merchant.helper.request.MerchantInfoAddRequest;
import com.jkm.hss.merchant.helper.request.RecommendRequest;
import com.jkm.hss.merchant.helper.response.BankListResponse;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by xingliujie on 2017/7/24.
 */
@Slf4j
@Controller
@RequestMapping(value = "appMerchantInfo")
public class AppMerchantInfoController extends BaseController {
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private UserInfoService userInfoService;
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
    private SmsAuthService smsAuthService;
    @Autowired
    private DealerRecommendService dealerRecommendService;
    @Autowired
    private OemInfoService oemInfoService;
    @Autowired
    private BankCardBinService bankCardBinService;
    @Autowired
    private VerifyIdService verifyIdService;
    @Autowired
    private SendMessageService sendMessageService;
    @Autowired
    private MerchantInfoCheckRecordService merchantInfoCheckRecordService;
    @Autowired
    private AccountBankService accountBankService;
    /**
     * HSSH5001002 注册
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public CommonResponse register(HttpServletRequest request,HttpServletResponse response,@RequestBody MerchantLoginRequest loginRequest) {
        long oemId = 0;
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
                oemId = dealerOptional.get().getOemId();
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
                oemId = miOptional.get().getOemId();
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
                mi.setSource(EnumSource.DEALERRECOMMEND.getId());
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
                return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "注册成功",mi.getId());
            }else{
                mi.setSource(EnumSource.RECOMMEND.getId());
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
     * HSSH5001003 推荐商户名称
     * @param loginRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getRecommendInfo", method = RequestMethod.POST)
    public CommonResponse getRecommendInfo(@RequestBody MerchantLoginRequest loginRequest) {
        long oemId = 0;
        if(loginRequest.getOemNo()!=null&&!"".equals(loginRequest.getOemNo())){
            Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(loginRequest.getOemNo());
            if(!oemInfoOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "分公司不存在");
            }
            oemId = oemInfoOptional.get().getDealerId();
        }else{
            List<MerchantInfo> merchantInfoList = merchantInfoService.selectByMobile(MerchantSupport.encryptMobile(loginRequest.getInviteCode()));
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
        if (StringUtils.isBlank(loginRequest.getInviteCode())) {
            return CommonResponse.simpleResponse(-1, "邀请码不能为空");
        }
        if(loginRequest.getInviteCode().length()>6){
            Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectByMobileAndOemId(MerchantSupport.encryptMobile(loginRequest.getInviteCode()),oemId);
            Preconditions.checkState(merchantInfoOptional.isPresent(), "邀请码不存在");
            if(merchantInfoOptional.get().getName()!=null&&!"".equals(merchantInfoOptional.get().getName())){
                return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",merchantInfoOptional.get().getName());
            }else{
                String mobile = MerchantSupport.decryptMobile(merchantInfoOptional.get().getMobile());
                return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",mobile.substring(0,3)+mobile.substring(mobile.length()-4,mobile.length()));
            }
        }else{
            Optional<Dealer> dealerOptional = dealerService.getDealerByInviteCode(loginRequest.getInviteCode());
            Preconditions.checkState(dealerOptional.isPresent(), "邀请码不存在");
            Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectBySuperDealerId(dealerOptional.get().getId());
            if(merchantInfoOptional.get().getName()!=null&&!"".equals(merchantInfoOptional.get().getName())){
                return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",merchantInfoOptional.get().getName());
            }else{
                String mobile = MerchantSupport.decryptMobile(merchantInfoOptional.get().getMobile());
                return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",mobile.substring(0,3)+mobile.substring(mobile.length()-4,mobile.length()));
            }
        }
    }

    /**
     * HSSH5001005 填写资料
     * @param request
     * @param response
     * @param merchantInfo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResponse save(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final MerchantInfoAddRequest merchantInfo){
        final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        final String reserveMobile = merchantInfo.getReserveMobile();
        final String verifyCode = merchantInfo.getCode();
        final String store = merchantInfo.getMerchantName();
        final String address = merchantInfo.getAddress();
        final String bankNo = merchantInfo.getBankNo();
        final String bankPic = merchantInfo.getBankPic();
        final String name = merchantInfo.getName();
        final String identity =merchantInfo.getIdentity();
        if (StringUtils.isBlank(store)) {
            return CommonResponse.simpleResponse(-1, "店铺名不能为空");
        }
        if (StringUtils.isBlank(address)) {
            return CommonResponse.simpleResponse(-1, "地址不能为空");
        }
        if (StringUtils.isBlank(bankNo)) {
            return CommonResponse.simpleResponse(-1, "结算卡号不能为空");
        }
        if (StringUtils.isBlank(bankPic)) {
            return CommonResponse.simpleResponse(-1, "上传照片不能为空");
        }
        if (StringUtils.isBlank(name)) {
            return CommonResponse.simpleResponse(-1, "开户名不能为空");
        }
        if (StringUtils.isBlank(identity)) {
            return CommonResponse.simpleResponse(-1, "身份证号不能为空");
        }
        if (StringUtils.isBlank(reserveMobile)) {
            return CommonResponse.simpleResponse(-1, "手机号不能为空");
        }
        if (StringUtils.isBlank(verifyCode)) {
            return CommonResponse.simpleResponse(-1, "验证码不能为空");
        }
        if (!ValidateUtils.isMobile(reserveMobile)) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        if (StringUtils.isBlank(merchantInfo.getDistrictCode())) {
            return CommonResponse.simpleResponse(-1, "请选择支行所在地区");
        }
        if (StringUtils.isBlank(merchantInfo.getBranchName())) {
            return CommonResponse.simpleResponse(-1, "请填写支行信息");
        }
        if (!ValidateUtils.verifyCodeCheck(verifyCode)) {
            return CommonResponse.simpleResponse(-1, "请输入正确的6位数字验证码");
        }
        final Pair<Integer, String> checkResult =
                this.smsAuthService.checkVerifyCode(reserveMobile, verifyCode, EnumVerificationCodeType.BIND_CARD_MERCHANT);
        if (1 != checkResult.getLeft()) {
            return CommonResponse.simpleResponse(-1, checkResult.getRight());
        }
        merchantInfo.setId(merchantInfoOptional.get().getId());
        merchantInfo.setIdentity(MerchantSupport.encryptIdenrity(merchantInfo.getIdentity()));
        merchantInfo.setStatus(EnumMerchantStatus.ONESTEP.getId());
        merchantInfo.setReserveMobile(MerchantSupport.encryptMobile(merchantInfo.getReserveMobile()));
        Optional<BankCardBin> bankCardBinOptional = bankCardBinService.analyseCardNo(bankNo);
        merchantInfo.setBankBin(bankCardBinOptional.get().getShorthand());
        merchantInfo.setBankName(bankCardBinOptional.get().getBankName());
        merchantInfo.setBankNoShort(merchantInfo.getBankNo().substring((merchantInfo.getBankNo()).length()-4,(merchantInfo.getBankNo()).length()));
        merchantInfo.setBankNo(MerchantSupport.encryptBankCard(merchantInfo.getBankNo()));
        merchantInfo.setBankPic(merchantInfo.getBankPic());
        int res = this.merchantInfoService.update(merchantInfo);
        if (res<=0) {
            return CommonResponse.simpleResponse(-1, "资料添加失败");
        }
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "资料添加成功");

    }

    /**
     * HSSH5001006 发送绑定银行卡验证码
     * @param merchantInfo
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendVerifyCode", method = RequestMethod.POST)
    public CommonResponse sendVerifyCode(@RequestBody final MerchantInfo merchantInfo, final HttpServletRequest request){
        Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        String reserveMobile = merchantInfo.getReserveMobile();
        if (StringUtils.isBlank(reserveMobile)) {
            return CommonResponse.simpleResponse(-1, "手机号不能为空");
        }
        if (!ValidateUtils.isMobile(reserveMobile)) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        //校验身份4要素
        final Pair<Integer, String> verifyPair =
                this.verifyID4Element(MerchantSupport.decryptMobile(merchantInfoOptional.get().getMobile()), merchantInfo);
        if (0 == verifyPair.getLeft()) {//成功
            merchantInfoService.toAuthen("1",merchantInfoOptional.get().getId());
        }

        final Pair<Integer, String> verifyCode = this.smsAuthService.getVerifyCode(reserveMobile, EnumVerificationCodeType.BIND_CARD_MERCHANT);
        if (1 == verifyCode.getLeft()) {
            final Map<String, String> params = ImmutableMap.of("code", verifyCode.getRight());
            this.sendMessageService.sendMessage(SendMessageParams.builder()
                    .mobile(reserveMobile)
                    .uid(merchantInfo.getId() + "")
                    .data(params)
                    .userType(EnumUserType.BACKGROUND_USER)
                    .noticeType(EnumNoticeType.BIND_CARD_MERCHANT)
                    .build()
            );
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "发送验证码成功");
        }
        return CommonResponse.simpleResponse(-1, verifyCode.getRight());
    }

    /**
     * HSSH5001007 上传资料
     * @param request
     * @param response
     * @param merchantInfo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/savePic", method = RequestMethod.POST)
    public CommonResponse savePic(final HttpServletRequest request, final HttpServletResponse response,@RequestBody final MerchantInfoAddRequest merchantInfo){
        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        Preconditions.checkState(merchantInfoOptional.isPresent(), "商户不存在");
        merchantInfo.setId(merchantInfoOptional.get().getId());
        merchantInfo.setStatus(EnumMerchantStatus.REVIEW.getId());
        if(StringUtils.isBlank(merchantInfo.getIdentityFacePic())){
            return CommonResponse.simpleResponse(-1, "请上传身份证正面");
        }
        if(StringUtils.isBlank(merchantInfo.getIdentityOppositePic())){
            return CommonResponse.simpleResponse(-1, "请上传身份证反面");
        }
        if(StringUtils.isBlank(merchantInfo.getIdentityHandPic())){
            return CommonResponse.simpleResponse(-1, "请上传手持身份证");
        }
        if(StringUtils.isBlank(merchantInfo.getBankHandPic())){
            return CommonResponse.simpleResponse(-1, "请上传手持银行卡");
        }
        merchantInfo.setIdentityFacePic(merchantInfo.getIdentityFacePic());
        merchantInfo.setIdentityHandPic(merchantInfo.getIdentityHandPic());
        merchantInfo.setIdentityOppositePic(merchantInfo.getIdentityOppositePic());
        merchantInfo.setBankHandPic(merchantInfo.getBankHandPic());
        this.merchantInfoService.updatePic(merchantInfo);
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"照片添加成功");
    }

    /**
     * HSSH5001008 查询商户状态
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMerchanStatus", method = RequestMethod.POST)
    public CommonResponse getMerchanStatus(final HttpServletRequest request, final HttpServletResponse response){
        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        Preconditions.checkState(merchantInfoOptional.isPresent(), "商户不存在");
        String msg = "查询成功";
        if (merchantInfoOptional.get().getStatus()==EnumMerchantStatus.UNPASSED.getId()){
            String res = merchantInfoCheckRecordService.selectById(merchantInfoOptional.get().getId());
            msg = res;
        }if (merchantInfoOptional.get().getStatus()==EnumMerchantStatus.REVIEW.getId()){
            msg = "您的资料已经提交，我们将在一个工作日内处理";
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE,msg,merchantInfoOptional.get().getStatus());
    }

    /**
     * HSSH5001009 查询商户认证状态及信息
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAuthenInfo", method = RequestMethod.POST)
    public CommonResponse getAuthenInfo(final HttpServletRequest request, final HttpServletResponse response){
        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if(merchantInfoOptional.get().getStatus()>0){
            authenticationResponse.setIsMerchantAuthen(1);
            authenticationResponse.setMerchantName(merchantInfoOptional.get().getMerchantName());
            if(merchantInfoOptional.get().getProvinceName()!=null&&!"".equals(merchantInfoOptional.get().getProvinceName())){
                if("110000,120000,310000,500000".contains(merchantInfoOptional.get().getCityCode())){
                    authenticationResponse.setDistrict((merchantInfoOptional.get().getProvinceName()==null?"":merchantInfoOptional.get().getProvinceName())+(merchantInfoOptional.get().getCountyName()==null?"":merchantInfoOptional.get().getCountyName()));
                }else{
                    authenticationResponse.setDistrict((merchantInfoOptional.get().getProvinceName()==null?"":merchantInfoOptional.get().getProvinceName())+(merchantInfoOptional.get().getCityName()==null?"":merchantInfoOptional.get().getCityName()));
                }
            }
            authenticationResponse.setAddress(merchantInfoOptional.get().getAddress());
            authenticationResponse.setCreateTime(merchantInfoOptional.get().getCreateTime()==null?"": DateFormatUtil.format(merchantInfoOptional.get().getCreateTime(), DateFormatUtil.yyyy_MM_dd_HH_mm_ss));
            String name = merchantInfoOptional.get().getName();
            String tempName = "";
            if(name!=null&&!"".equals(name)){
                for(int i=0;i<name.length()-1;i++){
                    tempName+="*";
                }
                tempName+=name.substring(name.length()-1,name.length());
            }
            authenticationResponse.setName(tempName);
            if(merchantInfoOptional.get().getIdentity()!=null&&!"".equals(merchantInfoOptional.get().getIdentity())){
                String idCard = MerchantSupport.decryptIdentity(merchantInfoOptional.get().getIdentity());
                authenticationResponse.setIdCard(idCard.substring(0,3)+"*************"+idCard.substring(idCard.length()-2,idCard.length()));
            }
        }else{
            authenticationResponse.setIsMerchantAuthen(0);
        }
        if(merchantInfoOptional.get().getCreditCard()!=null&&!"".equals(merchantInfoOptional.get().getCreditCard())){
            authenticationResponse.setIsCreditAuthen(1);
            authenticationResponse.setCreditCardName(merchantInfoOptional.get().getCreditCardName());
            authenticationResponse.setCreditCardShort(merchantInfoOptional.get().getCreditCardShort());
        }else{
            authenticationResponse.setIsCreditAuthen(0);
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE,"查询成功",authenticationResponse);
    }

    /**
     * HSSH5001010 信用卡认证
     * @param creditCardAuthenRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "creditCardAuthen", method = RequestMethod.POST)
    public CommonResponse creditCardAuthen(@RequestBody final CreditCardAuthenRequest creditCardAuthenRequest) {
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        if(StringUtils.isBlank(creditCardAuthenRequest.getCreditCard())){
            return CommonResponse.simpleResponse(-1, "请输入信用卡号");
        }
        final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(creditCardAuthenRequest.getCreditCard());
        if(!bankCardBinOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "信用卡号错误");
        }
        if(Integer.parseInt(bankCardBinOptional.get().getCardTypeCode())!=1){
            return CommonResponse.simpleResponse(-1, "只能输入信用卡");
        }
        String creditCardNo = creditCardAuthenRequest.getCreditCard();
        String creditCardShort = creditCardNo.substring(creditCardNo.length()-4,creditCardNo.length());
        merchantInfoService.updateCreditCard(MerchantSupport.encryptBankCard(creditCardAuthenRequest.getCreditCard()),bankCardBinOptional.get().getBankName(),creditCardShort,merchantInfo.get().getId());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "操作成功");
    }

    /**
     * HSSH5001011 银行卡列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "myCardList", method = RequestMethod.POST)
    public CommonResponse myCardList() {
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        List<BankListResponse> accountBankList = accountBankService.selectAll(merchantInfo.get().getAccountId());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", accountBankList);
    }

    /**
     * HSSH5001012 银行卡详情
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "cardDetail", method = RequestMethod.POST)
    public CommonResponse cardDetail(@RequestBody final CardDetailRequest cardDetailRequest) {
        Optional<AccountBank> accountBankOptional = accountBankService.selectById(cardDetailRequest.getBankId());
        Preconditions.checkState(accountBankOptional.isPresent(), "查询不到默认银行卡信息");
        CardDetailResponse cardDetailResponse = new CardDetailResponse();
        if(accountBankOptional.get().getBankNo()!=null&&!"".equals(accountBankOptional.get().getBankNo())){
            String bankNo = MerchantSupport.decryptBankCard(accountBankOptional.get().getBankNo());
            cardDetailResponse.setBankNo(bankNo.substring(bankNo.length()-4,bankNo.length()));
            cardDetailResponse.setRealBankNo(bankNo);
        }
        if(accountBankOptional.get().getReserveMobile()!=null&&!"".equals(accountBankOptional.get().getReserveMobile())){
            String mobile = MerchantSupport.decryptMobile(accountBankOptional.get().getReserveMobile());
            cardDetailResponse.setMobile(mobile.substring(0,3)+"******"+mobile.substring(mobile.length()-2,mobile.length()));
        }
        cardDetailResponse.setBankName(accountBankOptional.get().getBankName());
        cardDetailResponse.setBankBin(accountBankOptional.get().getBankBin());
        cardDetailResponse.setProvinceCode(accountBankOptional.get().getBranchProvinceCode());
        cardDetailResponse.setProvinceName(accountBankOptional.get().getBranchProvinceName());
        cardDetailResponse.setCityCode(accountBankOptional.get().getBranchCityCode());
        cardDetailResponse.setCityName(accountBankOptional.get().getBranchCityName());
        cardDetailResponse.setCountyCode(accountBankOptional.get().getBranchCountyCode());
        cardDetailResponse.setCountyName(accountBankOptional.get().getBranchCountyName());
        cardDetailResponse.setBranchCode(accountBankOptional.get().getBranchCode());
        if(accountBankOptional.get().getBranchName()!=null&&!"".equals(accountBankOptional.get().getBranchName())){//有支行信息
            String tempBranchName = accountBankOptional.get().getBranchName();
            if(tempBranchName.length()>12){
                tempBranchName = "***"+tempBranchName.substring(tempBranchName.length()-12,tempBranchName.length());
            }
            cardDetailResponse.setBranchShortName(tempBranchName);
            cardDetailResponse.setBranchName(accountBankOptional.get().getBranchName());
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", cardDetailResponse);
    }

    /**
     * HSSH5001013 保存支行信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "saveBranchInfo", method = RequestMethod.POST)
    public CommonResponse saveBranchInfo(final HttpServletRequest request, final HttpServletResponse response,@RequestBody final ContinueBankInfoRequest continueBankInfoRequest) {
        if(StringUtils.isBlank(continueBankInfoRequest.getProvinceCode())){
            return CommonResponse.simpleResponse(-1, "请选择省份");
        }
        if(StringUtils.isBlank(continueBankInfoRequest.getProvinceName())){
            return CommonResponse.simpleResponse(-1, "请选择省份");
        }
        if(StringUtils.isBlank(continueBankInfoRequest.getCityCode())){
            return CommonResponse.simpleResponse(-1, "请选择城市");
        }
        if(StringUtils.isBlank(continueBankInfoRequest.getCityName())){
            return CommonResponse.simpleResponse(-1, "请选择城市");
        }
        if(StringUtils.isBlank(continueBankInfoRequest.getCountyCode())){
            return CommonResponse.simpleResponse(-1, "请选择县");
        }
        if(StringUtils.isBlank(continueBankInfoRequest.getCountyName())){
            return CommonResponse.simpleResponse(-1, "请选择县");
        }
        if(StringUtils.isBlank(continueBankInfoRequest.getBranchName())){
            return CommonResponse.simpleResponse(-1, "请选择支行");
        }
        if(continueBankInfoRequest.getBankId()<=0){
            return CommonResponse.simpleResponse(-1, "银行卡参数输入有误");
        }
        if(!super.isLogin(request)){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        if(merchantInfo.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "信息未完善或待审核");
        }
        AccountBank accountBank = accountBankService.getDefault(merchantInfo.get().getAccountId());
        if(accountBank==null){
            return CommonResponse.simpleResponse(-1, "您暂未设置默认银行卡");
        }
        continueBankInfoRequest.setId(merchantInfo.get().getId());
        merchantInfoService.updateBranchInfo(continueBankInfoRequest);
        accountBankService.updateBranchInfo(continueBankInfoRequest);
        merchantChannelRateService.updateInterNet(merchantInfo.get().getAccountId(),merchantInfo.get().getId());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "操作成功");
    }

    /**
     * HSSH5001014 我的客户
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "myRecommend", method = RequestMethod.POST)
    public CommonResponse myRecommend(final HttpServletRequest request, final HttpServletResponse response,@RequestBody final AppRecommendRequest appRecommendRequest ) {
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        RecommendAndMerchant recommendAndMerchant = null;
        if(appRecommendRequest.getType()==1){
            RecommendRequest recommendRequest = new RecommendRequest();
            recommendRequest.setMerchantId(merchantInfo.get().getId());
            recommendAndMerchant = recommendService.selectRecommend(recommendRequest);
        }
        if(appRecommendRequest.getType()==2){
            RecommendRequest recommendRequest = new RecommendRequest();
            recommendRequest.setMerchantId(merchantInfo.get().getSuperDealerId());
            if(merchantInfo.get().getSuperDealerId()!=null&&merchantInfo.get().getSuperDealerId()>0){
                recommendAndMerchant = recommendService.selectSuperRecommend(recommendRequest);
            }else{
                recommendAndMerchant = new RecommendAndMerchant();
            }
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", recommendAndMerchant);
    }
    /**
     * 校验身份4要素
     *
     * @return
     */
    private Pair<Integer, String> verifyID4Element(final String mobile, final MerchantInfo merchantInfo) {
        final String bankcard = merchantInfo.getBankNo();
        final String idCard = merchantInfo.getIdentity();
        final String bankReserveMobile = merchantInfo.getReserveMobile();
        final String realName = merchantInfo.getName();
        final Pair<Integer, String> pair = this.verifyIdService.verifyID(mobile, bankcard, idCard, bankReserveMobile, realName);
        return pair;
    }
}
