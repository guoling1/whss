package com.jkm.hss.controller;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yuxiang on 2016/11/16
 */
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
                model.addAttribute("inviteCode", invite);
                url = "/reg";
            }
            if(isRedirect){
                return "redirect:"+url;
            }else{
                return url;
            }
        }
    }
}
