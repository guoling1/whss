package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * Created by zhangbin on 2016/11/28.
 */
@Data
public class RequestMerchantInfo {



    /**
     * 商户编码
     */
    private long merchantId;

    /**
     * 原因描述
     */
    private String descr;

    /**
     * 账户id
     */
    private long accountId;

    /**
     * 状态
     */
    private int status;
}
