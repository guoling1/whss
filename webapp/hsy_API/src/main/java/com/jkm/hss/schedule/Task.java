package com.jkm.hss.schedule;

import com.alibaba.fastjson.JSONObject;
import com.jkm.hss.bill.service.HsyBalanceAccountEmailService;
import com.jkm.hss.mq.config.MqConfig;
import com.jkm.hss.mq.producer.MqProducer;
import com.jkm.hss.notifier.entity.ConsumeMsgSplitProfitRecord;
import com.jkm.hss.notifier.service.SendMqMsgService;
import com.jkm.hss.settle.service.AccountSettleAuditRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/1/12.
 */

@Slf4j
@Component
@Lazy(false)
public class Task {

//    @Autowired
//    private SendMqMsgService sendMqMsgService;
//    @Autowired
//    @Qualifier("hsyBalanceAccountEmailService")
//    private HsyBalanceAccountEmailService hsyBalanceAccountEmailService;

    /**
     * 处理重发分润-每一小时一次
     *
     * 间隔5秒一个消息
     */
//    @Scheduled(cron = "0 0 0/1 * * ?")
//    public void handleRetrySendSplitProfitTask() {
//        final List<ConsumeMsgSplitProfitRecord> records = this.sendMqMsgService.getPendingRecordsByTag(MqConfig.SPLIT_PROFIT);
//        log.info("定时任务--处理重发分润，消息个数[{}]", records.size());
//        if (!CollectionUtils.isEmpty(records)) {
//            for (int i = 0; i < records.size(); i++) {
//                final ConsumeMsgSplitProfitRecord record = records.get(i);
//                final JSONObject requestJsonObject = new JSONObject();
//                requestJsonObject.put("consumeMsgSplitProfitRecordId", record.getId());
//                MqProducer.produce(requestJsonObject, MqConfig.SPLIT_PROFIT, i * 5000);
//            }
//        }
//    }

    /**
     * 周一发送上周对账邮件
     */
//    @Scheduled(cron = "0 0 18 ? * MON")
//    public void sendWeekEmail() {
//        log.info("定时任务--处理每周发送邮件");
//        this.hsyBalanceAccountEmailService.sendWeekBalanceAccountEmail();
//    }

    /**
     * 1号发送上月对账邮件
     */
//    @Scheduled(cron = "0 0 18 1 * ?")
//    public void sendMonthEmail() {
//        log.info("定时任务--处理每周发送邮件");
//        this.hsyBalanceAccountEmailService.sendMonthBalanceAccountEmail();
//    }

}
