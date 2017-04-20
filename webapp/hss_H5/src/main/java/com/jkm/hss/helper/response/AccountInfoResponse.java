package com.jkm.hss.helper.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2017-02-15.
 */
@Data
public class AccountInfoResponse {

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 可用余额
     */
    private BigDecimal available;

    /**
     * 提现手续费
     */
    private BigDecimal withdrawFee;

    /**
     * 银行名
     */
    private String bankName;

    /**
     * 尾号 4位
     */
    private String bankNo;

    /**
     * 手机号
     */
    private String mobile;

    private BigDecimal settleAmount;
}
