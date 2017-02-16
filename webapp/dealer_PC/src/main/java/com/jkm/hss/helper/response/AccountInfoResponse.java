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
     * 待结算金额
     */
    private BigDecimal dueSettleAmount;
    /**
     * 可用余额
     */
    private BigDecimal available;

    private String bankName;

    private String bankNo;

    private int fee;

    private String mobile;
}
