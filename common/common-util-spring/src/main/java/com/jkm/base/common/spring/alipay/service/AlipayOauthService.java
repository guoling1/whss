package com.jkm.base.common.spring.alipay.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.alipay.api.response.AlipayUserUserinfoShareResponse;

/**
 * Created by xingliujie on 2017/4/19.
 */
public interface AlipayOauthService {
    /**
     * 获取授权信息
     * @param authCode
     * @return
     */
    String getUserId(String authCode) throws AlipayApiException;

}
