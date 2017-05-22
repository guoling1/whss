package com.jkm.hss.merchant.helper.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xingliujie on 2017/2/27.
 */
@Data
public class MerchantUpgradeRequest {
    /**
     * 商户id
     */
    private long merchantId;

    /**
     * 产品id
     */
    private long productId;

    /**
     * 支付方式
     */
    private Integer channelTypeSign;

    /**
     * 升级后费率
     */
    private BigDecimal rate;

}
