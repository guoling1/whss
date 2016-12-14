package com.jkm.base.common.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by huangwei on 1/26/16.
 */
public class HealthCheckFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isHttpHead(request)) {
            response.setContentLength(0);
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isHttpHead(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        return "head".equalsIgnoreCase(httpServletRequest.getMethod());
    }

    @Override
    public void destroy() {

    }

}
