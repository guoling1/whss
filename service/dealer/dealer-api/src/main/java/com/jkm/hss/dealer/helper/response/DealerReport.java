package com.jkm.hss.dealer.helper.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by wayne on 17/4/27.
 */
@Data
public class DealerReport {
    /**
     * 昨日商户交易总额
     */
    private BigDecimal yDayMertradeAmount;
    /**
     * 昨日商户交易总额-直属
     */
    private BigDecimal yDayMertradeAmountDir;
    /**
     * 昨日商户交易总额-下级
     */
    private BigDecimal yDayMertradeAmountSub;

    /**
     * 昨日商户注册数-直属
     */
    private Integer yDayregMerNumberDir;
    /**
     * 昨日商户注册数-下级代理
     */
    private Integer yDayregMerNumberSub;

    /**
     * 商户注册总数-直属
     */
    private Integer regMerNumberDir;
    /**
     * 商户注册总数-下级代理
     */
    private Integer regMerNumberSub;

    /**
     * 昨日商户审核数-直属
     */
    private Integer yDaycheckMerNumberDir;
    /**
     * 昨日商户审核数-下级代理
     */
    private Integer yDaycheckMerNumberSub;

    /**
     * 商户审核总数-直属
     */
    private Integer checkMerNumberDir;
    /**
     * 商户审核总数-下级代理
     */
    private Integer checkMerNumberSub;

    /**
     * 二维码总数
     */
    private Integer qrCodeNumber;


}
