package com.jkm.hss.admin.helper.requestparam;

import lombok.Data;


@Data
public class ChangeBankCardRequest{
    /**
     * 账户编码
     */
    private long merchantId;
    /**
     * 银行卡号
     */
    private String bankNo;
    /**
     * 预留手机号
     */
    private String reserveMobile;
}
