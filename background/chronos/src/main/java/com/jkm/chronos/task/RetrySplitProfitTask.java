package com.jkm.chronos.task;

import com.jkm.hss.bill.service.HSYTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by yulong.zhang on 2017/7/12.
 */
@Slf4j
@Component
public class RetrySplitProfitTask extends AbstractTask {

    @Autowired
    private HSYTransactionService hsyTransactionService;

    public RetrySplitProfitTask() {
        setName("重发分润任务");
    }


    @Override
    protected void run() throws InterruptedException {
        this.hsyTransactionService.handleRetrySplitProfitTask();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleJobExecutionException(final JobExecutionException jobExecutionException) throws JobExecutionException {
        super.handleJobExecutionException(jobExecutionException);
        log.error("重发分润任务执行失败:" + jobExecutionException.getMessage());
    }
}
