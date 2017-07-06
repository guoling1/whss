package com.jkm.hss.interceptor;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.hss.dealer.entity.OemInfo;
import com.jkm.hss.dealer.service.OemInfoService;
import com.jkm.hss.helper.ApplicationConsts;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
@Slf4j
public class CommonInterceptor extends HandlerInterceptorAdapter {
    @Setter
    private OemInfoService oemInfoService;
    /**
     * 添加一些公共属性
     * 例如request_url
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        log.info("请求地址：{}",StringUtils.isNotBlank(request.getQueryString()) ?
                request.getRequestURL() + "?" + request.getQueryString() : request.getRequestURL().toString());
        String oemNo = request.getParameter("oemNo");
        if(oemNo==null){
            oemNo = "";
        }
        if(oemNo!=null&&!"".equals(oemNo)){
            Optional<OemInfo> oemInfoOptional =  oemInfoService.selectByOemNo(oemNo);
            Preconditions.checkState(oemInfoOptional.isPresent(), "O单参数有误");
        }
        setRequestUrlAttribute(request);
        return super.preHandle(request, response, handler);
    }

    private void setRequestUrlAttribute(final HttpServletRequest request) {
        final String queryString = request.getQueryString();
        final StringBuffer requestURL = request.getRequestURL();
        request.setAttribute(ApplicationConsts.REQUEST_URL, StringUtils.isNotBlank(queryString) ?
                requestURL + "?" + queryString : request.getRequestURL().toString());
    }
}
