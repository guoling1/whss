package com.jkm.hss.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.jkm.api.service.PayCallbackService;
import com.jkm.api.service.SettleCallbackService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.WithdrawService;
import com.jkm.hss.bill.service.impl.BaseTradeService;
import com.jkm.hss.mq.config.MqConfig;
import com.jkm.hss.mq.producer.MqProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
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
    @Autowired
    private BaseTradeService baseTradeService;
    @Autowired
    private PayCallbackService payCallbackService;
    @Autowired
    private SettleCallbackService settleCallbackService;
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
                log.info("消费消息--结算单[{}]， 向网关发送提现请求", body.getLongValue("settlementRecordId"));
                final long merchantId = body.getLongValue("merchantId");
                final long settlementRecordId = body.getLongValue("settlementRecordId");
                final String payOrderSn = body.getString("payOrderSn");
                final int payChannelSign = body.getIntValue("payChannelSign");
                this.withdrawService.merchantWithdrawBySettlementRecord(merchantId, settlementRecordId, payOrderSn, payChannelSign);
            } else if (MqConfig.MERCHANT_WITHDRAW_D0.equals(message.getTag())) {
                log.info("消费消息--结算单[{}]， D0-向网关发送提现请求", body.getLongValue("settlementRecordId"));
                final long settlementRecordId = body.getLongValue("settlementRecordId");
                final String payOrderSn = body.getString("payOrderSn");
                final int payChannelSign = body.getIntValue("payChannelSign");
                this.baseTradeService.withdrawBySettlement(settlementRecordId, payOrderSn, payChannelSign);
            } else if (MqConfig.MERCHANT_WITHDRAW_T1.equals(message.getTag())) {
                log.info("消费消息--订单[{}], T1-发起提现请求", body.getLongValue("orderId"));
                final long orderId = body.getLongValue("orderId");
                this.orderService.t1WithdrawByOrderId(orderId);
            } else if (MqConfig.API_PAY_CALLBACK.equals(message.getTag())) {
                final long orderId = body.getLongValue("orderId");
                final long businessOrderId = body.getLongValue("businessOrderId");
                final int count = body.getIntValue("count");
                final String msg = body.getString("msg");
                final int isSuccess = body.getIntValue("isSuccess");
                log.info("消费消息--交易订单[{}], api支付结果第[{}]次通知商户", orderId, count);
                final Pair<Integer, String> resultPair = this.payCallbackService.payCallback(orderId, businessOrderId, isSuccess, msg);
                if (0 != resultPair.getLeft()) {
                    this.retryNotifyPayCallbackMsg(orderId, businessOrderId, isSuccess, msg, count);
                }
            } else if (MqConfig.API_SETTLE_CALLBACK.equals(message.getTag())) {
                final long orderId = body.getLongValue("orderId");
                final long businessOrderId = body.getLongValue("businessOrderId");
                final int count = body.getIntValue("count");
                log.info("消费消息--交易订单[{}], api结算成功结果第[{}]次通知商户", orderId, count);
                final Pair<Integer, String> resultPair = this.settleCallbackService.settleCallback(orderId, businessOrderId);
                if (0 != resultPair.getLeft()) {
                    this.retryNotifySettleCallbackMsg(orderId, businessOrderId, count);
                }
            }
        } catch (final Throwable e) {
            log.error("consume message error, Topic is: [{}], tag is: [{}] MsgId is: [{}] key is : [{}]", message.getTopic(),
                    message.getTag(), message.getMsgID(), message.getKey());
            log.error("消费消息--提现， 向网关发送提现请求异常", e);
        }
        //如果想测试消息重投的功能,可以将Action.CommitMessage 替换成Action.ReconsumeLater
        return Action.CommitMessage;
    }


    /**
     * 重试通知
     *
     * @param orderId
     * @param businessOrderId
     * @param isSuccess
     * @param msg
     * @param count
     */
    private void retryNotifyPayCallbackMsg(final long orderId, final long businessOrderId,
                                           final int isSuccess, final String msg, final int count) {
        final JSONObject requestJsonObject = new JSONObject();
        requestJsonObject.put("orderId", orderId);
        requestJsonObject.put("businessOrderId", businessOrderId);
        requestJsonObject.put("isSuccess", isSuccess);
        requestJsonObject.put("msg", msg);
        requestJsonObject.put("count", count + 1);
        if (count == 0) {
            log.info("消费消息--交易订单[{}], 支付重试通知商户", orderId);
            MqProducer.produce(requestJsonObject, MqConfig.API_PAY_CALLBACK, 3 * 1000);
        }
        if (count == 1) {
            log.info("消费消息--交易订单[{}], 支付重试通知商户", orderId);
            MqProducer.produce(requestJsonObject, MqConfig.API_PAY_CALLBACK, 5 * 1000);
        }
        if (count == 2) {
            log.info("消费消息--交易订单[{}], 支付重试通知商户", orderId);
            MqProducer.produce(requestJsonObject, MqConfig.API_PAY_CALLBACK, 5 * 60 * 1000);
        }
        if (count == 4) {
            log.info("消费消息--交易订单[{}], 支付重试通知商户", orderId);
            MqProducer.produce(requestJsonObject, MqConfig.API_PAY_CALLBACK, 30 * 60 * 1000);
        }
    }

    /**
     * 重试通知
     *
     * @param orderId
     * @param businessOrderId
     * @param count
     */
    private void retryNotifySettleCallbackMsg(final long orderId, final long businessOrderId, final int count) {
        final JSONObject requestJsonObject = new JSONObject();
        requestJsonObject.put("orderId", orderId);
        requestJsonObject.put("businessOrderId", businessOrderId);
        requestJsonObject.put("count", count + 1);
        if (count == 0) {
            log.info("消费消息--交易订单[{}], 结算成功重试通知商户", orderId);
            MqProducer.produce(requestJsonObject, MqConfig.API_SETTLE_CALLBACK, 3 * 1000);
        }
        if (count == 1) {
            log.info("消费消息--交易订单[{}], 结算成功重试通知商户", orderId);
            MqProducer.produce(requestJsonObject, MqConfig.API_SETTLE_CALLBACK, 5 * 1000);
        }
        if (count == 2) {
            log.info("消费消息--交易订单[{}], 结算成功重试通知商户", orderId);
            MqProducer.produce(requestJsonObject, MqConfig.API_SETTLE_CALLBACK, 5 * 60 * 1000);
        }
        if (count == 4) {
            log.info("消费消息--交易订单[{}], 结算成功重试通知商户", orderId);
            MqProducer.produce(requestJsonObject, MqConfig.API_SETTLE_CALLBACK, 30 * 60 * 1000);
        }
    }
}
