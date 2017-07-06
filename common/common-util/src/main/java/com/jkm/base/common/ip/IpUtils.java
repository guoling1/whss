package com.jkm.base.common.ip;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by huangwei on 6/2/15.
 */
public final class IpUtils {

    private IpUtils() {
    }

    /**
     * 获取请求ip地址
     *
     * @param request
     * @return
     */
    public static String getIp(final HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.trim().equals("") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.trim().equals("") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.trim().equals("") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.trim().equals("") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.trim().equals("") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip != null && !ip.trim().equals("")) {
            if (ip.indexOf(',') > 0) {
                ip = ip.substring(0, ip.indexOf(','));
            }
            return ip;
        } else {
            return "0.0.0.0";
        }
    }

    /**
     * 将127.0.0.1形式的IP地址转换成十进制整数，这里没有进行任何错误处理
     *
     * @param strIp
     * @return
     */
    public static long ip2long(final String strIp) {
        final long[] ip = new long[4];
        final int position1 = strIp.indexOf('.');
        final int position2 = strIp.indexOf('.', position1 + 1);
        final int position3 = strIp.indexOf('.', position2 + 1);

        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));

        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**
     * 将十进制整数形式转换成127.0.0.1形式的ip地址
     *
     * @param longIp
     * @return
     */
    public static String long2ip(final long longIp) {
        final StringBuilder sb = new StringBuilder();
        sb.append(longIp >>> 24).append('.')
                .append((longIp & 0x00FFFFFF) >>> 16).append('.')
                .append((longIp & 0x0000FFFF) >>> 8).append('.')
                .append(longIp & 0x000000FF);
        return sb.toString();
    }
}
