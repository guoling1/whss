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
            final String queryString = request.getQueryString();
            final StringBuffer requestURL = request.getRequestURL();
            String oemNo = request.getParameter("oemNo");
            if(oemNo!=null&&!"".equals(oemNo)){
                log.info("omeNo:"+oemNo);
                Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(oemNo);
                String url = "";
                if(oemInfoOptional.isPresent()){
                    log.info("有分公司");
                    String tempUrl = StringUtils.isNotBlank(queryString) ?
                            requestURL.toString() +"?" + queryString +"&oemNo="+oemNo:requestURL.toString()+"?oemNo="+oemNo;
                    String encoderUrl = URLEncoder.encode(tempUrl, "UTF-8");
                    url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+oemInfoOptional.get().getAppId()+"&redirect_uri=http%3a%2f%2fhss.qianbaojiajia.com%2fwx%2ftoOemSkip&response_type=code&scope=snsapi_base&state="+encoderUrl+"#wechat_redirect";
                }else{
                    log.info("无分公司");
                    String tempUrl = StringUtils.isNotBlank(queryString) ?
                            requestURL.toString() +"?"+ queryString : requestURL.toString()+"?oemNo="+oemNo;
                    url = WxConstants.WEIXIN_USERINFO+tempUrl+ WxConstants.WEIXIN_USERINFO_REDIRECT;
                }
                response.sendRedirect(url);
                return false;
            }else{
                String tempUrl = StringUtils.isNotBlank(queryString) ?
                        requestURL.toString() +"?"+ queryString : requestURL.toString();
                String url = WxConstants.WEIXIN_USERINFO+tempUrl+ WxConstants.WEIXIN_USERINFO_REDIRECT;
                response.sendRedirect(url);
                return false;
            }
        }
        log.info("有cookie直接跳走");
        return super.preHandle(request, response, handler);
    }
}
