package com.jkm.hss.schedule;

import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


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
     * 魔宝
     * 凌晨6点
     */
    @Scheduled(cron = "0 0 6 * * ?")
    public void handleMbT1Task() {
        log.info("hss-T1-魔宝快捷- 结算到卡定时任务--start");
        final ArrayList<Integer> channelList = new ArrayList<>();
        channelList.add(EnumPayChannelSign.MB_UNIONPAY.getId());
        this.orderService.handleT1UnSettlePayOrder(channelList);
        log.info("hss-T1-魔宝快捷- 结算到卡定时任务定时任务--end");
    }

    /**
     * T1 结算到卡定时任务
     * 易联
     * 中午12:30
     */
    @Scheduled(cron = "0 35 10 * * ?")
    public void handleEasyLinkT1Task() {
        log.info("hss-T1-易联快捷- 结算到卡定时任务--start");
        final ArrayList<Integer> channelList = new ArrayList<>();
        channelList.add(EnumPayChannelSign.EL_UNIONPAY.getId());
        this.orderService.handleT1UnSettlePayOrder(channelList);
        log.info("hss-T1-易联快捷- 结算到卡定时任务定时任务--end");
    }
}
