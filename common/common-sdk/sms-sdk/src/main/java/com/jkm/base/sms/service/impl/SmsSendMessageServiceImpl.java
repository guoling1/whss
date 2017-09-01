package com.jkm.base.sms.service.impl;

import com.jkm.base.common.spring.aliyun.util.HttpUtils;
import com.jkm.base.common.spring.http.client.impl.HttpClientFacade;
import com.jkm.base.sms.service.SmsSendMessageService;
import com.jkm.base.sms.service.constants.NotifierConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yulong.zhang on 2016/11/16.
 */
@Slf4j
@Service
public class SmsSendMessageServiceImpl implements SmsSendMessageService {

    @Autowired
    private HttpClientFacade httpClientFacade;
    /**
     * {@inheritDoc}
     *
     * @param mobile    手机号
     * @param content   通知内容
     * @return  第三方短信渠道返回标识
     */
    @Override
    public String sendMessage(String mobile, String content) {
        return httpClientFacade.get(this.generateUrl(mobile, content));
    }

    /**
     * {@inheritDoc}
     *
     * @param mobile
     * @param templateCode
     * @param signName
     * @param templateParam
     * @return
     */
    @Override
    public String sendMessageWithAliyun(final String mobile, final String templateCode, final String signName,
                                        final String templateParam, final String appCode) {

        final Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appCode);
        final Map<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("ParamString", templateParam);
        queryParam.put("RecNum", mobile);
        queryParam.put("SignName", signName);
        queryParam.put("TemplateCode", templateCode);
        try {
            final HttpResponse response = HttpUtils.doGet("http://sms.market.alicloudapi.com", "/singleSendSms", "GET", headers, queryParam);
            log.info("[{}]", EntityUtils.toString(response.getEntity()));
            return EntityUtils.toString(response.getEntity());
        } catch (final Exception e) {
            log.error("手机号[" + mobile + "]发送短信-templateCode[" + templateCode + "]，异常", e);
        }
        return "";
    }

    /**
     * 生成短信发送的url
     *
     * @param mobile
     * @param content
     * @return
     */
    private String generateUrl(final String mobile, final String content) {
        final String sn = NotifierConstants.getNotifierConfig().sn();
        final String pwd = NotifierConstants.getNotifierConfig().password();
        final Map<String, String> params = new HashMap<>();
        params.put("sn", sn);
        params.put("pwd", pwd);
        params.put("mobile", mobile);
        params.put("content", content);
        final StringBuilder urlBuilder = new StringBuilder("http://sdk2.entinfo.cn:8061/mdsmssend.ashx?");
        for (final Map.Entry<String, String> entry : params.entrySet()) {
            urlBuilder.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
        }
        return urlBuilder.substring(0, urlBuilder.length() - 1);
    }
}
