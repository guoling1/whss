package com.jkm.hss.helper.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xingliujie on 2017/8/8.
 */
@Data
public class ToBuyResponse {
    private String payUrl;
    private BigDecimal amount;
}
