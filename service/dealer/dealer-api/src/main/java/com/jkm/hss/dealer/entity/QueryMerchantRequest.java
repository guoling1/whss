package com.jkm.hss.dealer.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/3/24.
 */
@Data
public class QueryMerchantRequest {

    /**
     * 一级代理商id
     */
    private long firstLevelDealerId;

    /**
     * 代理商级别
     */
    private int level;

    /**
     * 代理商id
     */
    private long dealerId;

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
