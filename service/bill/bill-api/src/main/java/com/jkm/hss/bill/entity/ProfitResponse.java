package com.jkm.hss.bill.entity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhangbin on 2016/12/22.
 *
 * 交易订单
 *
 */
@Data
public class ProfitResponse {

    /**
     * 分润流水号
     */
    private String splitSn;

    /**
     * 分润金额
     */
    private BigDecimal splitAmount;

    /**
     * 分润方名称
     */
    private String receiptMoneyUserName;

    /**
     * 收款方类型
     */
    private int accountUserType;

    /**
     * 收款方类型
     */
    private String accountUserTypes;

    /**
     * 结算周期
     */
    private String settleType;

    /**
     * 结算时间
     */
    private Date splitDate;

    /**
     * 结算时间
     */
    private String splitDates;

    /**
     * 分润总额
     */
    private BigDecimal splitTotalAmount;

    /**
     * 备注
     */
    private String remark;

}

