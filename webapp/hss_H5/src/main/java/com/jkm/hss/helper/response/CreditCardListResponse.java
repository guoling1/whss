package com.jkm.hss.helper.response;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/3/14.
 */
@Data
public class CreditCardListResponse {

    /**
     * 卡ID
     */
    private long creditCardId;
    /**
     * 银行名字
     */
    private String bankName;
    /**
     * 尾号
     */
    private String shortNo;
    /**
     * 手机号
     */
    private String mobile;
}
