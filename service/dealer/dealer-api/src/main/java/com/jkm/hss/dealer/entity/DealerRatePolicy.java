package com.jkm.hss.dealer.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.dealer.enums.EnumPolicyType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xingliujie on 2017/6/8.
 */
@Data
public class DealerRatePolicy extends BaseEntity{
    /**
     * 代理商id
     */
    private long dealerId;
    /**
     * 费率类型
     *{@link EnumPolicyType}
     */
    private String policyType;
    /**
     *T1代理商结算价
     */
    private BigDecimal dealerTradeRateT1;
    /**
     * T1最小商户费率
     */
    private BigDecimal merchantMinRateT1;
    /**
     * T1最大商户费率
     */
    private BigDecimal merchantMaxRateT1;
    /**
     * D1代理商结算价
     */
    private BigDecimal dealerTradeRateD1;
    /**
     * D1最小商户费率
     */
    private BigDecimal merchantMinRateD1;
    /**
     * D1最大商户费率
     */
    private BigDecimal merchantMaxRateD1;
    /**
     * D0代理商结算价
     */
    private BigDecimal dealerTradeRateD0;
    /**
     * D0最小商户费率
     */
    private BigDecimal merchantMinRateD0;
    /**
     * D0最大商户费率
     */
    private BigDecimal merchantMaxRateD0;
}
