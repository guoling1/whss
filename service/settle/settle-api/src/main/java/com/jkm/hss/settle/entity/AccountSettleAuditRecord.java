package com.jkm.hss.settle.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2017/1/12.
 *
 * 结算审核记录
 *
 * tb_account_settle_audit_record
 */
@Data
public class AccountSettleAuditRecord extends BaseEntity {

    /**
     * 商户编号
     */
    private String merchantNo;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 上级代理编号
     */
    private String dealerNo;

    /**
     * 上级代理名称
     */
    private String dealerName;

    /**
     * 业务线（结算产品）
     *
     * {@link com.jkm.hss.bill.enums.EnumAppType}
     */
    private String appId;

    /**
     * 交易日期
     */
    private Date tradeDate;

    /**
     * 交易笔数
     */
    private int tradeNumber;

    /**
     * 结算总金额
     */
    private BigDecimal settleAmount;

    /**
     * 对账状态
     *
     * {@link com.jkm.hss.settle.enums.EnumAccountCheckStatus}
     */
    private int accountCheckStatus;

    /**
     * 结算状态
     *
     * {@link com.jkm.hss.settle.enums.EnumSettleStatus}
     */
    private int settleStatus;
}
