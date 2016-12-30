package com.jkm.hss.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.jkm.hss.bill.service.WithdrawService;
import com.jkm.hss.mq.config.MqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yulong.zhang on 2016/11/18.
 */
@Slf4j
public class MessageListenerImpl implements MessageListener {

    @Autowired
    private WithdrawService withdrawService;
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
                log.info("消费消息--提现单[{}]， 向网关发送提现请求", body.getLong("payOrderId"));
                final Long merchantId = body.getLong("merchantId");
                final Long payOrderId = body.getLong("payOrderId");
                final String payOrderSn = body.getString("payOrderSn");
                final String balanceAccountType = body.getString("balanceAccountType");
                this.withdrawService.merchantWithdrawByOrder(merchantId, payOrderId, payOrderSn, balanceAccountType);
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
