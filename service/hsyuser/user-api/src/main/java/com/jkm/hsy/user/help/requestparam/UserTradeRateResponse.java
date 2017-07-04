package com.jkm.hsy.user.help.requestparam;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xingliujie on 2017/6/9.
 */
@Data
public class UserTradeRateResponse {
    /**
     * 微信最小T1费率
     */
    private BigDecimal wxMinT1Trade;
    /**
     * 微信最大T1费率
     */
    private BigDecimal wxMaxT1Trade;
    /**
     * 支付宝最小T1费率
     */
    private BigDecimal zfbMinT1Trade;
    /**
     * 支付宝最大T1费率
     */
    private BigDecimal zfbMaxT1Trade;

}
