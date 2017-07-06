package com.jkm.hss.bill.helper.requestparam;
import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

import java.util.List;

/**
 * Created by wayne on 17/5/16.
 */
@Data
public class TradeListRequestParam extends PageQueryParams {
    private long shopId;
    private String startTime;
    private String endTime;
    private String channel;
    private List<Integer> paymentChannels;
    // /1当日  2按传参时间统计
    private int stType;
    /**
     * 是否查询全部店铺
     *
     * 0否，1是
     */
    private int all;
}
