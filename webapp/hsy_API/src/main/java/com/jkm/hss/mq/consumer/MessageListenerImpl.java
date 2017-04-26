package com.jkm.hss.mq.consumer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.jkm.hss.settle.service.AccountSettleAuditRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by yulong.zhang on 2016/11/18.
 */
@Slf4j
public class MessageListenerImpl implements MessageListener {

    @Autowired
    @Qualifier("accountSettleAuditRecordService")
    private AccountSettleAuditRecordService accountSettleAuditRecordService;
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

        } catch (Throwable e) {
            log.info("consume message error, Topic is: [{}], tag is: [{}] MsgId is: [{}] key is [{}]", message.getTopic(),
                    message.getTag(), message.getMsgID(), message.getKey());
        }
        //如果想测试消息重投的功能,可以将Action.CommitMessage 替换成Action.ReconsumeLater
        return Action.CommitMessage;
    }
}
