package com.jkm.hss.schedule;

import com.jkm.hss.bill.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * Created by yulong.zhang on 2017/3/14.
 */
@Slf4j
@Component
@Lazy(false)
public class Task {

    @Autowired
    private OrderService orderService;

    /**
     * T1 结算到卡定时任务
     *
     * 凌晨6点
     */
    @Scheduled(cron = "0 30 9 * * ?")
    public void handleT1Task() {
        log.info("hss-T1- 结算到卡定时任务--start");
        this.orderService.handleT1UnSettlePayOrder();
        log.info("hss-T1- 结算到卡定时任务定时任务--end");
    }
}
