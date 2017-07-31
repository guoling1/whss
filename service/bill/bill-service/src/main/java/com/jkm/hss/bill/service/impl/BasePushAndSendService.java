package com.jkm.hss.bill.service.impl;

import java.util.Date;

/**
 * Created by yulong.zhang on 2017/7/31.
 */
public interface BasePushAndSendService {

    /**
     * app推送， 发送打印小票消息
     *
     * @param tradeOrderNo
     * @param successTime
     */
    void pushAndSendPrintMsg(String tradeOrderNo, Date successTime);
}
