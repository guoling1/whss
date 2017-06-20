package com.jkm.hss.schedule;

import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.settle.service.AccountSettleAuditRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

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
        this.accountSettleAuditRecordService.handleSettleAuditRecordTask(null);
        log.info("更新结算审核记录状态定时任务-end");
    }


    /**
     * 更新结算审核记录状态-临时策略
     */
    @Scheduled(cron = "0 30 3 20 6 ?")
    public void handleUpdateSettlementTaskTest() {
        log.info("更新结算审核记录状态定时任务（2017-06-20）-临时策略-start");
        final Date settleDate = DateFormatUtil.parse("2017-06-20" , DateFormatUtil.yyyy_MM_dd);
        this.accountSettleAuditRecordService.handleSettleAuditRecordTask(settleDate);
        log.info("更新结算审核记录状态定时任务（2017-06-20）-临时策略-end");
    }
}
