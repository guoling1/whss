package com.jkm.hss.notifier.service;


import com.jkm.hss.notifier.helper.SendMessageParams;

/**
 * Created by huangwei on 9/10/15.
 * 发送短信
 */
public interface SendMessageService {
    /**
     * 发送短信消息
     *
     * @param params
     * @return
     */
    long sendMessage(final SendMessageParams params);

}
