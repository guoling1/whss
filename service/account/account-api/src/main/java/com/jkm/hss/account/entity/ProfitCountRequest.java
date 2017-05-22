package com.jkm.hss.account.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/5/22.
 */
@Data
public class ProfitCountRequest {

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
