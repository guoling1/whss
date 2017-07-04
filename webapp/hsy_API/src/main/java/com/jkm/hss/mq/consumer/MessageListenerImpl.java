package com.jkm.hss.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.jkm.hss.bill.service.HSYTransactionService;
import com.jkm.hss.mq.config.MqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by yulong.zhang on 2016/11/18.
 */
@Slf4j
public class MessageListenerImpl implements MessageListener {
    @Autowired
    private HSYTransactionService hsyTransactionService;
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
            final JSONObject body =  JSONObject.parseObject(new String(message.getBody(),"UTF-8"));
            if (MqConfig.SPLIT_PROFIT.equals(message.getTag())) {
                log.info("消费消息--支付分润[{}]", body.getLong("consumeMsgSplitProfitRecordId"));
                final long consumeMsgSplitProfitRecordId = body.getLong("consumeMsgSplitProfitRecordId");
                this.hsyTransactionService.paySplitProfit(consumeMsgSplitProfitRecordId);
            }
        } catch (final Throwable e) {
            log.error("consume message error, Topic is: [" + message.getTopic() + "], tag is: [" + message.getTag() + "] " +
                            "MsgId is: [" + message.getMsgID() + "] key is [" + message.getKey() + "]", e);
        }
        //如果想测试消息重投的功能,可以将Action.CommitMessage 替换成Action.ReconsumeLater
        return Action.CommitMessage;
    }
}
