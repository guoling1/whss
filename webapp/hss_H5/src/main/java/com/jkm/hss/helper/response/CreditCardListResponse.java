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
     * 银行便码
     */
    private String bankCode;
    /**
     * 尾号
     */
    private String shortNo;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 状态 0：禁用，1：启用
     */
    private int status;
    /**
     * 是否默认 0：非，1：是
     */
    private int isDefault;
}
