package com.jkm.hss.helper.request;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/7/5.
 */
@Data
public class PcOrderListRequest extends PageQueryParams {

    /**
     * 店铺id
     */
    private long shopId;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 支付方式
     *
     * {@link com.jkm.hss.product.enums.EnumPaymentChannel}
     */
    private List<Integer> paymentChannels = Arrays.asList(new Integer[] {1, 2, 3});
    /**
     * 是否查询全部店铺
     *
     * 0否，1是
     */
    private int all;
}
