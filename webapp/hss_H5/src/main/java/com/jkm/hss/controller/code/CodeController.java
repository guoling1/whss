package com.jkm.hss.controller.code;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by yulong.zhang on 2016/11/29.
 */
@Slf4j
@Controller
@RequestMapping(value = "/code")
public class CodeController extends BaseController {

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private MerchantInfoService merchantInfoService;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 扫码
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/scanCode", method = RequestMethod.GET)
    public String scanCode(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "openId", required = false) String openId) {
        final String code = request.getParameter("code");
        final String sign = request.getParameter("sign");
        log.info("scan code[{}], sign is [{}]", code, sign);
        final Optional<QRCode> qrCodeOptional = this.qrCodeService.getByCode(code);
        Preconditions.checkState(qrCodeOptional.isPresent(), "二维码不存在");
        final QRCode qrCode = qrCodeOptional.get();
        Preconditions.checkState(qrCode.isCorrectSign(sign), "sign不正确");
        final long merchantId = qrCode.getMerchantId();
        final String agent = request.getHeader("User-Agent").toLowerCase();
        log.info("User-Agent is [{}]",agent);
        String url = "";
        if (qrCode.isActivate()) {//已激活
            Optional<UserInfo> userInfoOptional = userInfoService.selectByMerchantId(merchantId);
            Preconditions.checkState(userInfoOptional.isPresent(), "用户不存在");
            String openIdTemp = userInfoOptional.get().getOpenId();
            log.info("openIdTemp{}",openIdTemp);
            log.info("code[{}] is activate", code);
            final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.selectById(merchantId);
            Preconditions.checkState(merchantInfoOptional.isPresent(), "商户不存在");
            final MerchantInfo merchantInfo = merchantInfoOptional.get();
            model.addAttribute("merchantId", merchantId);
            if (EnumMerchantStatus.INIT.getId() == merchantInfo.getStatus()) { // 初始化-->填资料
                if (agent.indexOf("micromessenger") > -1) {//weixin
                    log.info("初始化-->填资料,扫码openId{}",openId);
                    //如何没有openId跳授权页面
                    if(openId==null||"".equals(openId)){
                        String requestUrl = "";
                        if(request.getQueryString() == null){
                            requestUrl = "";
                        }else{
                            requestUrl = request.getQueryString();
                        }
                        log.info("跳转地址是{}",requestUrl);
                        try {
                            String encoderUrl = URLEncoder.encode(requestUrl, "UTF-8");
                            return "redirect:"+ WxConstants.WEIXIN_MERCHANT_USERINFO+encoderUrl+ WxConstants.WEIXIN_USERINFO_REDIRECT;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(openId.equals(openIdTemp)){
                            log.info("同一个人填第一步资料");
                            log.info("code[{}], merchant is in init; go to fill profile", code);
                            url = "/sqb/addInfo";
                        }else{
                            model.addAttribute("message", "该商户未提交审核资料\n请使用其他方式向商户付款");
                            return "/message";
                        }
                    }
                }else{
                    model.addAttribute("message", "注册收款商户请使用“微信”扫码");
                    return "/message";
                }
            } else if (EnumMerchantStatus.ONESTEP.getId() == merchantInfo.getStatus()) { // 初始化-->图片
                if (agent.indexOf("micromessenger") > -1) {//weixin
                    log.info("初始化-->图片,扫码openId{}",openId);
                    //如何没有openId跳授权页面
                    if(openId==null||"".equals(openId)){
                        String requestUrl = "";
                        if(request.getQueryString() == null){
                            requestUrl = "";
                        }else{
                            requestUrl = request.getQueryString();
                        }
                        log.info("跳转地址是{}",requestUrl);
                        try {
                            String encoderUrl = URLEncoder.encode(requestUrl, "UTF-8");
                            return "redirect:"+ WxConstants.WEIXIN_MERCHANT_USERINFO+encoderUrl+ WxConstants.WEIXIN_USERINFO_REDIRECT;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(openId.equals(openIdTemp)){
                            log.info("同一个人填第二步资料");
                            log.info("code[{}], merchant is in one step; go to fill pic", code);
                            url = "/sqb/addNext";
                        }else{
                            model.addAttribute("message", "该商户未提交审核资料\n请使用其他方式向商户付款");
                            return "/message";
                        }
                    }
                }else{
                    model.addAttribute("message", "注册收款商户请使用“微信”扫码");
                    return "/message";
                }

            } else if (EnumMerchantStatus.REVIEW.getId() == merchantInfo.getStatus()) {//待审核
                if (agent.indexOf("micromessenger") > -1) {//weixin
                    log.info("初始化-->图片,扫码openId{}",openId);
                    //如何没有openId跳授权页面
                    if(openId==null||"".equals(openId)){
                        String requestUrl = "";
                        if(request.getQueryString() == null){
                            requestUrl = "";
                        }else{
                            requestUrl = request.getQueryString();
                        }
                        log.info("跳转地址是{}",requestUrl);
                        try {
                            String encoderUrl = URLEncoder.encode(requestUrl, "UTF-8");
                            return "redirect:"+ WxConstants.WEIXIN_MERCHANT_USERINFO+encoderUrl+ WxConstants.WEIXIN_USERINFO_REDIRECT;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(openId.equals(openIdTemp)){
                            log.info("同一个人资料待审核");
                            log.info("code[{}], merchant is in due audit", code);
                            url = "/sqb/prompt";
                        }else{
                            model.addAttribute("message", "该商户未提交审核资料\n请使用其他方式向商户付款");
                            return "/message";
                        }
                    }
                }else{
                    model.addAttribute("message", "注册收款商户请使用“微信”扫码");
                    return "/message";
                }
            } else if (EnumMerchantStatus.UNPASSED.getId() == merchantInfo.getStatus()) {//审核未通过
                if (agent.indexOf("micromessenger") > -1) {//weixin
                    log.info("初始化-->图片,扫码openId{}",openId);
                    //如何没有openId跳授权页面
                    if(openId==null||"".equals(openId)){
                        String requestUrl = "";
                        if(request.getQueryString() == null){
                            requestUrl = "";
                        }else{
                            requestUrl = request.getQueryString();
                        }
                        log.info("跳转地址是{}",requestUrl);
                        try {
                            String encoderUrl = URLEncoder.encode(requestUrl, "UTF-8");
                            return "redirect:"+ WxConstants.WEIXIN_MERCHANT_USERINFO+encoderUrl+ WxConstants.WEIXIN_USERINFO_REDIRECT;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(openId.equals(openIdTemp)){
                            log.info("同一个人资料未审核通过");
                            log.info("code[{}], merchant is not pass", code);
                            url = "/sqb/prompt";
                        }else{
                            model.addAttribute("message", "该商户未提交审核资料\n请使用其他方式向商户付款");
                            return "/message";
                        }
                    }
                }else{
                    model.addAttribute("message", "注册收款商户请使用“微信”扫码");
                    return "/message";
                }
            } else if (EnumMerchantStatus.PASSED.getId() == merchantInfo.getStatus()||EnumMerchantStatus.FRIEND.getId() == merchantInfo.getStatus()) {//审核通过
                model.addAttribute("name", merchantInfo.getMerchantName());
                log.info("设备标示{}",agent.indexOf("micromessenger"));
                if (agent.indexOf("micromessenger") > -1) {//weixin
                    url = "/sqb/paymentWx";
                }
                if (agent.indexOf("aliapp") > -1) {// AliApp
                    url = "/sqb/paymentZfb";
                }
            }else if (EnumMerchantStatus.LOGIN.getId() == merchantInfo.getStatus()) {//注册
                model.addAttribute("code", code);
                url =  "/sqb/reg";
            } else {
                log.error("code[{}] is activate, but merchant[{}] is disabled", code, merchantId);
            }
        } else {//注册
            if(agent.indexOf("micromessenger") > -1){
                model.addAttribute("code", code);
                url =  "/sqb/reg";
            }else{
                model.addAttribute("message", "注册收款商户请使用“微信”扫码");
                return "/message";
            }
        }
        return "redirect:"+url;
    }
}
