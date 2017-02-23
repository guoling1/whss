package com.jkm.hss.controller;

import com.google.common.base.Optional;
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
 * Created by yuxiang on 2016/11/16
 */
@Slf4j
@Controller
@RequestMapping(value = "")
public class IndexController extends BaseController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MerchantInfoService merchantInfoService;

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping(value = "/**", method = RequestMethod.GET)
    public String wallet(final HttpServletRequest request) {
        return "index";
    }
    /**
     * 好收收代理商注册
     *
     * @return
     */
    @RequestMapping(value = "/reg", method = RequestMethod.GET)
    public String reg(final HttpServletRequest request, final HttpServletResponse response, final Model model,@RequestParam(value = "invite", required = true) String invite) {
        boolean isRedirect = false;
        String url = "";
        if(invite==null||"".equals(invite)){
            model.addAttribute("message", "请求地址有误，缺失必须参数");
            url = "/message";
            return url;
        }
        if (!super.isLogin(request)) {
            String requestUrl = "";
            if (request.getQueryString() == null) {
                requestUrl = "";
            } else {
                requestUrl = request.getQueryString();
            }
            log.info("跳转地址是{}", requestUrl);
            try {
                String encoderUrl = URLEncoder.encode(requestUrl, "UTF-8");
                return "redirect:" + WxConstants.WEIXIN_DEALER_USERINFO + encoderUrl + WxConstants.WEIXIN_USERINFO_REDIRECT;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
            if (userInfoOptional.isPresent()) {
                Long merchantId = userInfoOptional.get().getMerchantId();
                if (merchantId != null && merchantId != 0){
                    Optional<MerchantInfo> result = merchantInfoService.selectById(merchantId);
                    if (result.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                        model.addAttribute("inviteCode", invite);
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
                    log.info("邀请二维码没有商户信息，码{}，用户编码{}，商户编码{},openId[{}]",invite,userInfoOptional.get().getId(),merchantId,super.getOpenId(request));
                    model.addAttribute("message","您的注册信息有误");
                    url = "/message";
                }
            } else {
                model.addAttribute("inviteCode", invite);
                url = "/reg";
            }
            if (isRedirect) {
                return "redirect:" + url;
            } else {
                return url;
            }
        }
    }
}
