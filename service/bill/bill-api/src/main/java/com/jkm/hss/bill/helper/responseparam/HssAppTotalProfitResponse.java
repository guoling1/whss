package com.jkm.hss.bill.helper.responseparam;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xingliujie on 2017/8/13.
 */
@Data
public class HssAppTotalProfitResponse {
    private Long count;
    private BigDecimal todayAmount;
    private BigDecimal yesterdayAmount;
    private BigDecimal totalAmount;
}
