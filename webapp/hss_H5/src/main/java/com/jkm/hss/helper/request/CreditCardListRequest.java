package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/3/22.
 */
@Data
public class CreditCardListRequest {

    /**
     *  信用卡ID
     */
    private long creditCardId;

    /**
     * 通道
     */
    private int channel;
}
