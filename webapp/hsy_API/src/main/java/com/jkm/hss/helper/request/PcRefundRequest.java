package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/7/6.
 */
@Data
public class PcRefundRequest {

    /**
     * 交易id
     */
    private long orderId;

    private String password;
}
