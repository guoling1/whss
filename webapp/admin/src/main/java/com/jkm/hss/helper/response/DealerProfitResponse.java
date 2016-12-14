package com.jkm.hss.helper.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-12-08.
 */
@Data
public class DealerProfitResponse {

    //代理分润，分润日期，代理商名称，代理商编号，收单交易分润金额，提现分润金额
    /**
     *代理分润当日总额
     */
    private BigDecimal dealerProfit;

    /**
     * 所属上级
     */
    private String dealerName;

    /**
     * 分润日期
     */
    private BigDecimal profitDate;

    /**
     * 代理商名称
     */
    private BigDecimal profitName;

    /**
     * 代理商编号
     */
    private long dealerId;

    /**
     * 收单分润
     */
    private BigDecimal collectMoney;

    /**
     * 提现分润
     */
    private BigDecimal withdrawMoney;
}
