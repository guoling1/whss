package com.jkm.hsy.user.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/3/29.
 */
@Data
public class HsyQueryMerchantRequest {

    private String globalID;//商户编号

    private String shortName;//商户名称

    /**
     * 一级代理商id
     */
    private long firstLevelDealerId;

    /**
     * 代理商id
     */
    private long dealerId;

    /**
     * 条数
     */
    private Integer offset;
    /**
     * 当前页数
     */
    private int pageNo;
    /**
     * 每页显示页数
     */
    private int pageSize;
}
