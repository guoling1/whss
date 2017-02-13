package com.jkm.hss.schedule;

import com.jkm.hss.settle.service.AccountSettleAuditRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by yulong.zhang on 2017/1/12.
 */

@Slf4j
@Component
@Lazy(false)
public class Task {

//    @Autowired
//    private AccountSettleAuditRecordService accountSettleAuditRecordService;

    /**
     * 处理 T1 结算审核, 生成记录
     *
     * 周一至周五 2点0时0分
     */
//    @Scheduled(cron = "0 0 1 ? * MON-FRI")
//    public void handleT1SettleTask() {
//        log.info("结算审核定时任务--start");
//        this.accountSettleAuditRecordService.handleT1SettleTask();
//        log.info("结算审核定时任务--end");
//    }
}
