package com.jkm.hss.merchant.helper.response;

import lombok.Data;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-24 11:13
 */
@Data
public class OtherPayQueryResult {
    /**
     * 系统返回码
     */
    private String retCode;
    /**
     * 打款状态码
     * 00成功
     * 其余 fail失败 exception 异常
     */
    private String r1Code;
    /**
     * 银行打款状态
     * S 已成功
     * I 银行处理中
     * F 出款失败， 原因见打款明细查询fail_Desc
     * W 未出款
     * U 未知
     */
    private String bankStatus;
    /**
     * 商户订单号
     */
    private String orderId;
    /**
     *三方系统订单号
     */
    private String outTradeNo;

}
