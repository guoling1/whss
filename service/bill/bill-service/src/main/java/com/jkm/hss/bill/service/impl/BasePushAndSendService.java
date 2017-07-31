package com.jkm.hss.bill.service.impl;

import java.util.Date;

/**
 * Created by yulong.zhang on 2017/7/31.
 */
public interface BasePushAndSendService {

    /**
     * app推送， 发送打印小票消息
     *
     * @param orderNumber 订单号
     * @param orderNo 交易订单号
     * @param successTime
     */
    void pushAndSendPrintMsg(String orderNumber, String orderNo, Date successTime);
}
