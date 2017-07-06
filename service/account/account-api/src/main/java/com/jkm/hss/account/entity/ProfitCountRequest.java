package com.jkm.hss.account.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhangbin on 2017/5/22.
 */
@Data
public class ProfitCountRequest {

    /**
     * 分账日期
     */
    private Date splitDate;

    /**
     * 分账日期
     */
    private Date splitDate1;

    /**
     * yu
     * 业务类型
     * {@link com.jkm.hss.account.enums.EnumSplitBusinessType}
     */
    private String businessType;

    /**
     * 查询条件：开始时间
     */
    private String startTime;

    /**
     * 查询条件：结束时间
     */
    private String endTime;

    /**
     * 分公司id
     */
    private Long oemId;

    /**
     * 是否是分公司
     */
    private int oemType;

    /**
     * 所属一级代理商id
     */
    private long firstLevelDealerId;

    /**
     * 代理商账户id
     */
    private long accountId;

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
