package com.jkm.hss.dealer.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2017/5/3.
 */
@Data
public class BranchAccountResponse {

    /**
     * 账户id
     */
    private long id;

    /**
     * 分公司名称
     */
    private String proxyName;

    /**
     * 分公司编码
     */
    private String markCode;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 待结算金额
     */
    private BigDecimal dueSettleAmount;

    /**
     * 可用余额
     */
    private BigDecimal available;
}
