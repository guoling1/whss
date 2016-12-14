package com.jkm.chronos.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by yulong.zhang on 2016/10/12.
 */
@Slf4j
@Component
public class TestTask extends AbstractTask {

    public TestTask() {
        setName("定时还款任务");
    }

    @Override
    protected void run() {
        //具体业务逻辑
    }
}
