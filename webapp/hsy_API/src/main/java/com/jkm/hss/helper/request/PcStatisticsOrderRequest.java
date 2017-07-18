package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/7/5.
 */
@Data
public class PcStatisticsOrderRequest {

    /**
     * 店铺id
     */
    private long shopId;
    /**
     * 开始时间
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;
}
