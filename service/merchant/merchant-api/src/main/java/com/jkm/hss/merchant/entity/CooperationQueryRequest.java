package com.jkm.hss.merchant.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/4/5.
 */
@Data
public class CooperationQueryRequest {

    /**
     * 当前页数
     */
    private int pageNo;
    /**
     * 每页显示页数
     */
    private int pageSize;

    /**
     * 条数
     */
    private Integer offset;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 类型（商户还是代理商）
     */
    private String type;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 查询条件：开始时间
     */
    private String startTime;

    /**
     * 查询条件：结束时间
     */
    private String endTime;
}
