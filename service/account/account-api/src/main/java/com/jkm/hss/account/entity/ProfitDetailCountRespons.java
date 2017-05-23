package com.jkm.hss.account.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhangbin on 2017/5/22.
 */
@Data
public class ProfitDetailCountRespons {

    /**
     * 分账流水号（唯一）
     */
    private String splitSn;

    /**
     * yu
     * 业务类型
     * {@link com.jkm.hss.account.enums.EnumSplitBusinessType}
     */
    private String businessType;

    /**
     * 分账日期
     */
    private Date splitDate;

    /**
     * 分账日期
     */
    private String splitDates;

    /**
     * 分账单号（为了支持多次分账， 现在可以和交易单号保持一致）
     */
    private String splitOrderNo;

    /**
     * 结算周期
     */
    private String settleType;

    /**
     * 收款用户名
     */
    private String receiptMoneyUserName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 代理名称
     */
    private String proxyName;

    /**
     * 代理编号
     */
    private String markCode;



    /**
     * 分账所得金额
     */
    private BigDecimal splitAmount;



}
