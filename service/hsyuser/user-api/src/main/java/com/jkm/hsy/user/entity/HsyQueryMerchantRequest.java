package com.jkm.hsy.user.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/3/29.
 */
@Data
public class HsyQueryMerchantRequest {

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
