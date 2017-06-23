package com.jkm.hss.interceptor;


import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.hss.dealer.entity.OemInfo;
import com.jkm.hss.dealer.service.OemInfoService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.RequestUrlParam;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.service.MerchantInfoService;
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
    @Setter
    private MerchantInfoService merchantInfoService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String encoderUrl = URLEncoder.encode(request.getAttribute(ApplicationConsts.REQUEST_URL).toString(), "UTF-8");
        RequestUrlParam requestUrlParam = new RequestUrlParam();
        requestUrlParam.setRequestUrl(encoderUrl);
        requestUrlParamService.insert(requestUrlParam);
        String oemNo = request.getParameter("oemNo");
        request.setAttribute("oemNo",oemNo);
        if ("".equals(CookieUtil.getCookie(request,ApplicationConsts.MERCHANT_COOKIE_KEY))) {
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
            if(userInfoOptional.isPresent()){
                Preconditions.checkState(userInfoOptional.get().getMerchantId()>0, "商户不存在");
                Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
                Preconditions.checkState(merchantInfoOptional.isPresent(), "商户不存在");
                if(oemNo!=null&&!"".equals(oemNo)){//当前商户应为分公司商户:1.如果为总公司，清除cookie 2.如果为分公司，判断是否是同一个分公司，是：继续，不是：清除cookie
                    Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(oemNo);
                    Preconditions.checkState(oemInfoOptional.isPresent(), "参数不合法");
                    if(merchantInfoOptional.get().getOemId()>0){
                        if(oemInfoOptional.get().getId()!=merchantInfoOptional.get().getOemId()){
                            CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                            response.sendRedirect(request.getAttribute(ApplicationConsts.REQUEST_URL).toString());
                            return false;
                        }
                    }else{//由金开门切到分公司
                        CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                        response.sendRedirect(request.getAttribute(ApplicationConsts.REQUEST_URL).toString());
                        return false;
                    }
                }else{//当前商户应为总公司商户：1.如果为分公司，清除cookie 2.总公司商户，不做处理
                    if(merchantInfoOptional.get().getOemId()>0){//分公司商户
                        CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                        response.sendRedirect(request.getAttribute(ApplicationConsts.REQUEST_URL).toString());
                        return false;
                    }
                }
                if (merchantInfoOptional.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                    response.sendRedirect("/sqb/reg");
                    return false;
                }else if(merchantInfoOptional.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                    response.sendRedirect("/sqb/addInfo");
                    return false;
                }else if(merchantInfoOptional.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                    response.sendRedirect("/sqb/addNext");
                    return false;
                }else if(merchantInfoOptional.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                        merchantInfoOptional.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                        merchantInfoOptional.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                    response.sendRedirect("/sqb/prompt");
                    return false;
                }else if(merchantInfoOptional.get().getStatus()== EnumMerchantStatus.PASSED.getId()||merchantInfoOptional.get().getStatus()== EnumMerchantStatus.FRIEND.getId()){//跳首页
                    response.sendRedirect("/sqb/wallet");
                    return false;
                }
            }else{
                CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                response.sendRedirect("/sqb/reg");
                return false;
            }
        }
        return super.preHandle(request, response, handler);
    }
}
