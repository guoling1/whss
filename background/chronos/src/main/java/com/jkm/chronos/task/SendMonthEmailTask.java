package com.jkm.chronos.task;

import com.jkm.hss.bill.service.HsyBalanceAccountEmailService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by yulong.zhang on 2017/7/12.
 */
@Slf4j
@Component
public class SendMonthEmailTask extends AbstractTask {
    @Autowired
    private HsyBalanceAccountEmailService hsyBalanceAccountEmailService;


    public SendMonthEmailTask() {
        setName("发送月账单任务");
    }


    @Override
    protected void run() throws InterruptedException {
        this.hsyBalanceAccountEmailService.sendMonthBalanceAccountEmail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleJobExecutionException(final JobExecutionException jobExecutionException) throws JobExecutionException {
        super.handleJobExecutionException(jobExecutionException);
        log.error("发送周账单任务执行失败:" + jobExecutionException.getMessage());
    }
}
