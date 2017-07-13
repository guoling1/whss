package com.jkm.hss.helper.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2016/12/24.
 */
@Data
public class StaticCodePayRequest {

    /**
     * 好收银订单id
     */
    private long hsyOrderId;

    /**
     * 总金额
     */
    private String totalFee;

    /**
     * 折扣金额
     */
    private BigDecimal discountFee;

    /**
     * 是否会员卡支付
     */
    private Integer isMemberCardPay=0;

    /**
     * 消费者ID
     */
    private Long cid;

    /**
     * 会员卡ID
     */
    private Long mcid;

    /**
     * 会员ID
     */
    private Long mid;
}
