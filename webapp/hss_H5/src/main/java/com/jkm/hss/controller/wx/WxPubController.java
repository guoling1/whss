package com.jkm.hss.controller.wx;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.enums.EnumGlobalIDPro;
import com.jkm.base.common.enums.EnumGlobalIDType;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.base.common.util.GlobalID;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.enums.EnumInviteBtn;
import com.jkm.hss.dealer.enums.EnumRecommendBtn;
import com.jkm.hss.dealer.enums.EnumSettlementPeriodType;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.dealer.service.ShallProfitDetailService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.*;
import com.jkm.hss.merchant.entity.*;
import com.jkm.hss.merchant.enums.*;
import com.jkm.hss.merchant.helper.MerchantConsts;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.SmPost;
import com.jkm.hss.merchant.helper.WxPubUtil;
import com.jkm.hss.merchant.helper.request.*;
import com.jkm.hss.merchant.helper.response.MerchantChannelRateResponse;
import com.jkm.hss.merchant.service.*;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.notifier.service.SmsAuthService;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.enums.*;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import com.jkm.hss.product.servcie.ProductService;
import com.jkm.hss.product.servcie.UpgradePayRecordService;
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
import java.text.DecimalFormat;
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
    private UpgradePayRecordService upgradePayRecordService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BankCardBinService bankCardBinService;
    @Autowired
    private MerchantChannelRateService merchantChannelRateService;
    @Autowired
    private BasicChannelService basicChannelService;



    /**
     * 好收收注册微信跳转页面
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
     * 获取商户银行卡信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "geBankInfo", method = RequestMethod.POST)
    public CommonResponse geBankInfo(final HttpServletRequest request, final HttpServletResponse response) {
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
        Map map = new HashMap();
        map.put("bankBin",merchantInfo.get().getBankBin());
        map.put("bankNo",merchantInfo.get().getBankNo());
        map.put("bankName",merchantInfo.get().getBankName());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", map);
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
            return CommonResponse.simpleResponse(-2, "未审核通过");
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
     * 登录
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
                if(miOptional.isPresent()&&miOptional.get().getIsUpgrade()==EnumIsUpgrade.CANNOTUPGRADE.getId()){
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
                                        merchantChannelRate.setProductId(productId);
                                        merchantChannelRate.setChannelTypeSign(dealerChannelRateList.get(i).getChannelTypeSign());
                                        merchantChannelRate.setMerchantBalanceType(dealerChannelRateList.get(i).getDealerBalanceType());
                                        merchantChannelRate.setMerchantPayRate(dealerChannelRateList.get(i).getDealerMerchantPayRate());
                                        merchantChannelRate.setMerchantWithdrawFee(dealerChannelRateList.get(i).getDealerMerchantWithdrawFee());
                                        merchantChannelRate.setEnterNet(EnumEnterNet.UNENT.getId());
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
                                merchantChannelRate.setChannelTypeSign(productChannelDetailList.get(i).getChannelTypeSign());
                                merchantChannelRate.setMerchantBalanceType(productChannelDetailList.get(i).getProductBalanceType());
                                merchantChannelRate.setMerchantPayRate(productChannelDetailList.get(i).getProductMerchantPayRate());
                                merchantChannelRate.setMerchantWithdrawFee(productChannelDetailList.get(i).getProductMerchantWithdrawFee());
                                merchantChannelRate.setEnterNet(EnumEnterNet.UNENT.getId());
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
                                merchantChannelRate.setChannelTypeSign(dealerChannelRateList.get(i).getChannelTypeSign());
                                merchantChannelRate.setMerchantBalanceType(dealerChannelRateList.get(i).getDealerBalanceType());
                                merchantChannelRate.setMerchantPayRate(dealerChannelRateList.get(i).getDealerMerchantPayRate());
                                merchantChannelRate.setMerchantWithdrawFee(dealerChannelRateList.get(i).getDealerMerchantWithdrawFee());
                                merchantChannelRate.setEnterNet(EnumEnterNet.UNENT.getId());
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
                                merchantChannelRate.setChannelTypeSign(productChannelDetailList.get(i).getChannelTypeSign());
                                merchantChannelRate.setMerchantBalanceType(productChannelDetailList.get(i).getProductBalanceType());
                                merchantChannelRate.setMerchantPayRate(productChannelDetailList.get(i).getProductMerchantPayRate());
                                merchantChannelRate.setMerchantWithdrawFee(productChannelDetailList.get(i).getProductMerchantWithdrawFee());
                                merchantChannelRate.setEnterNet(EnumEnterNet.UNENT.getId());
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
        if(merchantInfo.get().getStatus()!=EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!=EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
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
        if(merchantInfo.get().getStatus()!=EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!=EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
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
        if(merchantInfo.get().getStatus()!=EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!=EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
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
        if(merchantInfo.get().getStatus()!=EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!=EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
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
    @RequestMapping(value = "/creditCardAuthen", method = RequestMethod.POST)
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
        String creditCardNo = creditCardAuthenRequest.getCreditCard();
        String creditCardShort = creditCardNo.substring(creditCardNo.length()-4,creditCardNo.length());
        merchantInfoService.updateCreditCard(MerchantSupport.encryptBankCard(creditCardAuthenRequest.getCreditCard()),bankCardBinOptional.get().getBankName(),creditCardShort,merchantInfo.get().getId());
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
        continueBankInfoRequest.setId(merchantInfo.get().getId());
        merchantInfoService.updateBranchInfo(continueBankInfoRequest);
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
        Optional<BasicChannel> basicChannelOptional = basicChannelService.selectByChannelTypeSign(checkMerchantInfoRequest.getChannelTypeSign());
        if(!basicChannelOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "该通道不存在");
        }
        if(basicChannelOptional.get().getStatus()!= EnumBasicChannelStatus.USEING.getId()){//通道被禁用
            return CommonResponse.simpleResponse(-1, "该通道不存在");
        }
        /**
         * 1.是否需要入网
         * 2.是否填写支行信息
         * 3.是否填写信用卡信息
         * 4.入网状态
         */
        MerchantChannelRateResponse merchantChannelRateResponse = new MerchantChannelRateResponse();
        if(basicChannelOptional.get().getIsNeed()== EnumIsNet.NEED.getId()){//需入网
            log.info("商户需入网");
            if(StringUtils.isEmpty(merchantInfo.get().getBranchCode())){
                merchantChannelRateResponse.setIsBranch(EnumCheck.HASNOT.getId());
                merchantChannelRateResponse.setMessage("支行信息不完善");
                return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "支行信息不完善",merchantChannelRateResponse);
            }
            if(StringUtils.isEmpty(merchantInfo.get().getCreditCard())){
                merchantChannelRateResponse.setIsBranch(EnumCheck.HAS.getId());
                merchantChannelRateResponse.setIsCreditCard(EnumCheck.HASNOT.getId());
                merchantChannelRateResponse.setMessage("信用卡信息不完善");
                return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "信用卡信息不完善",merchantChannelRateResponse);
            }
            MerchantChannelRateRequest merchantChannelRateRequest = new MerchantChannelRateRequest();
            merchantChannelRateRequest.setChannelTypeSign(checkMerchantInfoRequest.getChannelTypeSign());
            merchantChannelRateRequest.setMerchantId(merchantInfo.get().getId());
            merchantChannelRateRequest.setProductId(merchantInfo.get().getProductId());
            Optional<MerchantChannelRate> merchantChannelRateOptional = merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(merchantChannelRateRequest);
            if(!merchantChannelRateOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "该通道不存在");
            }
            if(merchantChannelRateOptional.get().getEnterNet()!=EnumEnterNet.UNENT.getId()){//未入网
                Pair<Integer,Integer> payChannelSign = EnumPayChannelSign.getPayChannelSign(checkMerchantInfoRequest.getChannelTypeSign());

                MerchantChannelRateRequest merchantChannelRateRequest1 = new MerchantChannelRateRequest();
                merchantChannelRateRequest1.setChannelTypeSign(payChannelSign.getLeft());
                merchantChannelRateRequest1.setMerchantId(merchantInfo.get().getId());
                merchantChannelRateRequest1.setProductId(merchantInfo.get().getProductId());
                Optional<MerchantChannelRate> weixinMerchantChannelRateOptional = merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(merchantChannelRateRequest1);
                if(!weixinMerchantChannelRateOptional.isPresent()){
                    return CommonResponse.simpleResponse(-1, "微信通道"+payChannelSign.getLeft()+"不存在");
                }

                MerchantChannelRateRequest merchantChannelRateRequest2 = new MerchantChannelRateRequest();
                merchantChannelRateRequest2.setChannelTypeSign(payChannelSign.getRight());
                merchantChannelRateRequest2.setMerchantId(merchantInfo.get().getId());
                merchantChannelRateRequest2.setProductId(merchantInfo.get().getProductId());
                Optional<MerchantChannelRate> zhifubaoMerchantChannelRateOptional = merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(merchantChannelRateRequest2);
                if(!zhifubaoMerchantChannelRateOptional.isPresent()){
                    return CommonResponse.simpleResponse(-1, "微信通道"+payChannelSign.getRight()+"不存在");
                }

                merchantChannelRateOptional.get().getChannelTypeSign();
                Map<String, String> paramsMap = new HashMap<String, String>();
                paramsMap.put("merchantName", merchantInfo.get().getMerchantName());
                paramsMap.put("merchantNo", merchantInfo.get().getMarkCode());
                paramsMap.put("address",merchantInfo.get().getAddress());
                paramsMap.put("personName", merchantInfo.get().getName());
                paramsMap.put("idCard", merchantInfo.get().getIdentity());
                paramsMap.put("bankNo", merchantInfo.get().getBankNo());

                paramsMap.put("wxRate", weixinMerchantChannelRateOptional.get().getMerchantPayRate().toString());
                paramsMap.put("zfbRate", zhifubaoMerchantChannelRateOptional.get().getMerchantPayRate().toString());
                paramsMap.put("bankName",merchantInfo.get().getBankName());
                paramsMap.put("prov",merchantInfo.get().getProvinceName());
                paramsMap.put("city", merchantInfo.get().getCityName());     //后台通知url
                paramsMap.put("country", merchantInfo.get().getCountyName());
                paramsMap.put("bankBranch",merchantInfo.get().getBranchName());
                paramsMap.put("bankCode",merchantInfo.get().getBranchCode());
                paramsMap.put("creditCardNo",merchantInfo.get().getCreditCard());
                String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantIN(),paramsMap);
                if(result!=null&&!"".equals(result)){
                    JSONObject jo = JSONObject.fromObject(result);
                    if(jo.getInt("code")==1){
                        List<Integer> signIdList = new ArrayList<Integer>();
                        int count = merchantChannelRateService.batchCheck(signIdList);
                        merchantChannelRateResponse.setIsBranch(EnumCheck.HAS.getId());
                        merchantChannelRateResponse.setIsCreditCard(EnumCheck.HAS.getId());
                        merchantChannelRateResponse.setIsNet(EnumCheck.HAS.getId());
                        merchantChannelRateResponse.setMessage("入网成功");
                        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "校验成功",merchantChannelRateResponse);
                    }else{
                        merchantChannelRateResponse.setIsBranch(EnumCheck.HAS.getId());
                        merchantChannelRateResponse.setIsCreditCard(EnumCheck.HAS.getId());
                        merchantChannelRateResponse.setIsNet(EnumCheck.HASNOT.getId());
                        merchantChannelRateResponse.setMessage(jo.getString("msg"));
                        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, jo.getString("msg"), jo.getString("msg"));
                    }

                }else{
                    return CommonResponse.simpleResponse(-1, "入网异常");
                }
            }
            return null;
        }else{//否
            log.info("商户无需入网");
            merchantChannelRateResponse.setIsBranch(EnumCheck.HAS.getId());
            merchantChannelRateResponse.setIsCreditCard(EnumCheck.HAS.getId());
            merchantChannelRateResponse.setIsNet(EnumCheck.HAS.getId());
            merchantChannelRateResponse.setMessage("无需入网");
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "校验成功",merchantChannelRateResponse);
        }
    }

}
