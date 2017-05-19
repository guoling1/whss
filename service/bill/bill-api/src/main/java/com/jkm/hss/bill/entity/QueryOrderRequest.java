package com.jkm.hss.bill.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/5/18.
 */
@Data
public class QueryOrderRequest {

    /**
     * 页数
     */
    private Integer page;
    /**
     * 每页条数
     */
    private Integer size;
    /**
     * 条数
     */
    private Integer offset;
}
