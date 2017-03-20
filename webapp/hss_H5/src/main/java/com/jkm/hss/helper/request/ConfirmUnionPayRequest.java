package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/3/13.
 *
 * 快捷  确认下单
 */
@Data
public class ConfirmUnionPayRequest {

    /**
     * 交易订单id
     */
    private long orderId;

    /**
     * 验证码
     */
    private String code;
}
