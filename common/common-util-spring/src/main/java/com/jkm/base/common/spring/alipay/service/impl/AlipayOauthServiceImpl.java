package com.jkm.base.common.spring.alipay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.request.AlipayUserUserinfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
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
    public String getUserId(String authCode) throws AlipayApiException {
        String userId = "";
        try {
            //3. 利用authCode获得authToken
            AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
            oauthTokenRequest.setCode(authCode);
            oauthTokenRequest.setGrantType(AlipayServiceConstants.GRANT_TYPE);
            AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient
                    .execute(oauthTokenRequest);
            userId = oauthTokenResponse.getUserId();
//            log.info("成功返回信息为{}",JSONObject.toJSON(oauthTokenResponse).toString());
//            //成功获得authToken
//            if (null != oauthTokenResponse && oauthTokenResponse.isSuccess()) {
//                log.info("利用authToken获取用户信息");
//                AlipayUserUserinfoShareRequest userinfoShareRequest = new AlipayUserUserinfoShareRequest();
//                AlipayUserUserinfoShareResponse userinfoShareResponse = alipayClient.execute(
//                        userinfoShareRequest, oauthTokenResponse.getAccessToken());
//                log.info("利用authToken获取用户信息{}",JSONObject.toJSON(userinfoShareResponse).toString());
//                //成功获得用户信息
//                if (null != userinfoShareResponse && userinfoShareResponse.isSuccess()) {
//                    //这里仅是简单打印， 请开发者按实际情况自行进行处理
//                    System.out.println("获取用户信息成功：" + userinfoShareResponse.getBody());
//
//                } else {
//                    //这里仅是简单打印， 请开发者按实际情况自行进行处理
//                    System.out.println("获取用户信息失败");
//
//                }
//            } else {
//                //这里仅是简单打印， 请开发者按实际情况自行进行处理
//                System.out.println("authCode换取authToken失败");
//            }
        } catch (AlipayApiException alipayApiException) {
            //自行处理异常
            alipayApiException.printStackTrace();
        }
        return userId;
    }
}
