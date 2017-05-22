package com.jkm.hss.interceptor;


import com.google.common.base.Optional;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.hss.dealer.entity.OemInfo;
import com.jkm.hss.dealer.service.OemInfoService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.merchant.helper.WxConstants;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("".equals(CookieUtil.getCookie(request,ApplicationConsts.MERCHANT_COOKIE_KEY))) {//get请求走获取openId
            String omeNo = request.getParameter("omeNo");
            if(omeNo!=null&&!"".equals(omeNo)){
                log.info("omeNo:"+omeNo);
                Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(omeNo);
                String url = "";
                if(oemInfoOptional.isPresent()){
                    log.info("有分公司");
                    String tempUrl = "omeNo="+omeNo;
                    String encoderUrl = URLEncoder.encode(tempUrl, "UTF-8");
                    url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+oemInfoOptional.get().getAppId()+"&redirect_uri=http%3a%2f%2fhss.qianbaojiajia.com%2fwx%2ftoOemSkip&response_type=code&scope=snsapi_base&state="+encoderUrl+"#wechat_redirect";
                }else{
                    log.info("无分公司");
                    url = WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
                }
                response.sendRedirect(url);
                return false;
            }else{
                String url = WxConstants.WEIXIN_USERINFO+request.getRequestURI()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
                response.sendRedirect(url);
                return false;
            }

        }
        log.info("直接跳走");
        return super.preHandle(request, response, handler);
    }
}
