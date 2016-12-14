package com.jkm.base.common.ip;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

/**
 * Created by huangwei on 6/2/15.
 * TODO 支持多平台，返回统一格式地址信息
 */
public final class IpLocation {

    private IpLocation() {
    }

    /**
     * 根据ip获取地址信息
     *
     * @param ip
     * @return
     */
    public static IpInfo getIpInfo(final String ip) {
        String ipInfoJson = null;
        try {
            ipInfoJson = Request.Get("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip).execute().returnContent().asString();
        } catch (IOException e) {
            throw new RuntimeException("", e);
        }
        final JSONObject jsonObject = JSON.parseObject(ipInfoJson);
        if (jsonObject.getIntValue("code") == 1) {
            return null;
        }
        return jsonObject.getObject("data", IpInfo.class);
    }

}
