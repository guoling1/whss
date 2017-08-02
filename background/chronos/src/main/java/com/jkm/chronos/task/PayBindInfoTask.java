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
public class PayBindInfoTask extends AbstractTask {

    public PayBindInfoTask() {
        setName("民生信息同步");
    }

    @Autowired
    private HttpClientFacade httpClientFacade;

    @Override
    protected void run() {
        this.httpClientFacade.get("http://pay.qianbaojiajia.com/chronos/msBindInfo");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleJobExecutionException(final JobExecutionException jobExecutionException) throws JobExecutionException {
        super.handleJobExecutionException(jobExecutionException);
        log.error("民生信息同步,任务执行失败:" + jobExecutionException.getMessage());
    }
}
