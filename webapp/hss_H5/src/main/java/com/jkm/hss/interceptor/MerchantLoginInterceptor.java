package com.jkm.hss.interceptor;


import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.hss.dealer.entity.OemInfo;
import com.jkm.hss.dealer.service.OemInfoService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.merchant.entity.RequestUrlParam;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.service.RequestUrlParamService;
import com.jkm.hss.merchant.service.UserInfoService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-28 18:18
 */
@Slf4j
public class MerchantLoginInterceptor extends HandlerInterceptorAdapter {
    @Setter
    private OemInfoService oemInfoService;
    @Setter
    private RequestUrlParamService requestUrlParamService;
    @Setter
    private UserInfoService userInfoService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String encoderUrl = URLEncoder.encode(request.getAttribute(ApplicationConsts.REQUEST_URL).toString(), "UTF-8");
        RequestUrlParam requestUrlParam = new RequestUrlParam();
        requestUrlParam.setRequestUrl(encoderUrl);
        requestUrlParamService.insert(requestUrlParam);
        if ("".equals(CookieUtil.getCookie(request,ApplicationConsts.MERCHANT_COOKIE_KEY))) {
            String oemNo = request.getParameter("oemNo");
            if(oemNo!=null&&!"".equals(oemNo)){
                Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(oemNo);
                Preconditions.checkState(oemInfoOptional.isPresent(), "参数不合法");
                String url = WxConstants.WEIXIN_MERCHANT_USERINFO_START+oemInfoOptional.get().getAppId()+WxConstants.WEIXIN_MERCHANT_USERINFO_END+requestUrlParam.getUuid()+WxConstants.WEIXIN_USERINFO_REDIRECT;
                response.sendRedirect(url);
                return false;
            }else{
                String url = WxConstants.WEIXIN_USERINFO+requestUrlParam.getUuid()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
                response.sendRedirect(url);
                return false;
            }
        }else{
            Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(CookieUtil.getCookie(request,ApplicationConsts.MERCHANT_COOKIE_KEY));
            Preconditions.checkState(userInfoOptional.isPresent(), "商户不存在");
        }
        return super.preHandle(request, response, handler);
    }
}
