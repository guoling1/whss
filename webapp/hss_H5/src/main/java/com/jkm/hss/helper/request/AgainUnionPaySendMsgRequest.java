package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/3/13.
 *
 * 快捷再次支付 入参
 */
@Data
public class AgainUnionPaySendMsgRequest {
    /**
     * 金额
     */
    private String amount;
    /**
     * 渠道
     */
    private int channel;
    /**
     * 信用卡id
     */
    private long creditCardId;
    /**
     * 信用卡cvv2
     */
    private String cvv2;
}
