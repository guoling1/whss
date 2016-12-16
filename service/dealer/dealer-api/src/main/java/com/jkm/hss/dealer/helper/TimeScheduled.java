package com.jkm.hss.dealer.helper;

import com.jkm.hss.dealer.service.DailyProfitDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Lazy(false)
public class TimeScheduled {
    @Autowired
    private DailyProfitDetailService dailyProfitDetailService;

    @Scheduled(cron = "0 1 0 * * ?")
    public void otherPay(){
        try{
            this.dailyProfitDetailService.dailyProfitCount();
        }catch (final Throwable throwable){
            log.error("每日分润定时任务运行异常,异常信息:" + throwable.getMessage());
        }
    }
}
