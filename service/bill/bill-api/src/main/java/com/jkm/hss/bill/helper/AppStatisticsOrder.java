package com.jkm.hss.bill.helper;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/5/5.
 */
@Data
public class AppStatisticsOrder {

    /**
     * 笔数
     */
    private int number;

    /**
     * 金额
     */
    private BigDecimal amount;
}
