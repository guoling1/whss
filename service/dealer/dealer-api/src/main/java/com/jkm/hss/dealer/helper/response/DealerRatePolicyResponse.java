package com.jkm.hss.dealer.helper.response;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.dealer.enums.EnumPolicyType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xingliujie on 2017/6/8.
 */
@Data
public class DealerRatePolicyResponse extends BaseEntity{
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
     *固定
     */
    private FixedArr fixedArr;
    /**
     *可变
     */
    private ChangeArr changeArr;

    @Data
    public static  class FixedArr{
        /**
         * T1最小商户费率
         */
        private BigDecimal merchantMinRateT1;
        /**
         * T1最大商户费率
         */
        private BigDecimal merchantMaxRateT1;
        /**
         * D1最小商户费率
         */
        private BigDecimal merchantMinRateD1;
        /**
         * D1最大商户费率
         */
        private BigDecimal merchantMaxRateD1;
        /**
         * D0最小商户费率
         */
        private BigDecimal merchantMinRateD0;
        /**
         * D0最大商户费率
         */
        private BigDecimal merchantMaxRateD0;
    }
    @Data
    public static class ChangeArr{
        /**
         *T1代理商结算价
         */
        private BigDecimal dealerTradeRateT1;
        /**
         * D1代理商结算价
         */
        private BigDecimal dealerTradeRateD1;
        /**
         * D0代理商结算价
         */
        private BigDecimal dealerTradeRateD0;

    }
    /**
     * 排序1.微信 2.支付宝 3.提现
     */
    private Integer sn;
}
