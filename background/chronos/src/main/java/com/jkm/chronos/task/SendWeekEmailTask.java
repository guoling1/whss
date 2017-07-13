package com.jkm.chronos.task;

import com.jkm.hss.bill.service.HsyBalanceAccountEmailService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by yulong.zhang on 2017/7/12.
 */
@Slf4j
@Component
public class SendWeekEmailTask extends AbstractTask {

    @Autowired
    @Qualifier("hsyBalanceAccountEmailService")
    private HsyBalanceAccountEmailService hsyBalanceAccountEmailService;


    public SendWeekEmailTask() {
        setName("发送周账单任务");
    }


    @Override
    protected void run() {
        log.info("333");
//        this.hsyBalanceAccountEmailService.sendWeekBalanceAccountEmail();
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
