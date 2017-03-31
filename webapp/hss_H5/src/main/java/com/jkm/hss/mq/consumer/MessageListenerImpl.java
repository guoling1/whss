package com.jkm.hss.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.WithdrawService;
import com.jkm.hss.mq.config.MqConfig;
import com.jkm.hss.settle.service.AccountSettleAuditRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yulong.zhang on 2016/11/18.
 */
@Slf4j
public class MessageListenerImpl implements MessageListener {

    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private OrderService orderService;
    /**
     * 消费消息
     *
     * @param message
     * @param consumeContext
     * @return
     */
    @Override
    public Action consume(final Message message, final ConsumeContext consumeContext) {
        log.info("Receive message, Topic is: [{}], tag is: [{}] MsgId is: [{}]", message.getTopic(),
                message.getTag(), message.getMsgID());
        try {
            final JSONObject body = JSONObject.parseObject(new String(message.getBody(),"UTF-8"));
            if (MqConfig.MERCHANT_WITHDRAW.equals(message.getTag())) {
                log.info("消费消息--结算单[{}]， D0-向网关发送提现请求", body.getLongValue("settlementRecordId"));
                final long merchantId = body.getLongValue("merchantId");
                final long settlementRecordId = body.getLongValue("settlementRecordId");
                final String payOrderSn = body.getString("payOrderSn");
                final int payChannelSign = body.getIntValue("payChannelSign");
                log.info("发起提现+++++++++++++++++++++++++++");
//                this.withdrawService.merchantWithdrawBySettlementRecord(merchantId, settlementRecordId, payOrderSn, payChannelSign);
            } else if (MqConfig.MERCHANT_WITHDRAW_T1.equals(message.getTag())) {
                log.info("消费消息--订单[{}], T1-发起提现请求", body.getLongValue("orderId"));
                final long orderId = body.getLongValue("orderId");
                this.orderService.t1WithdrawByOrderId(orderId);
            }
        } catch (final Throwable e) {
            log.error("consume message error, Topic is: [{}], tag is: [{}] MsgId is: [{}]", message.getTopic(),
                    message.getTag(), message.getMsgID());
            log.error("消费消息--提现， 向网关发送提现请求异常", e);
        }
        //如果想测试消息重投的功能,可以将Action.CommitMessage 替换成Action.ReconsumeLater
        return Action.CommitMessage;
    }
}
