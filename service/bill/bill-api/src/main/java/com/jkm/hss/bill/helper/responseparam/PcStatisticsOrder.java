package com.jkm.hss.bill.helper.responseparam;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/7/5.
 */
@Data
public class PcStatisticsOrder {

    /**
     * 支付方式
     *
     * {@link com.jkm.hss.product.enums.EnumPaymentChannel}
     */
    private int paymentChannel;
    /**
     * 订单状态
     *
     * {@link com.jkm.hss.bill.enums.EnumHsyOrderStatus}
     */
    private int orderStatus;
    /**
     * 笔数
     */
    private int number;
    /**
     * 交易总额
     */
    private BigDecimal totalAmount;
    /**
     * 实付总额
     */
    private BigDecimal realAmount;
    /**
     * 手续费
     */
    private BigDecimal poundage;
}
