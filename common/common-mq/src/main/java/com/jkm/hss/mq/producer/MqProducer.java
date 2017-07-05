package com.jkm.hss.mq.producer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.jkm.base.common.spring.core.SpringContextHolder;
import com.jkm.hss.mq.config.MqConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息生产
 */
@Slf4j
public class MqProducer {

    /**
     *
     * @param requestData   消费时，用到的参数
     * @param mqType        消息tag类型
     * @param delayTime     毫秒
     */
    public static void produce(final JSONObject requestData, final String mqType, final long delayTime){

        final ProducerBean producer = SpringContextHolder.getBeanByName("producer");
        try {
            final Message message = new Message(MqConfig.TOPIC, mqType, requestData.toString().getBytes("UTF-8"));
            message.setStartDeliverTime(System.currentTimeMillis() + delayTime);
            final SendResult result = producer.send(message);
            log.info("mq消息tag[{}], msgId[{}]生产成功", mqType, result.getMessageId());
        } catch (Throwable e) {
            log.error("mq消息tag[" + mqType + "]生产失败", e);
        }
    }

    /**
     * 调用方法
     */
    public void test() {
        final JSONObject jsonObject = new JSONObject();
        MqProducer.produce(jsonObject, MqConfig.TEST, 1000);
    }
}
