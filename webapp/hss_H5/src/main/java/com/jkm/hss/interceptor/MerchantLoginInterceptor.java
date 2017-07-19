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
import com.jkm.hss.merchant.helper.WxPubUtil;
import com.jkm.hss.merchant.service.MerchantInfoCheckRecordService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.RequestUrlParamService;
import com.jkm.hss.merchant.service.UserInfoService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.Map;

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
    @Setter
    private MerchantInfoCheckRecordService merchantInfoCheckRecordService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tempUrl = request.getAttribute(ApplicationConsts.REQUEST_URL).toString();
        RequestUrlParam requestUrlParam = new RequestUrlParam();
        requestUrlParam.setRequestUrl(tempUrl);
        requestUrlParamService.insert(requestUrlParam);
        String oemNo = request.getParameter("oemNo");
        if(oemNo!=null&&"null".equals(oemNo)){
            oemNo = "";
        }
        if ("".equals(CookieUtil.getCookie(request,ApplicationConsts.MERCHANT_COOKIE_KEY))) {
            if(oemNo!=null&&!"".equals(oemNo)){
                Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(oemNo);
                Preconditions.checkState(oemInfoOptional.isPresent(), "参数不合法");
                String url = WxConstants.WEIXIN_MERCHANT_USERINFO_START+oemInfoOptional.get().getAppId()+WxConstants.WEIXIN_MERCHANT_USERINFO_END+requestUrlParam.getId()+WxConstants.WEIXIN_USERINFO_REDIRECT;
                response.sendRedirect(url);
                return false;
            }else{
                String url = WxConstants.WEIXIN_USERINFO_NEW+requestUrlParam.getId()+ WxConstants.WEIXIN_USERINFO_REDIRECT;
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
                    Preconditions.checkState(oemInfoOptional.isPresent(), "分公司不存在");
                    Preconditions.checkState(merchantInfoOptional.get().getOemId()>0, "参数有误");
                    if(merchantInfoOptional.get().getOemId()>0){
                        if(oemInfoOptional.get().getDealerId()!=merchantInfoOptional.get().getOemId()){//不是同一个分公司的商户
                            CookieUtil.deleteCookie(response,ApplicationConsts.MERCHANT_COOKIE_KEY,ApplicationConsts.getApplicationConfig().domain());
                            response.sendRedirect("http://hss.qianbaojiajia.com/sqb/reg?oemNo="+oemNo);
                            return false;
                        }
                    }
                }else{//当前商户应为总公司商户：1.如果为分公司，清除cookie 2.总公司商户，不做处理
                    if(merchantInfoOptional.get().getOemId()>0){//分公司商户
                        Optional<OemInfo> oemInfoOptional2= oemInfoService.selectOemInfoByDealerId(merchantInfoOptional.get().getOemId());
                        Preconditions.checkState(oemInfoOptional2.isPresent(), "分公司不存在");
                        String queryString = request.getQueryString();
                        StringBuffer requestURL = request.getRequestURL();
                        String redirectUrl = "";
                        if(StringUtils.isNotBlank(queryString)){
                            String[] arr = queryString.split("&");
                            for(int i=0;i<arr.length;i++){
                                String[] arr2= arr[i].split("=");
                                if("oemNo".equals(arr2[0])){
                                    queryString = queryString.replace("oemNo=","oemNo="+oemInfoOptional2.get().getOemNo());
                                }
                            }
                            redirectUrl = requestURL.toString() + "?" + queryString;
                        }else{
                            redirectUrl = requestURL.toString()+"?oemNo="+oemInfoOptional2.get().getOemNo();
                        }
                        response.sendRedirect(redirectUrl);
                        return false;
                    }
                }
                if (merchantInfoOptional.get().getStatus()== EnumMerchantStatus.LOGIN.getId()){//登录
                    response.sendRedirect("http://hss.qianbaojiajia.com/sqb/reg?oemNo="+oemNo);
                    return false;
                }else if(merchantInfoOptional.get().getStatus()== EnumMerchantStatus.INIT.getId()){
                    String appId = WxConstants.APP_ID;
                    String appSecret = WxConstants.APP_KEY;
                    if(oemNo!=null&&!"".equals(oemNo)){
                        Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(oemNo);
                        appId=oemInfoOptional.get().getAppId();
                        appSecret = oemInfoOptional.get().getAppSecret();
                        request.setAttribute("oemName",oemInfoOptional.get().getBrandName());
                    }else{
                        request.setAttribute("oemName","好收收");
                    }
                    Map<String, String> res = WxPubUtil.sign(tempUrl,appId,appSecret);
                    request.setAttribute("config",res);
                    request.setAttribute("markCode",merchantInfoOptional.get().getMarkCode());
                    request.getRequestDispatcher("/WEB-INF/jsp/material.jsp").forward(request, response);
                    return false;
                }else if(merchantInfoOptional.get().getStatus()== EnumMerchantStatus.ONESTEP.getId()){
                    String appId = WxConstants.APP_ID;
                    String appSecret = WxConstants.APP_KEY;
                    if(oemNo!=null&&!"".equals(oemNo)){
                        Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(oemNo);
                        appId=oemInfoOptional.get().getAppId();
                        appSecret = oemInfoOptional.get().getAppSecret();
                        request.setAttribute("oemName",oemInfoOptional.get().getBrandName());
                    }else{
                        request.setAttribute("oemName","好收收");
                    }
                    Map<String, String> res = WxPubUtil.sign(tempUrl,appId,appSecret);
                    request.setAttribute("config",res);
                    request.getRequestDispatcher("/WEB-INF/jsp/upload.jsp").forward(request, response);
                    return false;
                }else if(merchantInfoOptional.get().getStatus()== EnumMerchantStatus.REVIEW.getId()||
                        merchantInfoOptional.get().getStatus()== EnumMerchantStatus.UNPASSED.getId()||
                        merchantInfoOptional.get().getStatus()== EnumMerchantStatus.DISABLE.getId()){
                    if(oemNo!=null&&!"".equals(oemNo)){
                        Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(oemNo);
                        request.setAttribute("oemName",oemInfoOptional.get().getBrandName());
                    }else{
                        request.setAttribute("oemName","好收收");
                    }
                    String res = merchantInfoCheckRecordService.selectById(userInfoOptional.get().getMerchantId());
                    if (merchantInfoOptional.get().getStatus()==EnumMerchantStatus.UNPASSED.getId()){
                        request.setAttribute("res",res);
                        request.setAttribute("id",merchantInfoOptional.get().getId());
                        request.getRequestDispatcher("/WEB-INF/jsp/prompt.jsp").forward(request, response);
                    }if (merchantInfoOptional.get().getStatus()==EnumMerchantStatus.REVIEW.getId()){
                        request.setAttribute("res","您的资料已经提交，我们将在一个工作日内处理");
                        request.getRequestDispatcher("/WEB-INF/jsp/prompt1.jsp").forward(request, response);
                    }
                    return false;
                }
            }else{
                response.sendRedirect("http://hss.qianbaojiajia.com/sqb/reg?oemNo="+oemNo);
                return false;
            }
        }
        return super.preHandle(request, response, handler);
    }
}
