package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 *
 */
@Data
public class CompanyPrifitRequest {

    /**
     * 账户id
     */
    private long accId;

    /**
     * 收款账户id
     */
    private long receiptMoneyAccountId;

    /**
     * 页数
     */
    private Integer pageNo;
    /**
     * 每页条数
     */
    private Integer pageSize;
    /**
     * 条数
     */
    private Integer offset;
}
