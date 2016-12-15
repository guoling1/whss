package com.jkm.hss.controller.wx;


import com.google.common.base.Optional;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.service.ShallProfitDetailService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.merchant.entity.AccountInfo;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.helper.WxPubUtil;
import com.jkm.hss.merchant.service.AccountInfoService;
import com.jkm.hss.merchant.service.MerchantInfoCheckRecordService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
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
    private ShallProfitDetailService shallProfitDetailService;

    /**
     * 登录页面
     * @param request
     * @param response
     * @return
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
            Optional<UserInfo> userInfoOptional = userInfoService.selectById(super.getUserId(request));
            if (userInfoOptional.isPresent()) {
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
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()){//跳首页
                        url = "/sqb/wallet";
                        isRedirect= true;
                    }
                }else {
                    if(code!=null&&!"".equals(code)){
                        model.addAttribute("qrCode",code);
                    }
                    url = "/reg";
                }
            }else {
                CookieUtil.deleteCookie(response, ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
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
            Optional<UserInfo> userInfoOptional = userInfoService.selectById(super.getUserId(request));
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
                        url = "/material";
                    }else if(result.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                        url = "/sqb/addNext";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                            result.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                            result.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                        url = "/sqb/prompt";
                        isRedirect= true;
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()){//跳首页
                        url = "/sqb/wallet";
                        isRedirect= true;
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
            Optional<UserInfo> userInfoOptional = userInfoService.selectById(super.getUserId(request));
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
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()){//跳首页
                        url = "/sqb/wallet";
                        isRedirect= true;
                    }
                }else{
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
     * 展示审核状态
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
            Optional<UserInfo> userInfoOptional = userInfoService.selectById(super.getUserId(request));
            Optional<MerchantInfo> result = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
            String res = merchantInfoCheckRecordService.selectById(userInfoOptional.get().getMerchantId());
            if (result.get().getStatus()==EnumMerchantStatus.UNPASSED.getId()){
                model.addAttribute("res",res);
                model.addAttribute("id",result.get().getId());
                return "/prompt";
            }if (result.get().getStatus()==EnumMerchantStatus.REVIEW.getId()){
                model.addAttribute("res","您的资料正在审核中，请耐心等待");
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
        return "/follow";
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
    public String qrCode(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "qrCode", required = true) String qrCode,
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
    public String reg(final HttpServletRequest request, final Model model) throws IOException {
        if(!super.isLogin(request)){
            model.addAttribute("avaliable", "0.00");
        }else{
            Optional<UserInfo> userInfoOptional = userInfoService.selectById(super.getUserId(request));
            if(userInfoOptional.get().getMerchantId()!=0){
                Optional<MerchantInfo> merchantInfo = this.merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
                if(merchantInfo.isPresent()){
                    AccountInfo accountInfo = accountInfoService.selectByPrimaryKey(merchantInfo.get().getAccountId());
                    if(accountInfo==null){
                        model.addAttribute("avaliable", "0.00");
                    }else{
                        DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                        model.addAttribute("avaliable", accountInfo.getAvailable()==null?"0.00":decimalFormat.format(accountInfo.getAvailable()));
                    }

                }else{
                    model.addAttribute("avaliable", "0.00");
                }
            }else{
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
        String merchantName = "";
        Optional<UserInfo> userInfoOptional = userInfoService.selectById(super.getUserId(request));
        if(userInfoOptional.isPresent()){
            Optional<MerchantInfo> merchantInfo = this.merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
            if(merchantInfo.isPresent()){
                merchantName = merchantInfo.get().getMerchantName();
            }
        }
        model.addAttribute("merchantName", merchantName);
        return "/collection";
    }
    /**
     * 支付成功页面
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/success/{amount}/{orderId}", method = RequestMethod.GET)
    public String success(final HttpServletRequest request, final HttpServletResponse response, final Model model, @PathVariable("amount") String amount, @PathVariable("orderId") String orderId) throws IOException {
        model.addAttribute("money", amount);
        model.addAttribute("firstSn", orderId.substring(0,orderId.length()-6));
        model.addAttribute("secondSn", orderId.substring(orderId.length()-6,orderId.length()));
        return "/success";
    }

    @RequestMapping(value = "/paymentWx", method = RequestMethod.GET)
    public String paymentWx(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "merchantId", required = true) long merchantId,@RequestParam(value = "name") String name) throws IOException {
        model.addAttribute("mid", merchantId);
        model.addAttribute("merchantName", name);
        return "/payment-wx";
    }
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
            Optional<UserInfo> userInfoOptional = userInfoService.selectById(super.getUserId(request));
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
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()){//跳提现页面
                        String phone = MerchantSupport.decryptMobile(result.get().getReserveMobile());
                        String bankNo = MerchantSupport.decryptBankCard(result.get().getBankNo());
                        model.addAttribute("phone_01", phone.substring(0,3));
                        model.addAttribute("phone_02", phone.substring(phone.length()-4,phone.length()));
                        model.addAttribute("bankNo", bankNo.substring(bankNo.length()-4,bankNo.length()));
                        model.addAttribute("bankName",result.get().getBankName());
                        AccountInfo accountInfo = accountInfoService.selectByPrimaryKey(result.get().getAccountId());
                        DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                        if(accountInfo==null){//没有账户
                            model.addAttribute("channelFee","0.00");
                            model.addAttribute("avaMoney", "0.00");
                            model.addAttribute("realMoney","0.00");
                        }else{
                            Pair<BigDecimal, BigDecimal> pair = shallProfitDetailService.withdrawParams(merchantId);
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

    @RequestMapping(value = "/ticket", method = RequestMethod.GET)
    public String ticket(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        return "redirect:"+ WxConstants.WEIXIN_TICKET_USERINFO;
    }

    @RequestMapping(value = "/tradeRecord", method = RequestMethod.GET)
    public String tradRecord(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws IOException {
        boolean isRedirect = false;
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectById(super.getUserId(request));
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
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()){//跳提现页面
                        url = "/tradeRecord";
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

    @RequestMapping(value = "/bank", method = RequestMethod.GET)
    public String bank(final HttpServletRequest request, final HttpServletResponse response,final Model model) throws IOException {
        boolean isRedirect = false;
        if(!super.isLogin(request)){
            return "redirect:"+ WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }else {
            String url = "";
            Optional<UserInfo> userInfoOptional = userInfoService.selectById(super.getUserId(request));
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
                    }else if(result.get().getStatus()== EnumMerchantStatus.PASSED.getId()){//跳提现页面
                        String bankNo = MerchantSupport.decryptBankCard(result.get().getBankNo());
                        model.addAttribute("bankName", result.get().getBankName());
                        model.addAttribute("bankNo",bankNo.substring(bankNo.length()-4,bankNo.length()));
                        model.addAttribute("bankBin",result.get().getBankBin());
                        url = "/bank";
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

}
