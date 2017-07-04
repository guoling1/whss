package com.jkm.base.sms.service;

/**
 * Created by yulong.zhang on 2016/11/16.
 */
public interface SmsSendMessageService {
    /**
     * 发送短信通知
     *
     * @param mobile    手机号
     * @param content   通知内容
     * @return   第三方短信渠道返回标识
     */
    String sendMessage(String mobile, String content);
}
