package com.jkm.hss.merchant.helper.response;

import lombok.Data;

/**
 * @desc:代付响应参数
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-22 17:52
 */
@Data
public class DfmResponse {
    /**
     * 系统返回码(fail、success、invalid)
     */
    private String error_code;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回码(系统返回码)
     */
    private String ret_Code;
    /**
     * 打款状态码
     */
    private String r1_Code;
    /**
     * 银行打款状态
     */
    private String bank_Status;
    /**
     * 下游订单编号
     */
    private String order_id;
    /**
     * 系统订单编号
     */
    private String sys_order_id;
    /**
     * 签名
     */
    private String sign;
}
