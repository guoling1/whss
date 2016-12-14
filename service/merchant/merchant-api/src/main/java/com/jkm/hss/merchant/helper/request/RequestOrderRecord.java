package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-25 20:14
 */
@Data
public class RequestOrderRecord {
    private int page;
    private int size;
    private int offset;
    private long merchantId;
    private String orderId;
    private String startDate;
    private String endDate;
}
