package com.jkm.base.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by huangwei on 2/27/15.
 */
public final class CookieUtil {

    private CookieUtil() {
    }

    /**
     * 获取cookie
     *
     * @param request
     * @param key
     * @return
     */
    public static String getCookie(final HttpServletRequest request, final String key) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "";
        }

        for (final Cookie cookie : cookies) {
            if (key.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return "";
    }

    /**
     * 设置持久cookie
     *
     * @param response
     * @param key
     * @param value
     * @param domain
     * @return
     */
    public static void setPersistentCookie(final HttpServletResponse response, final String key, final String value, final String domain) {
        if (StringUtils.isBlank(domain)) {
            throw new IllegalArgumentException("cookie domain is null!");
        }

        final Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(60 * 60 * 24 * 365); // 有效期1年
        response.addCookie(cookie);
    }

    /**
     * 设置 Session cookie
     *
     * @param response
     * @param key
     * @param value
     * @param domain
     * @return
     */
    public static void setSessionCookie(final HttpServletResponse response, final String key, final String value, final String domain, final int seconds) {
        if (StringUtils.isBlank(domain)) {
            throw new IllegalArgumentException("cookie domain is null!");
        }

        final Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(seconds);
        response.addCookie(cookie);
    }

    /**
     * 设置 Session cookie
     *
     * @param response
     * @param key
     * @param value
     * @param domain
     * @return
     */
    public static void setSessionCookie(final HttpServletResponse response, final String key, final String value, final String domain) {
        if (StringUtils.isBlank(domain)) {
            throw new IllegalArgumentException("cookie domain is null!");
        }

        final Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(-1); // 会话级别，关闭浏览器失效
        response.addCookie(cookie);
    }

    /**
     * 删除cookie
     *
     * @param response
     * @param key
     * @param domain
     */
    public static void deleteCookie(final HttpServletResponse response, final String key, final String domain) {
        if (StringUtils.isBlank(domain)) {
            throw new IllegalArgumentException("cookie domain is null!");
        }
        final Cookie cookie = new Cookie(key, null);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /**
     * 设置一年cookie
     * @param response
     * @param value
     */
    public static void setCookie(final HttpServletResponse response,final String key, final String value) {
        Cookie cookie = new Cookie(key,value);
        cookie.setMaxAge(60 * 60 * 24 * 365);
        response.addCookie(cookie);
    }
}
