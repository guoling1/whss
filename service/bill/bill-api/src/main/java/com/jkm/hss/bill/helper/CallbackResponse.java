package com.jkm.hss.bill.helper;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2017/6/12.
 *
 * 交易返回到业务的回调参数
 */
@Data
public class CallbackResponse {

    /**
     * 业务订单号
     */
    private String businessOrderNo;
    /**
     * 交易订单号
     */
    private String tradeOrderNo;
    /**
     * 支付流水号
     */
    private String sn;
    /**
     * 交易成功时间
     */
    private Date successTime;
    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;
    /**
     * 手续费
     */
    private BigDecimal poundage;
    /**
     * {@link com.jkm.hss.bill.enums.EnumBasicStatus}
     */
    private int code;
    /**
     * 返回信息
     */
    private String message;
}
