package com.jkm.chronos.task;

import com.jkm.base.common.spring.http.client.impl.HttpClientFacade;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by yulong.zhang on 2017/7/18.
 */
@Slf4j
@Component
public class PayQueryRefundAcceptRefundOrderTask extends AbstractTask {

    public PayQueryRefundAcceptRefundOrderTask() {
        setName("处理退款已受理的退款流水，查询是否到账任务");
    }

    @Autowired
    private HttpClientFacade httpClientFacade;

    @Override
    protected void run() {
        this.httpClientFacade.get("http://pay.qianbaojiajia.com/chronos/queryRefundAcceptRefundOrder");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleJobExecutionException(final JobExecutionException jobExecutionException) throws JobExecutionException {
        super.handleJobExecutionException(jobExecutionException);
        log.error("处理退款已受理的退款流水，查询是否到账任务执行失败:" + jobExecutionException.getMessage());
    }
}
