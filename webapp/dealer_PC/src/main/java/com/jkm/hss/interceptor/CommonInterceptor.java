package com.jkm.hss.interceptor;

import com.jkm.hss.helper.ApplicationConsts;
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
        log.info("请求地址：{}",request.getRequestURL());
        log.info("请求方式：{}",request.getMethod());
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
