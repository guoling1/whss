package com.jkm.hsy.user.help.requestparam;

import com.jkm.hsy.user.Enum.EnumIsOpen;
import com.jkm.hsy.user.Enum.EnumPolicyType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xingliujie on 2017/6/9.
 */
@Data
public class UserTradeRateListResponse {
    /**
     * 支付方式
     */
    private String rateName;
    /**
     * 政策类型
     * {@link EnumPolicyType}
     */
    private String policyType;
    /**
     * T1支付费率
     */
    private BigDecimal tradeRateT1;
    /**
     * D1支付费率
     */
    private BigDecimal tradeRateD1;
    /**
     * D0支付费率
     */
    private BigDecimal tradeRateD0;
}
