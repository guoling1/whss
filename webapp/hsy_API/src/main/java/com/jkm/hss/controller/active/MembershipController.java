package com.jkm.hss.controller.active;

import com.jkm.base.common.spring.alipay.constant.AlipayServiceConstants;
import com.jkm.base.common.spring.alipay.service.AlipayOauthService;
import com.jkm.hss.entity.AuthInfo;
import com.jkm.hss.entity.AuthParam;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.helper.WxPubUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Allen on 2017/5/10.
 */
@Slf4j
@Controller
@RequestMapping(value = "/membership")
public class MembershipController {

    @Autowired
    private AlipayOauthService alipayOauthService;

    @RequestMapping("getAuth/{sidEncode}")
    public String getAuth(HttpServletRequest request, HttpServletResponse response,@PathVariable String sidEncode){
        String agent = request.getHeader("User-Agent").toLowerCase();
        if (agent.indexOf("micromessenger") > -1){
            return "redirect:"+ WxConstants.WEIXIN_HSY_MEMBERSHIP_AUTHINFO+sidEncode+"%7CWX"+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }
        if (agent.indexOf("aliapp") > -1) {
            System.out.println(AlipayServiceConstants.OAUTH_URL+sidEncode+"|ZFB"+AlipayServiceConstants.OAUTH_URL_LATER+AlipayServiceConstants.MEMBERSHIP_REDIRECT_URI);
            return "redirect:"+ AlipayServiceConstants.OAUTH_URL+sidEncode+"|ZFB"+AlipayServiceConstants.OAUTH_URL_LATER+AlipayServiceConstants.MEMBERSHIP_REDIRECT_URI;
        }
        return "/tips";
    }

    @RequestMapping("getAuthInfo")
    public String getAuthInfo(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, AuthParam authParam){
        log.info("回跳参数:"+authParam.toString());
        boolean successFlag=true;
        String info="";
        if(authParam.getState()!=null) {
            if (authParam.getState().endsWith("WX")) {
                Map<String,String> ret = WxPubUtil.getOpenid(authParam.getCode(), WxConstants.APP_HSY_ID,WxConstants.APP_HSY_SECRET);
                String openID=ret.get("openid");
                redirectAttributes.addAttribute("openID",openID);
                if(openID==null||openID.equals(""))
                {
                    successFlag = false;
                    info = "获取微信OPENID失败";
                }
            } else if (authParam.getState().endsWith("ZFB")) {
                try {
                    String userID = alipayOauthService.getUserId(authParam.getAuth_code());
                    redirectAttributes.addAttribute("userID", userID);
                    redirectAttributes.addAttribute("sidEncode", authParam.getState().split("|")[0]);
                    if(userID==null||userID.equals(""))
                    {
                        successFlag = false;
                        info = "获取支付宝USERID失败";
                    }
                } catch (Exception e) {
                    successFlag = false;
                    info = "获取支付宝USERID失败";
                }
            } else {
                successFlag = false;
                info = "不支持该支付类型";
            }
        }
        else
        {
            successFlag = false;
            info = "授权失败";
        }
        redirectAttributes.addAttribute("successFlag", successFlag);
        redirectAttributes.addAttribute("infoDetail", info);
        return "redirect:/membership/checkMember";
    }

    @RequestMapping("checkMember")
    public String checkMember(HttpServletRequest request, HttpServletResponse response, Model model,AuthInfo authInfo){
        if(authInfo!=null&&authInfo.getInfoDetail()!=null&&!authInfo.getInfoDetail().equals("")) {
            try {
                authInfo.setInfoDetail(new String(authInfo.getInfoDetail().getBytes("iso-8859-1"), "utf-8"));
            } catch (Exception e) {

            }
        }
        log.info("会员开始参数:"+authInfo.toString());
        model.addAttribute("authInfo",authInfo);
        return "/memberinfo";
    }
}
