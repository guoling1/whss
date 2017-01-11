package com.jkm.hss.merchant.service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhangbin on 2016/12/2.
 */
public interface SendMsgService {

    /**
     * 支付成功推送
     */
    void sendMessage(String money,String orderNo,String payNo,String store,String merchantName,String touser);

    /**
     * 提现成功推送
     */
    void sendPushMessage(BigDecimal totalAmount, Date withdrawTime, BigDecimal poundage, String bankNo, String toUser);
}
