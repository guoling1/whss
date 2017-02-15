package com.jkm.hss.bill.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 公司分润
 */


@Data
public class JkmProfitResponse {

    /**
     * 账户id
     */
    private long id;

    /**
     * 账户状态
     */
    private int status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 用户名字
     */
    private String userName;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 可用余额
     */
    private BigDecimal available;

    /**
     * 冻结金额
     */
    private BigDecimal frozenAmount;

    /**
     * 待结算金额
     */
    private BigDecimal dueSettleAmount;
}
