package com.jkm.hss.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.jkm.hss.bill.service.HSYTradeService;
import com.jkm.hss.mq.config.MqConfig;
import com.jkm.hss.notifier.entity.ConsumeMsgFailRecord;
import com.jkm.hss.notifier.enums.EnumConsumeMsgFailRecordStatus;
import com.jkm.hss.notifier.service.SendMqMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


/**
 * Created by yulong.zhang on 2016/11/18.
 */
@Slf4j
public class MessageListenerImpl implements MessageListener {

    @Autowired
    @Qualifier("hsyTradeService")
    private HSYTradeService hsyTradeService;
    @Autowired
    private SendMqMsgService sendMqMsgService;
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
        JSONObject body = null;
        try {
            body = JSONObject.parseObject(new String(message.getBody(),"UTF-8"));
            if (MqConfig.SPLIT_PROFIT.equals(message.getTag())) {
                log.info("消费消息--支付分润[{}]", body.getLong("orderId"));
                final long orderId = body.getLong("orderId");
                this.hsyTradeService.paySplitAccount(orderId);
            }

        } catch (final Throwable e) {
            log.error("consume message error, Topic is: [" + message.getTopic() + "], tag is: [" + message.getTag() + "] " +
                            "MsgId is: [" + message.getMsgID() + "] key is [" + message.getKey() + "]", e);
            if (MqConfig.SPLIT_PROFIT.equals(message.getTag()) && null != body) {
                final ConsumeMsgFailRecord record = new ConsumeMsgFailRecord();
                record.setRequestParam(JSONObject.toJSONString(body));
                record.setTag(MqConfig.SPLIT_PROFIT);
                record.setStatus(EnumConsumeMsgFailRecordStatus.PENDING_SEND.getId());
                this.sendMqMsgService.add(record);
            }
        }
        //如果想测试消息重投的功能,可以将Action.CommitMessage 替换成Action.ReconsumeLater
        return Action.CommitMessage;
    }
}
