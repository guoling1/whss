package com.jkm.hsy.user.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/3/29.
 */
@Data
public class HsyQueryMerchantRequest {

    private String auditTime; //审核时间

    private String auditTime1; //审核时间

    private String globalID;//商户编号

    private String shortName;//商户名称

    /**
     * 查询条件：注册开始时间
     */
    private String startTime;

    /**
     * 查询条件：注册结束时间
     */
    private String endTime;

    private String cellphone;//注册手机号

    private String industry;//行业

    private String industryCode;//行业代码

    private String proxyName;//所属代理商

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
