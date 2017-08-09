package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xingliujie on 2017/8/9.
 */
@Data
public class HsyPartnerRules extends BaseEntity{
    private BigDecimal oemProfit;
    private BigDecimal firstDealerProfit;
    private BigDecimal secondDealerProfit;
    private BigDecimal directRecommendProfit;
    private BigDecimal indirectRecommendProfit;
    private BigDecimal baodanProfit;
}
