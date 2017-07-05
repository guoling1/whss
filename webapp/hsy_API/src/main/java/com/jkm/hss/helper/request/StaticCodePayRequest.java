package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/24.
 */
@Data
public class StaticCodePayRequest {

    /**
     * 好收银订单id
     */
    private long hsyOrderId;

    /**
     * 总金额
     */
    private String totalFee;
}
