package com.jkm.hss.bill.helper.responseparam;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by wayne on 17/5/19.
 */
@Data
public class HsyOrderSTResponse {
    private int number;
    private BigDecimal totalAmount;
}
