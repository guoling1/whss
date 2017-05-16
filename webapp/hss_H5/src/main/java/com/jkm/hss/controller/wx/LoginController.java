package com.jkm.hss.controller.wx;


import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.admin.helper.responseparam.AppBizDistrictResponse;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.enums.EnumOrderStatus;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.PayService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.service.PartnerShallProfitDetailService;
import com.jkm.hss.dealer.service.ShallProfitDetailService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.response.CurrentRulesResponse;
import com.jkm.hss.merchant.entity.*;
import com.jkm.hss.merchant.enums.EnumIsUpgrade;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.enums.EnumPayMethod;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.helper.WxPubUtil;
import com.jkm.hss.merchant.helper.request.MerchantGetRateRequest;
import com.jkm.hss.merchant.service.*;
import com.jkm.hss.product.entity.*;
import com.jkm.hss.product.enums.*;
import com.jkm.hss.product.helper.response.PartnerRuleSettingResponse;
import com.jkm.hss.product.helper.response.UpgradeResult;
import com.jkm.hss.product.servcie.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-12-05 14:27
 */
@Slf4j
@Controller
@RequestMapping(value = "/sqb")
public class LoginController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MerchantInfoService merchantInfoService;

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private MerchantInfoCheckRecordService merchantInfoCheckRecordService;

    @Autowired
    private UpgradeRulesService upgradeRulesService;

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ProductChannelDetailService productChannelDetailService;

    @Autowired
    private ShallProfitDetailService shallProfitDetailService;

    @Autowired
    private UpgradePayRecordService upgradePayRecordService;

    @Autowired
    private PayService payService;

    @Autowired
    private PartnerShallProfitDetailService partnerShallProfitDetailService;

    @Autowired
    private MerchantChannelRateService merchantChannelRateService;

    @Autowired
    private AccountBankService accountBankService;

    @Autowired
    private PartnerRuleSettingService partnerRuleSettingService;

    @Autowired
    private ProductService productService;

    /**
     * 扫固定码注册和微信公众号注册入口
     * @param request
     * @param response
     * @param model
     * @param code
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/reg", method = RequestMethod.GET)
    public String reg(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "code", required = false) String code) throws IOException {
        boolean isRedirect = false;
        String ul = request.getRequestURI();
        if(code!=null&&!"".equals(code)){
            ul = request.getRequestURI()+"?code="+code;
        }
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+ul+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if (userInfoOptional.isPresent()) {
                if(code!=null&&!"".equals(code)){
                    Long merchantId = userInfoOptional.get().getMerchantId();
                    if (merchantId != null && merchantId != 0){
                        Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                        if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                            if(code!=null&&!"".equals(code)){
                                model.addAttribute("code",code);
                            }
                            model.addAttribute("openId",userInfoOptional.get().getOpenId());
                            url = "/reg";
                        }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                            url = "/sqb/addInfo";
                            isRedirect= true;
                        }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                            url = "/sqb/addNext";
                            isRedirect= true;
                        }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                                result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                                result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                            url = "/sqb/prompt";
                            isRedirect= true;
                        }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){//跳首页
                            model.addAttribute("message","您的微信已经绑定了好收收账号\n" +
                                    "请使用其他微信账号扫码");
                            url = "/message";
                        }else{
                            log.info("商户状态不合法，状态为{}",result.get().getStatus());
                            model.addAttribute("message","非法操作");
                            url = "/message";
                        }
                    }else {
                        log.info("二维码绑定错误，码{}，用户编码{}，商户编码{},openId[{}]",code,userInfoOptional.get().getId(),merchantId,super.getOpenId(request));
                        model.addAttribute("message","您的注册信息有误");
                        url = "/message";
                    }
                }else{
                    Long merchantId = userInfoOptional.get().getMerchantId();
                    if (merchantId != null && merchantId != 0){
                        Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                        if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                            if(code!=null&&!"".equals(code)){
                                model.addAttribute("code",code);
                            }
                            model.addAttribute("openId",userInfoOptional.get().getOpenId());
                            url = "/reg";
                        }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                            url = "/sqb/addInfo";
                            isRedirect= true;
                        }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                            url = "/sqb/addNext";
                            isRedirect= true;
                        }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                                result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                                result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                            url = "/sqb/prompt";
                            isRedirect= true;
                        }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){//跳首页
                            url = "/sqb/wallet";
                            isRedirect= true;
                        }
                    }else {
                        if(code!=null&&!"".equals(code)){
                            model.addAttribute("qrCode",code);
                        }
                        url = "/reg";
                    }
                }
            }else {
                if(code!=null&&!"".equals(code)){
                    model.addAttribute("qrCode",code);
                }
                url = "/reg";
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }
    }

    /**
     * 填写用户基本信息
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/addInfo",method = RequestMethod.GET)
    public String addInfo(final HttpServletRequest request, final HttpServletResponse response, final Model model)
            throws IOException {
        boolean isRedirect = false;
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if (userInfoOptional.isPresent()) {
                Long merchantId = userInfoOptional.get().getMerchantId();
                if (merchantId != null && merchantId != 0){
                    Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                    if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                        url = "/sqb/reg";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                        String requestUrl = "";
                        if(request.getQueryString() == null){
                            requestUrl = request.getRequestURL().toString();
                        }else{
                            requestUrl = request.getRequestURL()+"?"+request.getQueryString();
                        }
                        Map<String, String> res = WxPubUtil.sign(requestUrl);
                        model.addAttribute("config",res);
                        model.addAttribute("markCode",result.get().getMarkCode());
                        url = "/material";
                    }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                        url = "/sqb/addNext";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                            result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                            result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                        url = "/sqb/prompt";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){//跳首页
                        url = "/sqb/wallet";
                        isRedirect= true;
                    }
                }else{
                    CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                    url = "/sqb/reg";
                    isRedirect= true;
                }
            }else{
                CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                isRedirect= true;
                url = "/sqb/reg";
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }
    }
    /**
     * 填写用户基本信息
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/addNext",method = RequestMethod.GET)
    public String addNext(final HttpServletRequest request, final HttpServletResponse response, final Model model)
            throws IOException {
        boolean isRedirect = false;
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if (userInfoOptional.isPresent()) {
                Long merchantId = userInfoOptional.get().getMerchantId();
                if (merchantId != null && merchantId != 0){
                    Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                    if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                        url = "/sqb/reg";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                        url = "/sqb/addInfo";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                        String requestUrl = "";
                        if(request.getQueryString() == null){
                            requestUrl = request.getRequestURL().toString();
                        }else{
                            requestUrl = request.getRequestURL()+"?"+request.getQueryString();
                        }
                        Map<String, String> res = WxPubUtil.sign(requestUrl);
                        model.addAttribute("config",res);
                        url = "/upload";
                    }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                            result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                            result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                        url = "/sqb/prompt";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){//跳首页
                        url = "/sqb/wallet";
                        isRedirect= true;
                    }
                }else{
                    CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                    isRedirect= true;
                    url = "/sqb/reg";
                }
            }else{
                CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                isRedirect= true;
                url = "/sqb/reg";
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }
    }

    /**
     * 扫码支付用户扫未审核通过的商户二维码页面
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/unFinishedPrompt",method = RequestMethod.GET)
    public String notLoggedPrompt(final HttpServletRequest request, final HttpServletResponse response, final Model model)throws IOException {
        return "/unFinishedPrompt";
    }

    /**
     * 审核状态页面
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/prompt",method = RequestMethod.GET)
    public String prompt(final HttpServletRequest request, final HttpServletResponse response, final Model model)
            throws IOException {
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            Optional<MerchantInfo> result = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
            String res = merchantInfoCheckRecordService.selectById(userInfoOptional.get().getMerchantId());
            if (result.get().getStatus()==EnumMerchantStatus.UNPASSED.getId()){
                model.addAttribute("res",res);
                model.addAttribute("id",result.get().getId());
                return "/prompt";
            }if (result.get().getStatus()==EnumMerchantStatus.REVIEW.getId()){
                model.addAttribute("res","您的资料已经提交，我们将在一个工作日内处理");
                return "/prompt1";
            }
        }
        return null;
    }

    /**
     *重新审核
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/repeatAddInfo/{id}", method = RequestMethod.GET)
    public String repeatAddInfo(final HttpServletRequest request, final HttpServletResponse response, final Model model,@PathVariable("id") long id) throws IOException {
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            merchantInfoService.updateStauts(EnumMerchantStatus.INIT.getId(),id);
            String requestUrl = "";
            if(request.getQueryString() == null){
                requestUrl = request.getRequestURL().toString();
            }else{
                requestUrl = request.getRequestURL()+"?"+request.getQueryString();
            }
            Map<String, String> res = WxPubUtil.sign(requestUrl);
            model.addAttribute("config",res);
            return "/material";
        }
    }

    /**
     *添加关注
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/follow", method = RequestMethod.GET)
    public String follow(final HttpServletRequest request, final HttpServletResponse response, final Model model) throws IOException {
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            Map<String, String> map = WxPubUtil.getUserInfo(super.getOpenId(request));
            if("".equals(map.get("subscribe"))){
                return "/follow";
            }
            if("0".equals(map.get("subscribe"))){
                return "/follow";
            }else{
                return "redirect:/sqb/prompt";
            }
        }
    }

    /**
     *生成二维码
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/charge", method = RequestMethod.GET)
    public String charge(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "qrCode", required = true) String qrCode,
                         @RequestParam(value = "name", required = true) String name,
                         @RequestParam(value = "money", required = true) String money) throws IOException {
        model.addAttribute("payUrl",URLDecoder.decode(qrCode, "UTF-8"));
        model.addAttribute("subMerName",name);
        model.addAttribute("amount",money);
        return "/charge";
    }


    /**
     * 钱包页面
     * @param request
     * @return
     */
    @RequestMapping(value = "/wallet", method = RequestMethod.GET)
    public String wallet(final HttpServletRequest request, final Model model) throws IOException {
        if(!super.isLogin(request)){
            model.addAttribute("avaliable", "0.00");
            model.addAttribute("showRecommend", 1);
        }else{
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if(userInfoOptional.isPresent()){//存在
                if(userInfoOptional.get().getMerchantId()!=0){
                    Optional<MerchantInfo> merchantInfo = this.merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
                    if(merchantInfo.isPresent()){
//                        AccountInfo accountInfo = accountInfoService.selectByPrimaryKey(merchantInfo.get().getAccountId());
                        final Optional<Account> accountOptional = this.accountService.getById(merchantInfo.get().getAccountId());
                        if(!accountOptional.isPresent()){
                            model.addAttribute("avaliable", "0.00");
                        }else{
                            final Account account = accountOptional.get();
                            DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                            model.addAttribute("avaliable", account.getTotalAmount()==null?"0.00":decimalFormat.format(account.getTotalAmount()));
                        }
                        //是否显示推荐和升级
                        if(merchantInfo.get().getIsUpgrade()== EnumIsUpgrade.CANUPGRADE.getId()){//显示升级
                            model.addAttribute("showRecommend", 1);//显示升级
                        }else{
                            model.addAttribute("showRecommend", 2);//不显示升级
                        }
                    }else{
                        model.addAttribute("showRecommend", 1);
                        model.addAttribute("avaliable", "0.00");
                    }
                }else{
                    model.addAttribute("showRecommend", 1);
                    model.addAttribute("avaliable", "0.00");
                }
            }else{
                model.addAttribute("showRecommend", 1);
                model.addAttribute("avaliable", "0.00");
            }
        }
        return "/wallet";
    }

    /**
     * 输入收款金额页面
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/collection", method = RequestMethod.GET)
    public String collection(final HttpServletRequest request, final HttpServletResponse response, final Model model) throws IOException {
        boolean isRedirect = false;
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else{
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if (userInfoOptional.isPresent()) {
                Long merchantId = userInfoOptional.get().getMerchantId();
                if (merchantId != null && merchantId != 0){
                    Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                    if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                        url = "/sqb/reg";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                        url = "/sqb/addInfo";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                        url = "/sqb/addNext";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                            result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                            result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                        url = "/sqb/prompt";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){//跳首页
                        String merchantName = "";
                        long bankId = 0;
                        if(userInfoOptional.isPresent()){
                            Optional<MerchantInfo> merchantInfo = this.merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
                            if(merchantInfo.isPresent()){
                                merchantName = merchantInfo.get().getMerchantName();
                                AccountBank accountBank = accountBankService.getDefault(merchantInfo.get().getAccountId());
                                if(accountBank!=null){
                                    bankId = accountBank.getId();
                                }
                            }
                        }
                        model.addAttribute("merchantName", merchantName);
                        model.addAttribute("bankId", bankId);
                        url = "/collection";
                    }
                }else{
                    CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                    url = "/sqb/reg";
                    isRedirect= true;
                }
            }else{
                CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                isRedirect= true;
                url = "/sqb/reg";
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }
    }

    /**
     * 支付成功页面（前端回调）
     *
     *
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/success/{amount}/{orderId}", method = RequestMethod.GET)
    public String success(final HttpServletRequest request, final HttpServletResponse response, final Model model, @PathVariable("amount") String amount, @PathVariable("orderId") long orderId) throws IOException {
        model.addAttribute("money", amount);
        final Order order = this.orderService.getById(orderId).get();
        model.addAttribute("firstSn", order.getOrderNo().substring(0, order.getOrderNo().length() - 6));
        model.addAttribute("secondSn", order.getOrderNo().substring(order.getOrderNo().length() - 6, order.getOrderNo().length()));
        return "/success";
    }

    /**
     * 扫固定码微信支付页面
     * @param request
     * @param response
     * @param model
     * @param merchantId
     * @param name
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/paymentWx", method = RequestMethod.GET)
    public String paymentWx(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "merchantId", required = true) long merchantId,@RequestParam(value = "name") String name,@RequestParam(value = "isSelf") int isSelf) throws IOException {
        model.addAttribute("mid", merchantId);
        model.addAttribute("merchantName", name);
        model.addAttribute("isSelf", isSelf);
        return "/payment-wx";
    }

    /**
     * 扫固定码支付宝支付页面
     * @param request
     * @param response
     * @param model
     * @param merchantId
     * @param name
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/paymentZfb", method = RequestMethod.GET)
    public String paymentZfb(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "merchantId", required = true) long merchantId,@RequestParam(value = "name") String name) throws IOException {
        model.addAttribute("mid", merchantId);
        model.addAttribute("merchantName", name);
        return "/payment-zfb";
    }


    /**
     * 提现页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/drawCash", method = RequestMethod.GET)
    public String drawCash(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "code", required = false) String code) throws IOException {
        boolean isRedirect = false;
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if (userInfoOptional.isPresent()) {
                Long merchantId = userInfoOptional.get().getMerchantId();
                if (merchantId != null && merchantId != 0){
                    Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                    if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                        url = "/sqb/reg";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                        url = "/sqb/addInfo";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                        url = "/sqb/addNext";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                            result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                            result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                        url = "/sqb/prompt";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){//跳提现页面
                        AccountBank accountBank = accountBankService.getDefault(result.get().getAccountId());
                        if(accountBank==null){
                            model.addAttribute("message","查询不到默认银行卡信息");
                            url = "/message";
                        }
                        String phone = MerchantSupport.decryptMobile(accountBank.getReserveMobile());
                        String bankNo = MerchantSupport.decryptBankCard(accountBank.getBankNo());
                        model.addAttribute("phone_01", phone.substring(0,3));
                        model.addAttribute("phone_02", phone.substring(phone.length()-4,phone.length()));
                        model.addAttribute("bankNo", bankNo.substring(bankNo.length()-4,bankNo.length()));
                        model.addAttribute("bankName",accountBank.getBankName());
                        AccountInfo accountInfo = accountInfoService.selectByPrimaryKey(result.get().getAccountId());
                        DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                        if(accountInfo==null){//没有账户
                            model.addAttribute("channelFee","0.00");
                            model.addAttribute("avaMoney", "0.00");
                            model.addAttribute("realMoney","0.00");
                        }else{
                            Pair<BigDecimal, BigDecimal> pair = shallProfitDetailService.withdrawParams(merchantId,EnumPayChannelSign.YG_UNIONPAY.getId());
                            model.addAttribute("avaMoney", accountInfo.getAvailable()==null?"0.00":decimalFormat.format(accountInfo.getAvailable()));
                            int compareResult = accountInfo.getAvailable().compareTo(pair.getLeft());
                            if(compareResult!=1){//提现金额小于手续费
                                model.addAttribute("realMoney","0.00");
                            }else{
                                BigDecimal realMoney = accountInfo.getAvailable().subtract(pair.getLeft());
                                model.addAttribute("realMoney",decimalFormat.format(realMoney));
                            }
                            model.addAttribute("channelFee", pair.getLeft());
                        }
                        url = "/withdrawal";
                    }
                }else{
                    url = "/sqb/reg";
                    isRedirect= true;
                }
            }else{
                CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                isRedirect= true;
                url = "/sqb/reg";
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }
    }

    /**
     * 业务块（火车票跳转页面）
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/ticket", method = RequestMethod.GET)
    public String ticket(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        return "redirect:"+ WxConstants.WEIXIN_TICKET_USERINFO;
    }

    /**
     * 交易记录页面
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/tradeRecord", method = RequestMethod.GET)
    public String tradRecord(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws IOException {
        boolean isRedirect = false;
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if (userInfoOptional.isPresent()) {
                Long merchantId = userInfoOptional.get().getMerchantId();
                if (merchantId != null && merchantId != 0){
                    Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                    if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                        url = "/sqb/reg";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                        url = "/sqb/addInfo";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                        url = "/sqb/addNext";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                            result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                            result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                        url = "/sqb/prompt";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){//跳提现页面
                        url = "/tradeRecord";
                    }
                }else{
                    CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                    isRedirect= true;
                    url = "/sqb/reg";
                }
            }else{
                CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                isRedirect= true;
                url = "/sqb/reg";
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }
    }

    /**
     * 我的银行卡
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/bank", method = RequestMethod.GET)
    public String bank(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws IOException {
        boolean isRedirect = false;
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if (userInfoOptional.isPresent()) {
                Long merchantId = userInfoOptional.get().getMerchantId();
                if (merchantId != null && merchantId != 0){
                    Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                    if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                        url = "/sqb/reg";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                        url = "/sqb/addInfo";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                        url = "/sqb/addNext";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                            result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                            result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                        url = "/sqb/prompt";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){//跳提现页面
                        url = "/bank";
                    }
                }else{
                    CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                    isRedirect= true;
                    url = "/sqb/reg";
                }
            }else{
                CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                isRedirect= true;
                url = "/sqb/reg";
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }
    }

    /**
     * 所属分行
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/bankBranch/{bankId}", method = RequestMethod.GET)
    public String bankBranch(final HttpServletRequest request, final HttpServletResponse response,final Model model,@PathVariable("bankId") Long bankId) throws IOException {
        boolean isRedirect = false;
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if (userInfoOptional.isPresent()) {
                Long merchantId = userInfoOptional.get().getMerchantId();
                if (merchantId != null && merchantId != 0){
                    Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                    if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                        url = "/sqb/reg";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                        url = "/sqb/addInfo";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                        url = "/sqb/addNext";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                            result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                            result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                        url = "/sqb/prompt";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){//跳提现页面
                        Optional<AccountBank> accountBankOptional = accountBankService.selectById(bankId);
                        if(!accountBankOptional.isPresent()){
                            model.addAttribute("message","查询不到默认银行卡信息");
                            url = "/message";
                        }
                        if(accountBankOptional.get().getBankNo()!=null&&!"".equals(accountBankOptional.get().getBankNo())){
                            String bankNo = MerchantSupport.decryptBankCard(accountBankOptional.get().getBankNo());
                            model.addAttribute("bankNo",bankNo.substring(bankNo.length()-4,bankNo.length()));
                        }else{
                            model.addAttribute("bankNo","");
                        }
                        if(accountBankOptional.get().getReserveMobile()!=null&&!"".equals(accountBankOptional.get().getReserveMobile())){
                            String mobile = MerchantSupport.decryptMobile(accountBankOptional.get().getReserveMobile());
                            model.addAttribute("mobile",mobile.substring(0,3)+"******"+mobile.substring(mobile.length()-2,mobile.length()));
                        }else{
                            model.addAttribute("mobile","");
                        }
                        model.addAttribute("bankName", accountBankOptional.get().getBankName());
                        model.addAttribute("bankBin",accountBankOptional.get().getBankBin());
                        model.addAttribute("provinceCode",accountBankOptional.get().getBranchProvinceCode());
                        model.addAttribute("provinceName",accountBankOptional.get().getBranchProvinceName());
                        model.addAttribute("cityCode",accountBankOptional.get().getBranchCityCode());
                        model.addAttribute("cityName",accountBankOptional.get().getBranchCityName());
                        model.addAttribute("countyCode",accountBankOptional.get().getBranchCountyCode());
                        model.addAttribute("countyName",accountBankOptional.get().getBranchCountyName());
                        model.addAttribute("branchCode",accountBankOptional.get().getBranchCode());
                        if(accountBankOptional.get().getBranchName()!=null&&!"".equals(accountBankOptional.get().getBranchName())){//有支行信息
                            String tempBranchName = accountBankOptional.get().getBranchName();
                            if(tempBranchName.length()>12){
                                tempBranchName = "***"+tempBranchName.substring(tempBranchName.length()-12,tempBranchName.length());
                            }
                            model.addAttribute("branchShortName",tempBranchName);
                            model.addAttribute("branchName",accountBankOptional.get().getBranchName());
                        }else{
                            model.addAttribute("branchShortName","");
                            model.addAttribute("branchName","");
                        }
                        url = "/bankBranch";
                    }
                }else{
                    CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                    isRedirect= true;
                    url = "/sqb/reg";
                }
            }else{
                CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                isRedirect= true;
                url = "/sqb/reg";
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }
    }

    /**
     * 信用卡认证
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/creditCardAuthen", method = RequestMethod.GET)
    public String creditCardAuthen(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws IOException {
        boolean isRedirect = false;
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if (userInfoOptional.isPresent()) {
                Long merchantId = userInfoOptional.get().getMerchantId();
                if (merchantId != null && merchantId != 0){
                    Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                    if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                        url = "/sqb/reg";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                        url = "/sqb/addInfo";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                        url = "/sqb/addNext";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                            result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                            result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                        url = "/sqb/prompt";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){//跳提现页面
                        url = "/creditCardAuthen";
                    }
                }else{
                    CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                    isRedirect= true;
                    url = "/sqb/reg";
                }
            }else{
                CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                isRedirect= true;
                url = "/sqb/reg";
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }
    }

    private  Pair<String,String> payOf(int payWay,String status) {
        String result="";
        String message = "";
        if(payWay==0){//交易
            if("N".equals(status)){
                result="N";
                message="待支付";
            }
            if("H".equals(status)||"A".equals(status)||"E".equals(status)){
                result="H";
                message="支付中";
            }
            if("S".equals(status)){
                result="S";
                message="支付成功";
            }
            if("F".equals(status)){
                result="S";
                message="支付失败";
            }
        }
        if(payWay==1){//提现
            if("N".equals(status)){
                result="N";
                message="待审核";
            }
            if("H".equals(status)||"W".equals(status)||"E".equals(status)||"A".equals(status)){
                result="H";
                message="审核中";
            }
            if("S".equals(status)){
                result="S";
                message="提现成功";
            }
            if("F".equals(status)||"D".equals(status)){
                result="F";
                message="提现失败";
            }
            if("O".equals(status)){
                result="O";
                message="审核未通过";
            }
        }

        return Pair.of(result,message);
    }

    /**
     * 登录页面
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "mobile", required = false) String mobile) throws IOException {
        String ul = request.getRequestURI();
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+ul+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            model.addAttribute("mobile",mobile);
            return "/login";
        }
    }
    /**
     * 邀请注册
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/invite/{id}", method = RequestMethod.GET)
    public String invite(final HttpServletRequest request, final HttpServletResponse response, final Model model,@PathVariable("id") long id) throws IOException {
        boolean isRedirect = false;
        String ul = request.getRequestURI();
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+ul+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if (userInfoOptional.isPresent()) {
                model.addAttribute("message","您已经注册过了\n" +
                        "不能再被邀请注册");
                url = "/message";
            }else {
                Optional<UserInfo> uiOptional = userInfoService.selectById(id);
                if(uiOptional.isPresent()){
                    Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(uiOptional.get().getMerchantId());
                    if(merchantInfoOptional.isPresent()){
                        if(merchantInfoOptional.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfoOptional.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
                            model.addAttribute("message","推荐商户未审核通过");
                            url = "/message";
                        }else{
                            model.addAttribute("inviteCode",MerchantSupport.decryptMobile(uiOptional.get().getMobile()));
                            url = "/reg";
                        }
                    }else{
                        model.addAttribute("message","推荐商户不存在");
                        url = "/message";
                    }
                }else{
                    model.addAttribute("message","推荐用户不存在");
                    url = "/message";
                }
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }

    }
    /**
     * 推荐好友页面
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/myRecommend", method = RequestMethod.GET)
    public String myRecommend(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws IOException {
        boolean isRedirect = false;
        String ul = request.getRequestURI();
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+ul+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if (userInfoOptional.isPresent()) {
                Long merchantId = userInfoOptional.get().getMerchantId();
                if (merchantId != null && merchantId != 0){
                    Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                    if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                        isRedirect= true;
                        url = "/sqb/reg";
                    }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                        isRedirect= true;
                        url = "/sqb/addInfo";
                    }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                        isRedirect= true;
                        url = "/sqb/addNext";
                    }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                            result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                            result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                        isRedirect= true;
                        url = "/sqb/prompt";
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){
                        BigDecimal totalProfit = partnerShallProfitDetailService.selectTotalProfitByMerchantId(result.get().getId());
                        DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                        model.addAttribute("totalProfit", totalProfit==null?"0.00":decimalFormat.format(totalProfit));
                        model.addAttribute("shareUrl","http://"+ApplicationConsts.getApplicationConfig().domain()+"/sqb/invite/"+userInfoOptional.get().getId());
                        url = "/myRecommend";
                    }
                }else {
                    isRedirect= true;
                    url = "/sqb/reg";
                }
            }else {
                isRedirect= true;
                url = "/sqb/reg";
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }
    }

    private String getNameByLevel(int level){
        String name = "";
        if(level==0){
            name="普通";
        }
        if(level==1){
            name="店员";
        }
        if(level==2){
            name="店长";
        }
        if(level==3){
            name="老板";
        }
        return name;
    }
    /**
     * 升级降费率(大尺寸)
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/upgradeMax", method = RequestMethod.GET)
    public String upgradeMax(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws IOException {
        boolean isRedirect = false;
        String ul = request.getRequestURI();
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+ul+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if (userInfoOptional.isPresent()) {
                Long merchantId = userInfoOptional.get().getMerchantId();
                if (merchantId != null && merchantId != 0){
                    Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                    if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                        isRedirect= true;
                        url = "/sqb/reg";
                    }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                        isRedirect= true;
                        url = "/sqb/addInfo";
                    }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                        isRedirect= true;
                        url = "/sqb/addNext";
                    }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                            result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                            result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                        isRedirect= true;
                        url = "/sqb/prompt";
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){
                        Map<String, String> map = WxPubUtil.getUserInfo(userInfoOptional.get().getOpenId());
                        if(map==null){
                            model.addAttribute("headimgUrl","");
                        }else{
                            model.addAttribute("headimgUrl",map.get("headimgurl").toString());
                        }
                        String phone = MerchantSupport.decryptMobile(result.get().getMobile());
                        phone = phone.substring(0,3)+"***"+phone.substring(phone.length()-3,phone.length());
                        model.addAttribute("mobile",phone);
                        model.addAttribute("name",getNameByLevel(result.get().getLevel()));
                        model.addAttribute("level",result.get().getLevel());
                        Optional<Product> productOptional = productService.selectByType(EnumProductType.HSS.getId());
                        List<PartnerRuleSettingResponse> partnerRuleSettingResponses = partnerRuleSettingService.selectAllByProductId(productOptional.get().getId());
                        model.addAttribute("upgradeArray",partnerRuleSettingResponses);
                        url = "/upgradeMax";
                    }
                }else {
                    isRedirect= true;
                    url = "/sqb/reg";
                }
            }else {
                isRedirect= true;
                url = "/sqb/reg";
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }
    }
    /**
     * 升级降费率(小尺寸)
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/upgradeMin", method = RequestMethod.GET)
    public String upgradeMin(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws IOException {
        boolean isRedirect = false;
        String ul = request.getRequestURI();
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+ul+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if (userInfoOptional.isPresent()) {
                Long merchantId = userInfoOptional.get().getMerchantId();
                if (merchantId != null && merchantId != 0){
                    Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                    if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                        isRedirect= true;
                        url = "/sqb/reg";
                    }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                        isRedirect= true;
                        url = "/sqb/addInfo";
                    }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                        isRedirect= true;
                        url = "/sqb/addNext";
                    }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                            result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                            result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                        isRedirect= true;
                        url = "/sqb/prompt";
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){
                        Map<String, String> map = WxPubUtil.getUserInfo(userInfoOptional.get().getOpenId());
                        if(map==null){
                            model.addAttribute("headimgUrl","");
                        }else{
                            model.addAttribute("headimgUrl",map.get("headimgurl").toString());
                        }
                        String phone = MerchantSupport.decryptMobile(result.get().getMobile());
                        phone = phone.substring(0,3)+"***"+phone.substring(phone.length()-3,phone.length());
                        model.addAttribute("mobile",phone);
                        model.addAttribute("name",getNameByLevel(result.get().getLevel()));
                        model.addAttribute("level",result.get().getLevel());
                        Optional<Product> productOptional = productService.selectByType(EnumProductType.HSS.getId());
                        List<PartnerRuleSettingResponse> partnerRuleSettingResponses = partnerRuleSettingService.selectAllByProductId(productOptional.get().getId());
                        model.addAttribute("upgradeArray",partnerRuleSettingResponses);
                        url = "/upgradeMin";
                    }
                }else {
                    isRedirect= true;
                    url = "/sqb/reg";
                }
            }else {
                isRedirect= true;
                url = "/sqb/reg";
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }
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
     * 我要升级
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/toUpgrade", method = RequestMethod.GET)
    public String toUpgrade(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws IOException {
        boolean isRedirect = false;
        String ul = request.getRequestURI();
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+ul+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if (userInfoOptional.isPresent()) {
                Long merchantId = userInfoOptional.get().getMerchantId();
                if (merchantId != null && merchantId != 0){
                    Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                    if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                        isRedirect= true;
                        url = "/sqb/reg";
                    }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                        isRedirect= true;
                        url = "/sqb/addInfo";
                    }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                        isRedirect= true;
                        url = "/sqb/addNext";
                    }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                            result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                            result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                        isRedirect= true;
                        url = "/sqb/prompt";
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){

                        Optional<Product> productOptional = productService.selectByType(EnumProductType.HSS.getId());
                        List<UpgradeRules> upgradeRules = upgradeRulesService.selectAll(productOptional.get().getId());
                        List<CurrentRulesResponse> list = new ArrayList<CurrentRulesResponse>();
                        int hasCount = recommendService.selectFriendCount(result.get().getId());
                        if(upgradeRules.size()>0){
                            for(int i=0;i<upgradeRules.size();i++){
                                CurrentRulesResponse currentRulesResponse = new CurrentRulesResponse();
                                BigDecimal needMoney = needMoney(result.get().getProductId(),result.get().getLevel(),upgradeRules.get(i).getType());
                                currentRulesResponse.setId(upgradeRules.get(i).getId());
                                currentRulesResponse.setName(upgradeRules.get(i).getName());
                                currentRulesResponse.setType(upgradeRules.get(i).getType());
                                currentRulesResponse.setNeedCount(upgradeRules.get(i).getPromotionNum());
                                currentRulesResponse.setRestCount(upgradeRules.get(i).getPromotionNum()-hasCount);
                                currentRulesResponse.setNeedMoney(needMoney);
                                list.add(currentRulesResponse);
                            }
                        }else{
                            model.addAttribute("message","信息配置有误");
                            return "/500";
                        }
                        model.addAttribute("currentLevel",result.get().getLevel());
                        model.addAttribute("upgradeRules",list);
                        model.addAttribute("merchantId",result.get().getId());
                        model.addAttribute("shareUrl","http://"+ApplicationConsts.getApplicationConfig().domain()+"/sqb/invite/"+userInfoOptional.get().getId());
                        url = "/toUpgerde";
                    }
                }else {
                    isRedirect= true;
                    url = "/sqb/reg";
                }
            }else {
                isRedirect= true;
                url = "/sqb/reg";
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }
    }


    /**
     * 算算H5
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/suansuan", method = RequestMethod.GET)
    public String suansuan(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws IOException {
        return "/suansuan";
    }

    /**
     * 我的认证
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/authentication", method = RequestMethod.GET)
    public String authentication(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws IOException {
        boolean isRedirect = false;
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if (userInfoOptional.isPresent()) {
                Long merchantId = userInfoOptional.get().getMerchantId();
                if (merchantId != null && merchantId != 0){
                    Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                    if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                        url = "/sqb/reg";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                        url = "/sqb/addInfo";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                        url = "/sqb/addNext";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                            result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                            result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                        url = "/sqb/prompt";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){//跳首页
                        model.addAttribute("merchantName",result.get().getMerchantName());
                        if(result.get().getProvinceName()==null||"".equals(result.get().getProvinceName())){
                            model.addAttribute("district","");
                        }else{
                            if("110000,120000,310000,500000".contains(result.get().getCityCode())){
                                model.addAttribute("district",(result.get().getProvinceName()==null?"":result.get().getProvinceName())+(result.get().getCountyName()==null?"":result.get().getCountyName()));
                            }else{
                                model.addAttribute("district",(result.get().getProvinceName()==null?"":result.get().getProvinceName())+(result.get().getCityName()==null?"":result.get().getCityName()));
                            }
                        }
                        model.addAttribute("address",result.get().getAddress());
                        model.addAttribute("createTime",result.get().getCreateTime()==null?"":DateFormatUtil.format(result.get().getCreateTime(), DateFormatUtil.yyyy_MM_dd_HH_mm_ss));
                        String name = result.get().getName();
                        String tempName = "";
                        if(name!=null&&!"".equals(name)){
                            for(int i=0;i<name.length()-1;i++){
                                tempName+="*";
                            }
                            tempName+=name.substring(name.length()-1,name.length());
                        }
                        model.addAttribute("name",tempName);
                        if(result.get().getIdentity()!=null&&!"".equals(result.get().getIdentity())){
                            String idCard = MerchantSupport.decryptIdentity(result.get().getIdentity());
                            model.addAttribute("idCard",idCard.substring(0,3)+"*************"+idCard.substring(idCard.length()-2,idCard.length()));
                        }else{
                            model.addAttribute("idCard","");
                        }
                        if(result.get().getCreditCard()!=null&&!"".equals(result.get().getCreditCard())){
                            model.addAttribute("isAuthen",1);//已认证
                        }else{
                            model.addAttribute("isAuthen",0);//未认证
                        }
                        model.addAttribute("creditCardName",result.get().getCreditCardName());
                        model.addAttribute("creditCardShort",result.get().getCreditCardShort());
                        url = "/authentication";
                    }
                }else{
                    CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                    url = "/sqb/reg";
                    isRedirect= true;
                }
            }else{
                CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                isRedirect= true;
                url = "/sqb/reg";
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }
    }


    /**
     * 立即升级
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/toBuy/{id}", method = RequestMethod.GET)
    public String toBuy(final HttpServletRequest request, final HttpServletResponse response,final Model model,@PathVariable("id") long id) throws IOException {
        boolean isRedirect = false;
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if (userInfoOptional.isPresent()) {
                Long merchantId = userInfoOptional.get().getMerchantId();
                if (merchantId != null && merchantId != 0){
                    Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                    if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                        url = "/sqb/reg";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                        url = "/sqb/addInfo";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                        url = "/sqb/addNext";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                            result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                            result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                        url = "/sqb/prompt";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()||result.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){//跳首页
                        Optional<UpgradeRules> upgradeRulesOptional = upgradeRulesService.selectById(id);
                        if(!upgradeRulesOptional.isPresent()){//不存在
                            model.addAttribute("message","没有此级别合伙人");
                            url = "/message";
                        }else{
                            BigDecimal needMoney = needMoney(result.get().getProductId(),result.get().getLevel(),upgradeRulesOptional.get().getType());
                            UpgradePayRecord upgradePayRecord = new UpgradePayRecord();
                            upgradePayRecord.setMerchantId(merchantId);
                            upgradePayRecord.setProductId(result.get().getProductId());
                            upgradePayRecord.setBeforeLevel(result.get().getLevel());
                            upgradePayRecord.setStatus(EnumUpgrade.NORMAL.getId());
                            upgradePayRecord.setReqSn(SnGenerator.generateReqSn());
                            upgradePayRecord.setAmount(needMoney);
                            upgradePayRecord.setLevel(upgradeRulesOptional.get().getType());
                            upgradePayRecord.setUpgradeRulesId(id);
                            upgradePayRecord.setNote("充值升级");
                            upgradePayRecord.setPayResult(EnumUpgradePayResult.UNPAY.getId());
                            upgradePayRecordService.insert(upgradePayRecord);
                            String skipUrl = "http://"+ApplicationConsts.getApplicationConfig().domain()+"/sqb/buySuccess/"+needMoney+"/"+upgradePayRecord.getReqSn();
                            Pair<Integer, String> pair = payService.generateMerchantUpgradeUrl(upgradePayRecord.getMerchantId(),upgradePayRecord.getReqSn(),needMoney,skipUrl);
                            log.info("返回left:{}",pair.getLeft());
                            log.info("返回right:{}",pair.getRight());
                            if(pair.getLeft()==0){
                                isRedirect= true;
                                String payUrl = URLDecoder.decode(pair.getRight(), "UTF-8");
                                url = "/toBuy";
                                model.addAttribute("payUrl",payUrl);
                                model.addAttribute("amount",needMoney);
                            }else{
                                model.addAttribute("message",pair.getRight());
                                url = "/message";
                            }
                        }
                    }
                }else{
                    CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                    url = "/sqb/reg";
                    isRedirect= true;
                }
            }else{
                CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                isRedirect= true;
                url = "/sqb/reg";
            }
            if(isRedirect){
                log.info("直接跳转{}",url);
                return url;
            }else{
                return url;
            }
        }
    }

    /**
     * 支付升级成功页面
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/buySuccess/{amount}/{orderId}", method = RequestMethod.GET)
    public String buySuccess(final HttpServletRequest request, final HttpServletResponse response, final Model model, @PathVariable("amount") String amount, @PathVariable("orderId") String orderId) throws IOException {
        model.addAttribute("money", amount);
        model.addAttribute("firstSn", orderId.substring(0, orderId.length() - 6));
        model.addAttribute("secondSn", orderId.substring(orderId.length() - 6, orderId.length()));
        return "/buySuccess";
    }


    @RequestMapping(value = "/weixinreg", method = RequestMethod.GET)
    public String weixinreg(final HttpServletRequest request, final HttpServletResponse response, final Model model, @PathVariable("amount") String amount, @PathVariable("orderId") String orderId) throws IOException {
        model.addAttribute("money", amount);
        model.addAttribute("firstSn", orderId.substring(0, orderId.length() - 6));
        model.addAttribute("secondSn", orderId.substring(orderId.length() - 6, orderId.length()));
        return "/buySuccess";
    }

}
