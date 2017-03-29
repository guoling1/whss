package com.jkm.hss.dealer.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/3/24.
 */
@Data
public class QueryMerchantRequest {

    /**
     * 商户编号
     */
    private String markCode;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 所属一级
     */
    private String proxyName;

    /**
     * 所属二
     * 级
     */
    private String proxyName1;

    /**
     * 注册手机号
     */
    private String mobile;

    /**
     * 查询条件：注册开始时间
     */
    private String startTime;

    /**
     * 查询条件：注册结束时间
     */
    private String endTime;

    /**
     * 查询条件：认证开始时间
     */
    private String startTime1;

    /**
     * 查询条件：认证结束时间
     */
    private String endTime1;

    /**
     * 认证时间
     */
    private String authenticationTime;

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
