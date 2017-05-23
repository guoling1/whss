package com.jkm.hss.controller.wx;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.base.common.enums.EnumGlobalIDPro;
import com.jkm.base.common.enums.EnumGlobalIDType;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.base.common.util.GlobalID;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.entity.OemInfo;
import com.jkm.hss.dealer.enums.EnumInviteBtn;
import com.jkm.hss.dealer.enums.EnumRecommendBtn;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.dealer.service.OemInfoService;
import com.jkm.hss.dealer.service.ShallProfitDetailService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.*;
import com.jkm.hss.helper.response.CurrentRulesResponse;
import com.jkm.hss.helper.response.ToUpgradeResponse;
import com.jkm.hss.merchant.entity.*;
import com.jkm.hss.merchant.enums.*;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.WxPubUtil;
import com.jkm.hss.merchant.helper.request.*;
import com.jkm.hss.merchant.helper.response.BankListResponse;
import com.jkm.hss.merchant.helper.response.MerchantChannelRateResponse;
import com.jkm.hss.merchant.service.*;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.notifier.service.SmsAuthService;
import com.jkm.hss.product.entity.*;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.enums.EnumUpGradeType;
import com.jkm.hss.product.enums.EnumUpperChannel;
import com.jkm.hss.product.servcie.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-23 14:17
 */
@Slf4j
@Controller
@RequestMapping(value = "/wx")
public class WxPubController extends BaseController {
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private SmsAuthService smsAuthService;
    @Autowired
    private SendMessageService sendMessageService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private OrderRecordService orderRecordService;
    @Autowired
    private ShallProfitDetailService shallProfitDetailService;
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private ProductChannelDetailService productChannelDetailService;
    @Autowired
    private QRCodeService qrCodeService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private DealerChannelRateService dealerChannelRateService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BankCardBinService bankCardBinService;
    @Autowired
    private MerchantChannelRateService merchantChannelRateService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private AccountBankService accountBankService;
    @Autowired
    private ProductChannelGatewayService productChannelGatewayService;
    @Autowired
    private ChannelSupportDebitCardService channelSupportDebitCardService;
    @Autowired
    private PartnerRuleSettingService partnerRuleSettingService;
    @Autowired
    private UpgradeRulesService upgradeRulesService;
    @Autowired
    private OemInfoService oemInfoService;



    /**
     * 好收收注册微信跳转
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "toSkip", method = RequestMethod.GET)
    public String  toSkip(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws Exception{
        String getQueryString = "";
        if(request.getQueryString() == null){
            getQueryString="";
        }else{
            getQueryString = request.getQueryString();
        }
        String[] arr = getQueryString.split("&");
        String code="";
        String state="";
        for(int i =0;i<arr.length;i++){
            if("code".equals(arr[i].split("=")[0])){
                code = arr[i].split("=")[1];
            }
            if("state".equals(arr[i].split("=")[0])){
                state = arr[i].split("=")[1];
            }
        }

        Map<String,String> ret = WxPubUtil.getOpenid(code);
        CookieUtil.setPersistentCookie(response, ApplicationConsts.MERCHANT_COOKIE_KEY, ret.get("openid"),
                ApplicationConsts.getApplicationConfig().domain());
        String tempUrl = URLDecoder.decode(state, "UTF-8");
        return "redirect:"+tempUrl;
    }

    /**
     * 分公司注册微信跳转
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "toOemSkip", method = RequestMethod.GET)
    public String  toOemSkip(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws Exception{
        String getQueryString = "";
        if(request.getQueryString() == null){
            getQueryString="";
        }else{
            getQueryString = request.getQueryString();
        }
        String[] arr = getQueryString.split("&");
        String code="";
        String state="";
        for(int i =0;i<arr.length;i++){
            if("code".equals(arr[i].split("=")[0])){
                code = arr[i].split("=")[1];
            }
            if("state".equals(arr[i].split("=")[0])){
                state = arr[i].split("=")[1];
            }
        }
        String tempUrl = URLDecoder.decode(state, "UTF-8");
        String[] temArr = tempUrl.split("[?]");
        String oemNo = "";
        if(temArr.length>1){
            String[] temArr1 = temArr[1].split("&");
            for(int i =0;i<temArr1.length;i++){
                if("oemNo".equals(temArr1[i].split("=")[0])){
                    oemNo = temArr1[i].split("=")[1];
                }
            }
        }
        Map<String,String> ret = null;
       if(!"".equals(oemNo)){
           Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(oemNo);
           if(oemInfoOptional.isPresent()){
               ret = WxPubUtil.getOpenid(code,oemInfoOptional.get().getAppId(),oemInfoOptional.get().getAppSecret());
           }else{
               ret = WxPubUtil.getOpenid(code);
           }
       }else{
           ret = WxPubUtil.getOpenid(code);
       }
        CookieUtil.setPersistentCookie(response, ApplicationConsts.MERCHANT_COOKIE_KEY, ret.get("openid"),
                ApplicationConsts.getApplicationConfig().domain());
        return "redirect:"+tempUrl;
    }

    /**
     * 火车票微信跳转页面
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "toTicketSkip", method = RequestMethod.GET)
    public String  toTicketSkip(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws Exception{
        String getQueryString = "";
        if(request.getQueryString() == null){
            getQueryString="";
        }else{
            getQueryString = request.getQueryString();
        }
        String[] arr = getQueryString.split("&");
        String code="";
        String state="";
        for(int i =0;i<arr.length;i++){
            if("code".equals(arr[i].split("=")[0])){
                code = arr[i].split("=")[1];
            }
            if("state".equals(arr[i].split("=")[0])){
                state = arr[i].split("=")[1];
            }
        }
        Map<String,String> ret = WxPubUtil.getOpenid(code);
        return "redirect:http://hcp.jinkaimen.com/ticket/main-menu/reserve?appid=1012&uid="+ ret.get("openid");
    }


    /**
     * 扫固定微信跳转页面
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "toMerchantSkip", method = RequestMethod.GET)
    public String  toMerchantSkip(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws Exception{
        String getQueryString = "";
        if(request.getQueryString() == null){
            getQueryString="";
        }else{
            getQueryString = request.getQueryString();
        }
        String[] arr = getQueryString.split("&");
        String code="";
        String state="";
        for(int i =0;i<arr.length;i++){
            if("code".equals(arr[i].split("=")[0])){
                code = arr[i].split("=")[1];
            }
            if("state".equals(arr[i].split("=")[0])){
                state = arr[i].split("=")[1];
            }
        }
        Map<String,String> ret = WxPubUtil.getOpenid(code);
        model.addAttribute("openId", ret.get("openid"));
        log.info("openid是：{}",ret.get("openid"));
        String tempUrl = URLDecoder.decode(state, "UTF-8");
        log.info("tempUrl是：{}",tempUrl);
        String redirectUrl = URLDecoder.decode(tempUrl,"UTF-8");
        log.info("redirectUrl是：{}",redirectUrl);
        String finalRedirectUrl = "http://"+ApplicationConsts.getApplicationConfig().domain()+"/code/scanCode?"+redirectUrl;
        log.info("跳转地址是：{}",finalRedirectUrl);
        return "redirect:"+finalRedirectUrl;
    }

    /**
     * 代理商邀请注册跳转
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "toDealerSkip", method = RequestMethod.GET)
    public String  toDealerSkip(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws Exception{
        String getQueryString = "";
        if(request.getQueryString() == null){
            getQueryString="";
        }else{
            getQueryString = request.getQueryString();
        }
        String[] arr = getQueryString.split("&");
        String code="";
        String state="";
        for(int i =0;i<arr.length;i++){
            if("code".equals(arr[i].split("=")[0])){
                code = arr[i].split("=")[1];
            }
            if("state".equals(arr[i].split("=")[0])){
                state = arr[i].split("=")[1];
            }
        }
        Map<String,String> ret = WxPubUtil.getOpenid(code);
        CookieUtil.setPersistentCookie(response, ApplicationConsts.MERCHANT_COOKIE_KEY, ret.get("openid"),
                ApplicationConsts.getApplicationConfig().domain());
        String tempUrl = URLDecoder.decode(state, "UTF-8");
        log.info("tempUrl是：{}",tempUrl);
        String redirectUrl = URLDecoder.decode(tempUrl,"UTF-8");
        log.info("redirectUrl是：{}",redirectUrl);
        String finalRedirectUrl = "http://"+ApplicationConsts.getApplicationConfig().domain()+"/reg?"+redirectUrl;
        log.info("跳转地址是：{}",finalRedirectUrl);
        return "redirect:"+finalRedirectUrl;
    }

    /**
     * 获取注册验证码
     * @param codeRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getCode", method = RequestMethod.POST)
    public CommonResponse getCode(@RequestBody MerchantLoginCodeRequest codeRequest) {
        final String mobile = codeRequest.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号不能为空");
        }
        if (!ValidateUtils.isMobile(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        Optional<UserInfo> userInfoOptional = userInfoService.selectByMobile(MerchantSupport.encryptMobile(mobile));
        if(userInfoOptional.isPresent()){
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "该用户已注册,请直接登录",false);
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
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "发送验证码成功",true);
        }
        return CommonResponse.simpleResponse(-1, verifyCode.getRight());
    }

    /**
     * 获取登录验证码
     * @param codeRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getLoginCode", method = RequestMethod.POST)
    public CommonResponse getLoginCode(@RequestBody MerchantLoginCodeRequest codeRequest) {
        final String mobile = codeRequest.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号不能为空");
        }
        if (!ValidateUtils.isMobile(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
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
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "发送验证码成功");
        }
        return CommonResponse.simpleResponse(-1, verifyCode.getRight());
    }

    /**
     * 发送提现验证码
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getWithDrawCode", method = RequestMethod.POST)
    public CommonResponse getWithDrawCode(final HttpServletRequest request, final HttpServletResponse response) {
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "登录异常，请重新登录");
        }
        Optional<MerchantInfo>  merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-1, "登录异常，请重新登录");
        }
        final Pair<Integer, String> verifyCode = this.smsAuthService.getVerifyCode(MerchantSupport.decryptMobile(merchantInfo.get().getReserveMobile()), EnumVerificationCodeType.WITH_DRAW);
        if (1 == verifyCode.getLeft()) {
            final Map<String, String> params = ImmutableMap.of("code", verifyCode.getRight());
            this.sendMessageService.sendMessage(SendMessageParams.builder()
                    .mobile(MerchantSupport.decryptMobile(merchantInfo.get().getReserveMobile()))
                    .uid("")
                    .data(params)
                    .userType(EnumUserType.BACKGROUND_USER)
                    .noticeType(EnumNoticeType.WITHDRAW_CODE)
                    .build()
            );
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "发送验证码成功");
        }
        return CommonResponse.simpleResponse(-1, verifyCode.getRight());
    }


    /**
     * 交易记录
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getOrderRecord", method = RequestMethod.POST)
    public CommonResponse getOrderRecord(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final RequestOrderRecord req) throws ParseException {
        if(!super.isLogin(request)){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        if(merchantInfo.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "信息未完善或待审核");
        }
        req.setMerchantId(merchantInfo.get().getId());
        final PageModel<OrderRecord> pageModel = new PageModel<>(req.getPage(), req.getSize());
        req.setOffset(pageModel.getFirstIndex());
        if(req.getEndDate()!=null&&!"".equals(req.getEndDate())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndDate());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndDate(sdf.format(rightNow.getTime()));
        }
        List<OrderRecord> orderList =  orderRecordService.selectAllOrderRecordByPage(req);
        long count = orderRecordService.selectAllOrderRecordCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(orderList);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }

    /**
     * 注册
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public CommonResponse login(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final MerchantLoginRequest loginRequest) {
        log.info("参数为{}",JSONObject.fromObject(loginRequest).toString());
        final String mobile = loginRequest.getMobile();
        final String verifyCode = loginRequest.getCode();
        if (StringUtils.isBlank(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号不能为空");
        }
        if (StringUtils.isBlank(verifyCode)) {
            return CommonResponse.simpleResponse(-1, "验证码不能为空");
        }
        if (!ValidateUtils.isMobile(mobile)) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        if (!ValidateUtils.verifyCodeCheck(verifyCode)) {
            return CommonResponse.simpleResponse(-1, "请输入正确的6位数字验证码");
        }
        if ((loginRequest.getQrCode()==null||"".equals(loginRequest.getQrCode()))&&StringUtils.isBlank(loginRequest.getInviteCode())) {
            return CommonResponse.simpleResponse(-1, "邀请码不能为空");
        }

        if (!StringUtils.isBlank(loginRequest.getInviteCode())) {
            if (loginRequest.getInviteCode().length()!=6&&loginRequest.getInviteCode().length()!=11) {
                return CommonResponse.simpleResponse(-1, "邀请码必须是6位或11位");
            }
            if(loginRequest.getInviteCode().length()==6){
                log.info("代理商邀请码{}",loginRequest.getInviteCode());
                Optional<Dealer> dealerOptional = dealerService.getDealerByInviteCode(loginRequest.getInviteCode());
                if(!dealerOptional.isPresent()){
                    return CommonResponse.simpleResponse(-1, "邀请码不存在");
                }
                if(StringUtils.isBlank(dealerOptional.get().getInviteCode())){
                    return CommonResponse.simpleResponse(-1, "邀请码不存在");
                }
                if(dealerOptional.get().getInviteBtn()== EnumInviteBtn.OFF.getId()){
                    return CommonResponse.simpleResponse(-1, "邀请码不存在");
                }
            }else{
                log.info("商户邀请码{}",loginRequest.getInviteCode());
                Optional<UserInfo> uoOptional =  userInfoService.selectByMobile(MerchantSupport.encryptMobile(loginRequest.getInviteCode()));
                if(!uoOptional.isPresent()){
                    return CommonResponse.simpleResponse(-1, "邀请码不存在");
                }
                if(loginRequest.getInviteCode().equals(loginRequest.getMobile())){
                    return CommonResponse.simpleResponse(-1, "不能邀请自己");
                }
                Optional<MerchantInfo> miOptional = merchantInfoService.selectByMobile(MerchantSupport.encryptMobile(loginRequest.getInviteCode()));
                if(!miOptional.isPresent()){
                    return CommonResponse.simpleResponse(-1, "邀请码不存在");
                }
                if(miOptional.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&miOptional.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
                    return CommonResponse.simpleResponse(-1, "邀请码不存在");
                }
                if(miOptional.get().getIsUpgrade()==EnumIsUpgrade.CANNOTUPGRADE.getId()){
                    return CommonResponse.simpleResponse(-1, "邀请码不存在");
                }
            }

        }

        final Pair<Integer, String> checkResult =
                this.smsAuthService.checkVerifyCode(mobile, verifyCode, EnumVerificationCodeType.REGISTER_MERCHANT);
        if (1 != checkResult.getLeft()) {
            return CommonResponse.simpleResponse(-1, checkResult.getRight());
        }


        //产品id
        Optional<Product> productOptional = productService.selectByType(EnumProductType.HSS.getId());
        if(!productOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "产品信息有误");
        }
        long productId = productOptional.get().getId();
        //①根据openId判断有没有用户
        Optional<UserInfo> ui = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!ui.isPresent()){//根据openId找不到商户
            //②根据mobile判断有没有用户
            Optional<UserInfo> uoOptional = userInfoService.selectByMobile(MerchantSupport.encryptMobile(mobile));
            if(uoOptional.isPresent()){//有
                return CommonResponse.simpleResponse(2, "该商户已注册，请直接登录");
            }else{//没有，走注册
                //③判断是扫码注册还是邀请注册
                if(loginRequest.getQrCode()!=null&&!"".equals(loginRequest.getQrCode())){
                    log.info("扫码注册");
                    MerchantInfo mi = new MerchantInfo();
                    mi.setStatus(EnumMerchantStatus.INIT.getId());
                    mi.setMobile(MerchantSupport.encryptMobile(mobile));
                    mi.setMdMobile(MerchantSupport.passwordDigest(mobile,"JKM"));
                    mi.setCode(loginRequest.getQrCode());
                    mi.setSource(EnumSource.SCAN.getId());
                    mi.setProductId(productId);
                    //根据二维码查出该商户所属代理商
                    Triple<Long, Long, Long> triple =  qrCodeService.getCurrentAndFirstAndSecondByCode(mi.getCode());
                    mi.setDealerId(triple.getLeft());
                    mi.setFirstDealerId(triple.getMiddle());
                    mi.setSecondDealerId(triple.getRight());
                    mi.setFirstMerchantId(0);
                    mi.setSecondMerchantId(0);
                    mi.setAccountId(0);
                    mi.setLevel(EnumUpGradeType.COMMON.getId());
                    mi.setHierarchy(0);
                    mi.setIsUpgrade(EnumIsUpgrade.CANUPGRADE.getId());
                    //判断是否能升级
                    if(mi.getFirstDealerId()>0){
                        Optional<Dealer> dealerOptional = dealerService.getById(mi.getFirstDealerId());
                        if(dealerOptional.isPresent()){
                            int recommendBtn = dealerOptional.get().getRecommendBtn();
                            if(EnumRecommendBtn.OFF.getId()==recommendBtn){
                                mi.setIsUpgrade(EnumIsUpgrade.CANNOTUPGRADE.getId());
                            }else{//能升级
                                mi.setIsUpgrade(EnumIsUpgrade.CANUPGRADE.getId());
                            }
                        }
                    }
                    //添加商户
                    merchantInfoService.regByCode(mi);
                    //初始化费率
                    if(mi.getFirstDealerId()>0){
                        log.info("开始继承代理商产品费率配置");
                        //配置代理商费率
                        Optional<Dealer> dealerOptional = dealerService.getById(mi.getFirstDealerId());
                        if(dealerOptional.isPresent()){//存在
                            //②配置费率
                            log.info("该商户继承代理商{}费率",mi.getFirstDealerId());
                            List<DealerChannelRate> dealerChannelRateList = dealerChannelRateService.selectByDealerIdAndProductId(mi.getFirstDealerId(),productId);
                            if(dealerChannelRateList.size()>0){
                                    for(int i=0;i<dealerChannelRateList.size();i++){
                                        MerchantChannelRate merchantChannelRate = new MerchantChannelRate();
                                        merchantChannelRate.setMerchantId(mi.getId());
                                        merchantChannelRate.setMarkCode(mi.getMarkCode());
                                        merchantChannelRate.setSysType(EnumProductType.HSS.getId());
                                        merchantChannelRate.setProductId(productId);
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
                        }else{
                            log.info("初始化费率是二维码{}对应的代理商{}不存在",loginRequest.getQrCode(),mi.getFirstDealerId());
                            return CommonResponse.simpleResponse(-1, "代理商不存在");
                        }
                    }else{
                        log.info("开始继承基础产品费率配置");
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
                    }
                    //添加用户
                    UserInfo uo = new UserInfo();
                    uo.setMobile(MerchantSupport.encryptMobile(mobile));
                    uo.setPwd("");
                    uo.setOpenId(super.getOpenId(request));
                    Map<String, String> userMap = WxPubUtil.getUserInfo(super.getOpenId(request));
                    uo.setNickName(userMap.get("nickname"));
                    uo.setHeadImgUrl(userMap.get("headimgurl"));
                    uo.setType(EnumUserInfoType.HSS.getId());
                    uo.setRoleId(0);
                    uo.setStatus(EnumCommonStatus.NORMAL.getId());
                    uo.setMerchantId(mi.getId());
                    userInfoService.insertUserInfo(uo);
                    String tempMarkCode = GlobalID.GetGlobalID(EnumGlobalIDType.USER,EnumGlobalIDPro.MIN,uo.getId()+"");
                    userInfoService.updatemarkCode(tempMarkCode,uo.getId());
                    return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "注册成功",mi.getId());
                }else{
                    log.info("推荐注册");
                    MerchantInfo mi = new MerchantInfo();
                    mi.setStatus(EnumMerchantStatus.INIT.getId());
                    mi.setMobile(MerchantSupport.encryptMobile(mobile));
                    mi.setMdMobile(MerchantSupport.passwordDigest(mobile,"JKM"));
                    mi.setProductId(productId);
                    if(loginRequest.getInviteCode().length()==6){
                        mi.setSource(EnumSource.DEALERRECOMMEND.getId());
                        log.info("代理商邀请码注册");
                        Optional<Dealer> dealerOptional = dealerService.getDealerByInviteCode(loginRequest.getInviteCode());
                        if(!dealerOptional.isPresent()){
                            log.info("代理商不存在");
                            return CommonResponse.simpleResponse(-1, "邀请码(代理商)不存在");
                        }
                        if(dealerOptional.get().getLevel()==EnumDealerLevel.FIRST.getId()){//二级代理
                            mi.setDealerId(dealerOptional.get().getId());
                            mi.setFirstDealerId(dealerOptional.get().getId());
                            mi.setSecondDealerId(0);
                        }
                        if(dealerOptional.get().getLevel()==EnumDealerLevel.SECOND.getId()){//二级代理
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
//                                merchantChannelRate.setMerchantPayRate(dealerChannelRateList.get(i).getDealerMerchantPayRate());
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
                        uo.setPwd("");
                        uo.setOpenId(super.getOpenId(request));
                        Map<String, String> userMap = WxPubUtil.getUserInfo(super.getOpenId(request));
                        uo.setNickName(userMap.get("nickname"));
                        uo.setHeadImgUrl(userMap.get("headimgurl"));
                        uo.setType(EnumUserInfoType.HSS.getId());
                        uo.setRoleId(0);
                        uo.setStatus(EnumCommonStatus.NORMAL.getId());
                        uo.setMerchantId(mi.getId());
                        userInfoService.insertUserInfo(uo);
                        String tempMarkCode = GlobalID.GetGlobalID(EnumGlobalIDType.USER,EnumGlobalIDPro.MIN,uo.getId()+"");
                        userInfoService.updatemarkCode(tempMarkCode,uo.getId());
                        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "注册成功",mi.getId());
                    }else{
                        mi.setSource(EnumSource.RECOMMEND.getId());
                        log.info("手机号推荐注册");
                        //初始化代理商和商户
                        Optional<UserInfo> inviteUserOptional =  userInfoService.selectByMobile(MerchantSupport.encryptMobile(loginRequest.getInviteCode()));
                        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(inviteUserOptional.get().getMerchantId());
                        if(!merchantInfoOptional.isPresent()){
                            log.info("该用户没有关联的商户");
                            return CommonResponse.simpleResponse(-1, "邀请码不存在");
                        }
                        mi.setDealerId(0);
                        mi.setFirstDealerId(merchantInfoOptional.get().getFirstDealerId());
                        mi.setSecondDealerId(merchantInfoOptional.get().getSecondDealerId());
                        mi.setFirstMerchantId(merchantInfoOptional.get().getId());
                        mi.setSecondMerchantId(merchantInfoOptional.get().getFirstMerchantId());
                        mi.setAccountId(0);
                        mi.setLevel(EnumUpGradeType.COMMON.getId());
                        mi.setHierarchy(merchantInfoOptional.get().getHierarchy()+1);//邀请人级别加1
                        mi.setIsUpgrade(EnumIsUpgrade.CANUPGRADE.getId());
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
                        uo.setPwd("");
                        uo.setOpenId(super.getOpenId(request));
                        Map<String, String> userMap = WxPubUtil.getUserInfo(super.getOpenId(request));
                        uo.setNickName(userMap.get("nickname"));
                        uo.setHeadImgUrl(userMap.get("headimgurl"));
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
        }else{//该商户已注册
            return CommonResponse.simpleResponse(2, "该商户已注册,请直接登录");
        }
    }


    /**
     * 解除微信绑定
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "unbundling", method = RequestMethod.POST)
    public CommonResponse unbundling(final HttpServletRequest request, final HttpServletResponse response) {
        if(!super.isLogin(request)){//尚未登录，无法解绑
            return CommonResponse.simpleResponse(-2, "尚未登录，无法解绑");
        }
        String openId = super.getOpenId(request);
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(openId);
        if(userInfoOptional.isPresent()){//用户存在
            int returnCount = userInfoService.cleanOpenId(userInfoOptional.get().getId());
            if(returnCount>0){
                return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "解绑成功");
            }else{
                return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "解绑失败，请重试");
            }
        }else{//用户不存在
            return CommonResponse.simpleResponse(-1, "您尚未绑定微信，请先注册");
        }
    }
    /**
     * 已有账户，直接登录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "directLogin", method = RequestMethod.POST)
    public CommonResponse directLogin(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final DirectLoginRequest directLoginRequest) {
        log.info("手机号登录");
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
        Optional<UserInfo> userInfoOptional = userInfoService.selectByMobile(MerchantSupport.encryptMobile(directLoginRequest.getMobile()));
        if(userInfoOptional.isPresent()){
            if(userInfoOptional.get().getOpenId()!=null&&!"".equals(userInfoOptional.get().getOpenId())){
                if((userInfoOptional.get().getOpenId()).equals(super.getOpenId(request))){//相等，直接登录
                    CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                    CookieUtil.setPersistentCookie(response, ApplicationConsts.MERCHANT_COOKIE_KEY, userInfoOptional.get().getOpenId(),
                            ApplicationConsts.getApplicationConfig().domain());
                    return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "登陆成功");
                }else{//不相等，报错
                    log.info("该手机号已绑定其他微信号，请解绑后再次登陆");
                    return CommonResponse.simpleResponse(-1, "该手机号已绑定其他微信号，请解绑后再次登陆");
                }
            }else{
                log.info("openId为空,说明解绑过，需要重新绑定");
                int backCount = userInfoService.bindOpenId(userInfoOptional.get().getId(),super.getOpenId(request));
                if(backCount>0){
                    log.info("绑定openId成功");
                    return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "登陆成功");
                }else{
                    log.info("绑定openId失败");
                    return CommonResponse.simpleResponse(-1, "登陆失败，请重试");
                }
            }
        }else{
            log.info("手机号不正确");
            return CommonResponse.simpleResponse(-1, "该账户不存在，请确定手机号是否正确");
        }
    }



    /**
     * 收款
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "receipt", method = RequestMethod.POST)
    public CommonResponse receipt(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final TradeRequest tradeRequest) {
        if(!super.isLogin(request)){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        if(merchantInfo.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "信息未完善或待审核");
        }
        final String totalFee = tradeRequest.getTotalFee();
        if (StringUtils.isBlank(totalFee)) {
            return CommonResponse.simpleResponse(-1, "请输入收款金额");
        }
        if(StringUtils.isBlank(merchantInfo.get().getMerchantName())){
            return CommonResponse.simpleResponse(-1, "缺失商户名称");
        }
        int compareResult = (new BigDecimal(totalFee)).compareTo(new BigDecimal("5"));
        if(compareResult<0){
            return CommonResponse.simpleResponse(-1, "支付金额至少5.00元");
        }
        tradeRequest.setMerchantId(merchantInfo.get().getId());
        tradeRequest.setSubMerName(merchantInfo.get().getMerchantName());
        tradeRequest.setSubMerNo(merchantInfo.get().getId()+"");
        JSONObject jo = orderRecordService.PayOrder(tradeRequest);
        if(jo.getInt("code")==1){
            return CommonResponse.objectResponse(1,"收款成功",jo.getJSONObject("data"));
        }else{
            return CommonResponse.simpleResponse(-1, jo.getString("message"));
        }
    }
    /**
     * 扫固定码收款
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "receiptByCode", method = RequestMethod.POST)
    public CommonResponse receiptByCode(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final TradeRequest tradeRequest) {
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(tradeRequest.getMerchantId());
        if(merchantInfo.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "信息未完善或待审核");
        }
        final String totalFee = tradeRequest.getTotalFee();
        if (StringUtils.isBlank(totalFee)) {
            return CommonResponse.simpleResponse(-1, "请输入收款金额");
        }
        if(StringUtils.isBlank(merchantInfo.get().getMerchantName())){
            return CommonResponse.simpleResponse(-1, "缺失商户名称");
        }
        int compareResult = (new BigDecimal(totalFee)).compareTo(new BigDecimal("5"));
        if(compareResult<0){
            return CommonResponse.simpleResponse(-1, "支付金额至少5.00元");
        }
        tradeRequest.setSubMerName(merchantInfo.get().getMerchantName());
        tradeRequest.setSubMerNo(merchantInfo.get().getId()+"");
        tradeRequest.setPayChannel(EnumPayChannelSign.YG_WECHAT.getId());
        JSONObject jo = orderRecordService.PayOrder(tradeRequest);
        if(jo.getInt("code")==1){
            return CommonResponse.objectResponse(1,"收款成功",jo.getJSONObject("data"));
        }else{
            return CommonResponse.simpleResponse(-1, jo.getString("message"));
        }
    }

    /**
     * 提现
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "withdraw", method = RequestMethod.POST)
    public CommonResponse withdraw(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final OtherPayRequest otherPayRequest) {
        final String verifyCode = otherPayRequest.getCode();
        if(!super.isLogin(request)){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        if(merchantInfo.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "信息未完善或待审核");
        }
        final Pair<Integer, String> checkResult =
                this.smsAuthService.checkVerifyCode(MerchantSupport.decryptMobile(merchantInfo.get().getReserveMobile()), verifyCode, EnumVerificationCodeType.WITH_DRAW);
        if (1 != checkResult.getLeft()) {
            return CommonResponse.simpleResponse(-1, checkResult.getRight());
        }
        JSONObject jo = orderRecordService.otherPay(merchantInfo.get());
        if(jo.getInt("code")==1){
            return CommonResponse.simpleResponse(1,"受理成功");
        }else{
            return CommonResponse.simpleResponse(-1, "提现失败");
        }
    }



    /**
     * 支付回调
     * @param request
     * @param response
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "payResult", method = RequestMethod.POST)
    public void payResult(final HttpServletRequest request,final HttpServletResponse response) throws IOException {
        String outTradeNo = request.getParameter("outTradeNo");
        String tradeType = request.getParameter("tradeType");
        String tradeResult = request.getParameter("tradeResult");
        String returnMsg = request.getParameter("returnMsg");
        String payNum = request.getParameter("orderId");
        Map map = new HashMap();
        map.put("outTradeNo",outTradeNo);
        map.put("tradeType",tradeType);
        map.put("tradeResult",tradeResult);
        map.put("returnMsg",returnMsg);
        map.put("orderId",payNum);
        orderRecordService.payResult(map);
        response.getWriter().write("SUCCESS");
    }

    /**
     * 我推广的好友
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "myRecommend", method = RequestMethod.POST)
    public CommonResponse myRecommend(final HttpServletRequest request, final HttpServletResponse response,@RequestBody final RecommendRequest recommendRequest ) {
        if(!super.isLogin(request)){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        if(merchantInfo.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "信息未完善或待审核");
        }
        recommendRequest.setMerchantId(merchantInfo.get().getId());
        RecommendAndMerchant recommendAndMerchant = recommendService.selectRecommend(recommendRequest);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", recommendAndMerchant);
    }

    /**
     * 信用卡认证
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "creditCardAuthen", method = RequestMethod.POST)
    public CommonResponse creditCardAuthen(final HttpServletRequest request, final HttpServletResponse response,@RequestBody final CreditCardAuthenRequest creditCardAuthenRequest) {
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
        if(!super.isLogin(request)){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        if(merchantInfo.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "信息未完善或待审核");
        }
        String creditCardNo = creditCardAuthenRequest.getCreditCard();
        String creditCardShort = creditCardNo.substring(creditCardNo.length()-4,creditCardNo.length());
        merchantInfoService.updateCreditCard(MerchantSupport.encryptBankCard(creditCardAuthenRequest.getCreditCard()),bankCardBinOptional.get().getBankName(),creditCardShort,merchantInfo.get().getId());
        Optional<MerchantInfo> merchantInfo1 = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        AccountBank accountBank = accountBankService.getDefault(merchantInfo1.get().getAccountId());
        if(accountBank==null){
            return CommonResponse.simpleResponse(-1, "您暂未设置默认银行卡");
        }
//        if(accountBank.getBranchName()!=null&&!"".equals(accountBank.getBranchName())
//                &&merchantInfo1.get().getCreditCard()!=null&&!"".equals(merchantInfo1.get().getCreditCard())){
//            merchantChannelRateService.enterInterNet(merchantInfo.get().getProductId(),merchantInfo.get().getId(),EnumUpperChannel.KAMENG.getValue());
//        }
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "操作成功");
    }

    /**
     * 支行信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "branchInfo", method = RequestMethod.POST)
    public CommonResponse branchInfo(final HttpServletRequest request, final HttpServletResponse response,@RequestBody final ContinueBankInfoRequest continueBankInfoRequest) {
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
        if(StringUtils.isBlank(continueBankInfoRequest.getBranchCode())){
            return CommonResponse.simpleResponse(-1, "请选择支行");
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
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
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
//        if(accountBank.getBranchName()!=null&&!"".equals(accountBank.getBranchName())
//                &&merchantInfo.get().getCreditCard()!=null&&!"".equals(merchantInfo.get().getCreditCard())){
//            merchantChannelRateService.enterInterNet(merchantInfo.get().getProductId(),merchantInfo.get().getId(),EnumUpperChannel.KAMENG.getValue());
//        }
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "操作成功");
    }

    /**
     * 查询通道是否可用
     * @param request
     * @param response
     * @param checkMerchantInfoRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkMerchantInfo", method = RequestMethod.POST)
    public CommonResponse checkMerchantInfo(final HttpServletRequest request, final HttpServletResponse response,@RequestBody final CheckMerchantInfoRequest checkMerchantInfoRequest) {
        if(!super.isLogin(request)){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        if(merchantInfo.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "信息未完善或待审核");
        }
        MerchantChannelRateRequest merchantChannelRateRequest = new MerchantChannelRateRequest();
        merchantChannelRateRequest.setMerchantId(merchantInfo.get().getId());
        merchantChannelRateRequest.setProductId(merchantInfo.get().getProductId());
        merchantChannelRateRequest.setChannelTypeSign(checkMerchantInfoRequest.getChannelTypeSign());
        Optional<MerchantChannelRate> merchantChannelRateOptional = merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(merchantChannelRateRequest);
        if(!merchantChannelRateOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "通道信息配置有误");
        }
        MerchantChannelRate merchantChannelRate = merchantChannelRateOptional.get();

        MerchantChannelRateResponse merchantChannelRateResponse = new MerchantChannelRateResponse();
        if(merchantChannelRate.getEnterNet()==EnumEnterNet.UNSUPPORT.getId()){
            log.info("商户无需入网");
            merchantChannelRateResponse.setIsBranch(EnumCheck.HAS.getId());
            merchantChannelRateResponse.setIsCreditCard(EnumCheck.HAS.getId());
            merchantChannelRateResponse.setIsNet(EnumEnterNet.UNSUPPORT.getId());
            merchantChannelRateResponse.setMessage("无需入网");
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "校验成功",merchantChannelRateResponse);
        }
        if(merchantChannelRate.getEnterNet()==EnumEnterNet.ENTING.getId()){
            log.info("商户入网中");
            merchantChannelRateResponse.setIsBranch(EnumCheck.HAS.getId());
            merchantChannelRateResponse.setIsCreditCard(EnumCheck.HAS.getId());
            merchantChannelRateResponse.setIsNet(EnumEnterNet.ENTING.getId());
            merchantChannelRateResponse.setMessage("商户入网中");
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "校验成功",merchantChannelRateResponse);
        }
        if(merchantChannelRate.getEnterNet()==EnumEnterNet.ENT_FAIL.getId()){
            log.info("商户入网失败");
            merchantChannelRateResponse.setIsBranch(EnumCheck.HAS.getId());
            merchantChannelRateResponse.setIsCreditCard(EnumCheck.HAS.getId());
            merchantChannelRateResponse.setIsNet(EnumEnterNet.ENT_FAIL.getId());
            merchantChannelRateResponse.setMessage("商户入网失败");
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "校验成功",merchantChannelRateResponse);
        }
        if(merchantChannelRate.getEnterNet()==EnumEnterNet.HASENT.getId()){
            log.info("商户已入网");
            merchantChannelRateResponse.setIsBranch(EnumCheck.HAS.getId());
            merchantChannelRateResponse.setIsCreditCard(EnumCheck.HAS.getId());
            merchantChannelRateResponse.setIsNet(EnumEnterNet.HASENT.getId());
            merchantChannelRateResponse.setMessage("商户已入网");
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "校验成功",merchantChannelRateResponse);
        }
        if(merchantChannelRate.getEnterNet()==EnumEnterNet.UNENT.getId()) {
            AccountBank accountBank = accountBankService.getDefault(merchantInfo.get().getAccountId());
            log.info("商户需入网");
            if (StringUtils.isEmpty(accountBank.getBranchCode())) {
                merchantChannelRateResponse.setIsBranch(EnumCheck.HASNOT.getId());
                if(StringUtils.isEmpty(merchantInfo.get().getCreditCard())){
                    merchantChannelRateResponse.setIsCreditCard(EnumCheck.HASNOT.getId());
                }else{
                    merchantChannelRateResponse.setIsCreditCard(EnumCheck.HAS.getId());
                }
                merchantChannelRateResponse.setIsNet(EnumEnterNet.UNENT.getId());
                merchantChannelRateResponse.setMessage("商户信息不完善");
                return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "商户信息不完善", merchantChannelRateResponse);
            }else{
                merchantChannelRateResponse.setIsBranch(EnumCheck.HAS.getId());
                if(StringUtils.isEmpty(merchantInfo.get().getCreditCard())){
                    merchantChannelRateResponse.setIsCreditCard(EnumCheck.HASNOT.getId());
                }else{
                    merchantChannelRateResponse.setIsCreditCard(EnumCheck.HAS.getId());
                }
                merchantChannelRateResponse.setIsNet(EnumEnterNet.UNENT.getId());
                merchantChannelRateResponse.setMessage("商户信息不完善");
                return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "商户信息不完善", merchantChannelRateResponse);
            }
        }
        return null;
    }

    /**
     * 我的银行卡列表
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "myCardList", method = RequestMethod.POST)
    public CommonResponse myCardList(final HttpServletRequest request, final HttpServletResponse response) {
        if(!super.isLogin(request)){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        if(merchantInfo.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "信息未完善或待审核");
        }
        List<BankListResponse> accountBankList = accountBankService.selectAll(merchantInfo.get().getAccountId());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", accountBankList);
    }

    /**
     * 删除信用卡
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "deleteCreditCard", method = RequestMethod.POST)
    public CommonResponse deleteCreditCard(final HttpServletRequest request, final HttpServletResponse response,@RequestBody final DeleteCreditCardRequest deleteCreditCardRequest) {
        if(!super.isLogin(request)){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        if(merchantInfo.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "信息未完善或待审核");
        }
        Optional<AccountBank> accountBankOptional = accountBankService.selectById(deleteCreditCardRequest.getBankId());
        if(accountBankOptional.isPresent()&&accountBankOptional.get().getCardType()!=EnumAccountBank.CREDIT.getId()){
            return CommonResponse.simpleResponse(-1, "只能删除信用卡");
        }
        accountBankService.deleteCreditCard(deleteCreditCardRequest.getBankId());
        if(accountBankOptional.get().getIsDefault()==EnumBankDefault.DEFAULT.getId()){
            Optional<AccountBank> accountBankOptional1 = accountBankService.getTopCreditCard(merchantInfo.get().getAccountId());
            if(accountBankOptional1.isPresent()){
                accountBankService.setDefaultCreditCardById(accountBankOptional1.get().getId());
            }
        }
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "删除成功");
    }


    /**
     * 查询通道是否可用
     * @param request
     * @param response
     * @param checkMerchantInfoRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkMerchantInfo1", method = RequestMethod.POST)
    public CommonResponse checkMerchantInfo1(final HttpServletRequest request, final HttpServletResponse response,@RequestBody final CheckMerchantInfoRequest checkMerchantInfoRequest) {
        if(!super.isLogin(request)){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        if(merchantInfo.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "信息未完善或待审核");
        }
        MerchantChannelRateRequest merchantChannelRateRequest = new MerchantChannelRateRequest();
        merchantChannelRateRequest.setMerchantId(merchantInfo.get().getId());
        merchantChannelRateRequest.setProductId(merchantInfo.get().getProductId());
        merchantChannelRateRequest.setChannelTypeSign(checkMerchantInfoRequest.getChannelTypeSign());
        Optional<MerchantChannelRate> merchantChannelRateOptional = merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(merchantChannelRateRequest);
        if(!merchantChannelRateOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "通道信息配置有误");
        }
        if(merchantChannelRateOptional.get().getChannelCompany()==null||"".equals(merchantChannelRateOptional.get().getChannelCompany())){
            log.info("上游通道公司为空");
            return CommonResponse.simpleResponse(-1, "通道信息配置有误");
        }
        MerchantChannelRate merchantChannelRate = merchantChannelRateOptional.get();
        //hlb通道结算卡拦截
        if (checkMerchantInfoRequest.getChannelTypeSign() == EnumPayChannelSign.HE_LI_UNIONPAY.getId()){
            final AccountBank accountBank = this.accountBankService.getDefault(merchantInfo.get().getAccountId());
            final Optional<ChannelSupportDebitCard> channelSupportDebitCardOptional = this.channelSupportDebitCardService.selectByBankCode(accountBank.getBankBin());
            if (!channelSupportDebitCardOptional.isPresent()){
                //通道结算卡不可用
                return CommonResponse.simpleResponse(-1, "该通道仅支持结算到大型银行，请联系客服更改结算卡再使用");
            }
        }
        //通道限额拦截，通道可用拦截，
        final BasicChannel basicChannel =
                this.basicChannelService.selectByChannelTypeSign(checkMerchantInfoRequest.getChannelTypeSign()).get();
        if (basicChannel.getIsUse() == EnumBoolean.FALSE.getCode()){
            //通道不可用
            return CommonResponse.simpleResponse(-1, "通道维护中");
        }
        if (checkMerchantInfoRequest.getAmount().compareTo(basicChannel.getLimitMinAmount()) == -1){
            return CommonResponse.simpleResponse(-1, "支付金额至少" + basicChannel.getLimitMinAmount() + "元");
        }
        if (checkMerchantInfoRequest.getAmount().compareTo(basicChannel.getLimitAmount()) == 1){
            return CommonResponse.simpleResponse(-1, "通道单笔限额" + basicChannel.getLimitAmount() + "元");
        }

        if(merchantChannelRate.getEnterNet()==EnumEnterNet.UNSUPPORT.getId()){
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "无需入网");
        }
        if(merchantChannelRate.getEnterNet()==EnumEnterNet.ENTING.getId()){
            log.info("商户入网中");
            return CommonResponse.simpleResponse(-1, "入网申请中");
        }
        if(merchantChannelRate.getEnterNet()==EnumEnterNet.ENT_FAIL.getId()){
            log.info("商户入网失败");
            if(merchantChannelRate.getRemarks()!=null&&merchantChannelRate.getRemarks().contains("重复")){
                return CommonResponse.simpleResponse(-1, "底层通道检测到您为重复入网，请使用其他通道");
            }else{
                return CommonResponse.simpleResponse(-1, "入网失败，请使用其他通道");
            }

        }
        if(merchantChannelRate.getEnterNet()==EnumEnterNet.HASENT.getId()){
            log.info("商户已入网");
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "商户已入网");
        }
        if(merchantChannelRate.getEnterNet()==EnumEnterNet.UNENT.getId()) {
            log.info("商户需入网");
            JSONObject jo = merchantChannelRateService.enterInterNet1(merchantInfo.get().getAccountId(),merchantInfo.get().getProductId(),merchantInfo.get().getId(),merchantChannelRateOptional.get().getChannelCompany());
            if(jo.getInt("code")==-1&&jo.getString("msg")!=null&&!"".equals(jo.getString("msg"))&&jo.getString("msg").contains("重复")){
                return CommonResponse.simpleResponse(-1, "底层通道检测到您为重复入网，请使用其他通道");
            }else{
                return CommonResponse.simpleResponse(jo.getInt("code"), jo.getString("msg"));
            }
        }
        return null;
    }

    private BigDecimal needMoney(long productId,int currentLevel,int needLevel){
        BigDecimal needMoney = null;
        //所升级别需付费
        Optional<UpgradeRules> upgradeRulesOptional = upgradeRulesService.selectByProductIdAndType(productId,needLevel);
        //当前级别需付费
        Optional<UpgradeRules> currentUpgradeRulesOptional = upgradeRulesService.selectByProductIdAndType(productId,currentLevel);
        if(!currentUpgradeRulesOptional.isPresent()){
            needMoney = upgradeRulesOptional.get().getUpgradeCost();
        }else{
            needMoney = upgradeRulesOptional.get().getUpgradeCost().subtract(currentUpgradeRulesOptional.get().getUpgradeCost());
        }
        return needMoney;
    }

    /**
     * 升级
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "toUpgrade", method = RequestMethod.POST)
    public CommonResponse toUpgrade(final HttpServletRequest request, final HttpServletResponse response) {
        if(!super.isLogin(request)){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        if(merchantInfo.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "信息未完善或待审核");
        }

        ToUpgradeResponse toUpgradeResponse = new ToUpgradeResponse();

        List<UpgradeRules> upgradeRules = upgradeRulesService.selectAll(merchantInfo.get().getProductId());
        List<CurrentRulesResponse> list = new ArrayList<CurrentRulesResponse>();
        int hasCount = recommendService.selectFriendCount(merchantInfo.get().getId());
        if(upgradeRules.size()>0){
            for(int i=0;i<upgradeRules.size();i++){
                CurrentRulesResponse currentRulesResponse = new CurrentRulesResponse();
                BigDecimal needMoney = needMoney(merchantInfo.get().getProductId(),merchantInfo.get().getLevel(),upgradeRules.get(i).getType());
                currentRulesResponse.setId(upgradeRules.get(i).getId());
                currentRulesResponse.setName(upgradeRules.get(i).getName());
                currentRulesResponse.setType(upgradeRules.get(i).getType());
                currentRulesResponse.setNeedCount(upgradeRules.get(i).getPromotionNum());
                currentRulesResponse.setRestCount(upgradeRules.get(i).getPromotionNum()-hasCount);
                currentRulesResponse.setNeedMoney(needMoney);
                currentRulesResponse.setUpgradeCost(upgradeRules.get(i).getUpgradeCost());
                list.add(currentRulesResponse);
            }
        }
        toUpgradeResponse.setMerchantId(merchantInfo.get().getId());
        toUpgradeResponse.setCurrentLevel(merchantInfo.get().getLevel());
        toUpgradeResponse.setUpgradeRules(list);
        toUpgradeResponse.setShareUrl("http://"+ApplicationConsts.getApplicationConfig().domain()+"/sqb/invite/"+userInfoOptional.get().getId());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", toUpgradeResponse);
    }
}
