package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-12-14 18:22
 */
@Data
public class UnpassRequest {
    /**
     * 订单编码
     */
    private long id;
    /**
     * 错误信息
     */
    private String message;
}
