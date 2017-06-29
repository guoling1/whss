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
     * hsy退款成功
     *
     * @param orderNo
     * @param refundAmount
     * @param touser
     */
    void refundSendMessage(String shopName, String payType, String orderNo, BigDecimal refundAmount, String touser);

    /**
     * 提现成功推送
     */
    void sendPushMessage(BigDecimal totalAmount, Date withdrawTime, BigDecimal poundage, String bankNo, String toUser);
    /**
     * 充值升级成功推送
     */
    void sendChargeMessage(String money,String typeName,String touser);

    /**
     * 审核通过推送
     */
    void sendAuditThroughMessage(String result, Date TransitTime,String touser);

    /**
     * 审核不通过推送
     */
    void sendAuditNoThroughMessage(String name, String desr, String toUsers);
}
