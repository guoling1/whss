package com.jkm.hss.dealer.helper.response;

import lombok.Data;

/**
 * Created by wayne on 17/4/27.
 */
@Data
public class DealerReport {
    /**
     * 昨日商户交易总额
     */
    private String yDayMertradeAmount;
    /**
     * 昨日商户交易总额-直属
     */
    private String yDayMertradeAmountDir;
    /**
     * 昨日商户交易总额-下级
     */
    private String yDayMertradeAmountSub;

    /**
     * 昨日商户注册数-直属
     */
    private String yDayregMerNumberDir;
    /**
     * 昨日商户注册数-下级代理
     */
    private String yDayregMerNumberSub;

    /**
     * 商户注册总数-直属
     */
    private String regMerNumberDir;
    /**
     * 商户注册总数-下级代理
     */
    private String regMerNumberSub;

    /**
     * 昨日商户审核数-直属
     */
    private String yDaycheckMerNumberDir;
    /**
     * 昨日商户审核数-下级代理
     */
    private String yDaycheckMerNumberSub;

    /**
     * 商户审核总数-直属
     */
    private String checkMerNumberDir;
    /**
     * 商户审核总数-下级代理
     */
    private String checkMerNumberSub;

    /**
     * 二维码总数
     */
    private String codeNumber;


}
