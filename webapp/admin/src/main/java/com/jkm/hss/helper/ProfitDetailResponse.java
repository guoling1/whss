package com.jkm.hss.helper;


import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-12-15.
 */
@Data
public class ProfitDetailResponse {

    /**
     * 商户交易流水号
     */
    private String paymentSn;

    /**
     * 分润类型(收单, 体现分润)
     */
    private int profitType;

    /**
     * 交易日期
     * 1999-09-09
     */
    private String profitDate;

    /**
     * 结算周期
     */
    private String settleDate;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 上级代理名称
     */
    private String dealerName;

    /**
     * 交易金额
     */
    private BigDecimal totalFee;

    /**
     * 商户手续费
     */
    private BigDecimal merchantFee;

    /**
     * 分润金额
     */
    private BigDecimal shallMoney;


}
