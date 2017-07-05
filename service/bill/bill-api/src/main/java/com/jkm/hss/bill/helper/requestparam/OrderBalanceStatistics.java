package com.jkm.hss.bill.helper.requestparam;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2017/6/30.
 *
 * 订单结算统计
 */
@Data
public class OrderBalanceStatistics {

    /**
     * 账户id
     */
    private long accountId;

    /**
     * 交易笔数
     */
    private int count;

    /**
     * 交易总额
     */
    private BigDecimal amount;

    /**
     * 手续费总额
     */
    private BigDecimal poundage;

    /**
     * 交易开始时间
     */
    private Date tradeStartTime;

    /**
     * 交易结束时间
     */
    private Date tradeEndTime;

    /**
     * 渠道方
     *
     * {@link com.jkm.hss.product.enums.EnumUpperChannel}
     */
    private int upperChannel;
}
