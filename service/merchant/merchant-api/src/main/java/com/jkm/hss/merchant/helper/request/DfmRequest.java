package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * @desc:代付请求参数
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-22 17:52
 */
@Data
public class DfmRequest {
    /**
     * 代理商id
     */
    private long dealerId;
    /**
     * 账户id
     */
    private long accountId;
    /**
     * 手机号N
     */
    private String phoneNo;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 收款人姓名
     */
    private String accountName;
    /**
     * 收款卡号
     */
    private String accountNumber;
    /**
     * 备注N
     */
    private String note;
    /**
     * 身份证号
     */
    private String idCard;

}
