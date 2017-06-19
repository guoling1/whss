package com.jkm.hss.product.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.product.enums.EnumPolicyType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2017-06-09.
 */
@Data
public class ProductRatePolicy extends BaseEntity {

    /**
     * 产品id
     */
    private long productId;
    /**
     * 费率类型
     *{@link EnumPolicyType}
     */
    private String policyType;
    /**
     *T1产品结算价
     */
    private BigDecimal productTradeRateT1;
    /**
     * T1最小商户费率
     */
    private BigDecimal merchantMinRateT1;
    /**
     * T1最大商户费率
     */
    private BigDecimal merchantMaxRateT1;
    /**
     * D1产品结算价
     */
    private BigDecimal productTradeRateD1;
    /**
     * D1最小商户费率
     */
    private BigDecimal merchantMinRateD1;
    /**
     * D1最大商户费率
     */
    private BigDecimal merchantMaxRateD1;
    /**
     * D0产品结算价
     */
    private BigDecimal productTradeRateD0;
    /**
     * D0最小商户费率
     */
    private BigDecimal merchantMinRateD0;
    /**
     * D0最大商户费率
     */
    private BigDecimal merchantMaxRateD0;
    /**
     * 排序1.微信 2.支付宝 3.提现
     */
    private Integer sn;
}
