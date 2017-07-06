package com.jkm.base.sms.service.impl;

import com.jkm.base.common.spring.http.client.impl.HttpClientFacade;
import com.jkm.base.sms.service.SmsSendMessageService;
import com.jkm.base.sms.service.constants.NotifierConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/11/16.
 */
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
