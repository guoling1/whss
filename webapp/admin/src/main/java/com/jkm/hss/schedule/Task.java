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
     * 更新结算审核记录状态
     */
    @Scheduled(cron = "0 30 15 * * ?")
    public void handleUpdateSettlementTask() {
        log.info("更新结算审核记录状态定时任务-start");
        this.accountSettleAuditRecordService.handleSettleAuditRecordTask();
        log.info("更新结算审核记录状态定时任务-end");
    }
}
