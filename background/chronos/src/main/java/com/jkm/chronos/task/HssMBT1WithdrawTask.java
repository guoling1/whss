package com.jkm.chronos.task;

import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Created by yulong.zhang on 2017/7/18.
 */
@Slf4j
@Component
public class HssMBT1WithdrawTask extends AbstractTask {

    public HssMBT1WithdrawTask() {
        setName("魔宝T1提现任务");
    }

    @Autowired
    private OrderService orderService;

    @Override
    protected void run() {
//        final ArrayList<Integer> channelList = new ArrayList<>();
//        channelList.add(EnumPayChannelSign.MB_UNIONPAY.getId());
//        this.orderService.handleT1UnSettlePayOrder(channelList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleJobExecutionException(final JobExecutionException jobExecutionException) throws JobExecutionException {
        super.handleJobExecutionException(jobExecutionException);
        log.error("魔宝T1提现任务执行失败:" + jobExecutionException.getMessage());
    }
}
