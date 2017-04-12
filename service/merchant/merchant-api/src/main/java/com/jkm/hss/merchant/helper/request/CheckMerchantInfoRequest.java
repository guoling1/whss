package com.jkm.hss.merchant.helper.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xingliujie on 2017/2/28.
 */
@Data
public class CheckMerchantInfoRequest {
    /**
     * 通道标示
     */
    private int channelTypeSign;

    /**
     * 收款金额
     */
    private BigDecimal amount;
}
