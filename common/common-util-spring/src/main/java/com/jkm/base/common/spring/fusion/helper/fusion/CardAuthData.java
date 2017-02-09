package com.jkm.base.common.spring.fusion.helper.fusion;

import lombok.Data;

@Data
public class CardAuthData {
    /**
     * 交易流水号
     */
    private String reqSn;
    /**
     * 卡号
     */
    private String crdNo;
    /**
     *银行账户名
     */
    private String accountName;
    /**
     * 证件号
     */
    private String idNo;
    /**
     * 手机号
     */
    private String phoneNo;
}
