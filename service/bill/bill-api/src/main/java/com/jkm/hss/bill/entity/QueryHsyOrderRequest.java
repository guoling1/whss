package com.jkm.hss.bill.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/7/24.
 */
@Data
public class QueryHsyOrderRequest {

    /**
     * 条数
     */
    private Integer offset;

    /**
     * 页数
     */
    private Integer page;
    /**
     * 每页条数
     */
    private Integer size;
}
