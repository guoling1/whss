package com.jkm.hss.dealer.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/5/3.
 */
@Data
public class BranchAccountRequest {

    /**
     * 分公司名称
     */
    private String proxyName;

    /**
     * 分公司编码
     */
    private String markCode;

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
