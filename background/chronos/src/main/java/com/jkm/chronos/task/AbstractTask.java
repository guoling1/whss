package com.jkm.chronos.task;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by hutao on 16/3/15.
 * 下午7:32
 */
@Slf4j
public abstract class AbstractTask extends AbstractSimpleElasticJob {
    @Setter
    private String name = "";

    /**
     * {@inheritDoc}
     */
    @Override
    public final void process(final JobExecutionMultipleShardingContext jobExecutionMultipleShardingContext) {
        try {
            log.info("定时任务[{}]开始", this.name);
            run();
            log.info("定时任务[{}]结束", this.name);
        } catch (final Throwable e) {
            log.info("定时任务[" + this.name + "]执行异常", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 定时器任务
     */
    protected abstract void run();
}
