package com.jkm.hss.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.jkm.hss.mq.config.MqConfig;
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

            final JSONObject body = JSONObject.parseObject(new String(message.getBody(),"UTF-8"));
            if (MqConfig.NORMAL_SETTLE.equals(message.getTag())) {
                log.info("消费消息--结算审核记录[{}]， 结算", body.getLong("recordId"));
                final long recordId = body.getLong("recordId");
                this.accountSettleAuditRecordService.settleImpl(recordId);
            }
        } catch (Throwable e) {
            log.info("consume message error, Topic is: [{}], tag is: [{}] MsgId is: [{}]", message.getTopic(),
                    message.getTag(), message.getMsgID());
            log.error("消费消息异常", e);
        }
        //如果想测试消息重投的功能,可以将Action.CommitMessage 替换成Action.ReconsumeLater
        return Action.CommitMessage;
    }
}
