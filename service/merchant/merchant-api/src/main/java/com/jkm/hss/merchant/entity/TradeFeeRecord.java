package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2016/11/22.
 */
@Data
public class TradeFeeRecord extends BaseEntity {

    /**
     *商户号
     */
    private long merchantId;

    /**
     *订单编号
     */
    private long orderId;

    /**
     *0.支付 1.提现
     */
    private int tradeType;

    /**
     *金额(元)
     */
    private BigDecimal totalFee;



}
