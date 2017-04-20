package com.jkm.base.common.spring.alipay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserUserinfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserUserinfoShareResponse;
import com.jkm.base.common.spring.alipay.constant.AlipayServiceConstants;
import com.jkm.base.common.spring.alipay.service.AlipayOauthService;
import com.jkm.base.common.spring.alipay.util.AlipayAPIClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by xingliujie on 2017/4/19.
 */
@Slf4j
@Service
public class AlipayOauthServiceImpl implements AlipayOauthService {
    /**
     * 获取授权信息
     *
     * @param authCode
     * @return
     */
    @Override
    public AlipayUserUserinfoShareResponse getUserInfo(String authCode) throws AlipayApiException {
        AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
        oauthTokenRequest.setCode(authCode);
        oauthTokenRequest.setGrantType(AlipayServiceConstants.GRANT_TYPE);
        AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
        AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient
                .execute(oauthTokenRequest);
        //成功获得authToken
        if (null != oauthTokenResponse && oauthTokenResponse.isSuccess()) {
            //4. 利用authToken获取用户信息
            AlipayUserUserinfoShareRequest userinfoShareRequest = new AlipayUserUserinfoShareRequest();
            AlipayUserUserinfoShareResponse userinfoShareResponse = alipayClient.execute(
                    userinfoShareRequest, oauthTokenResponse.getAccessToken());

            //成功获得用户信息
            if (null != userinfoShareResponse && userinfoShareResponse.isSuccess()) {
                //这里仅是简单打印， 请开发者按实际情况自行进行处理
                System.out.println("获取用户信息成功：" + userinfoShareResponse.getBody());
            } else {
                //这里仅是简单打印， 请开发者按实际情况自行进行处理
                System.out.println("获取用户信息失败");
            }
        } else {
            log.info("authCode换取authToken失败");
            return null;
        }
        return null;
    }
}
