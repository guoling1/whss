package com.jkm.base.common.util;

import com.google.common.base.Strings;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hutao on 15/8/4.
 * 下午5:32
 */
public final class RequestUtil {
    private RequestUtil() {
    }

    /**
     * 判定是否是ajax请求
     *
     * @param request request
     * @return bool
     */
    public static boolean isAjaxHttpRequest(final HttpServletRequest request) {
        final String requestType = request.getHeader("X-Requested-With");
        return !Strings.isNullOrEmpty(requestType) && "XMLHttpRequest".equalsIgnoreCase(requestType);
    }
}
