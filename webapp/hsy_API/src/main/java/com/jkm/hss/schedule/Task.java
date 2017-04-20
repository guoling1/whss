package com.jkm.hss.schedule;

import com.jkm.hss.settle.service.AccountSettleAuditRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    @Qualifier("accountSettleAuditRecordService")
    private AccountSettleAuditRecordService accountSettleAuditRecordService;

    /**
     * 处理 T1 结算审核, 生成记录
     *
     * 2点0时0分
     */
//    @Scheduled(cron = "0 0 2 * * ?")
    public void generateHsySettleAuditRecordTask() {
        log.info("生成结算审核记录定时任务--start");
        this.accountSettleAuditRecordService.generateHsySettleAuditRecordTask();
        log.info("生成结算审核记录定时任务--end");
    }

    /**
     * 处理 T1 结算 自动结算
     */
//    @Scheduled(cron = "0 0 14 * * ?")
    public void handleT1WithdrawTask() {
        log.info("结算审核记录自动结算定时任务--start");
//        this.accountSettleAuditRecordService.handleSettleAuditRecordTask();
        log.info("结算审核记录自动结算定时任务--end");
    }
}
