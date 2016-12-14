package com.jkm.hss.merchant.helper.response;

import lombok.Data;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-12-08 17:19
 */
@Data
public class DfQueryResponse {
    private int code;

    private String message;

    private OtherPayQueryResult data;
}
