package com.jkm.hss.dealer.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Thinkpad on 2016/12/29.
 * 推荐人规则分润
 * tb_dealer_upgerde_rate
 */
@Data
public class DealerUpgerdeRate extends BaseEntity {
    /**
     * 产品编码
     */
    private long productId;
    /**
     * 分润类型
     */
    private int type;
    /**
     *代理商编码
     */
    private long dealerId;

    /**
     *一级代理商分润比例
     */
    private BigDecimal firstDealerShareProfitRate;

    /**
     *二级代理商分润比例
     */
    private BigDecimal secondDealerShareProfitRate;

    /**
     *金开门分润比例
     */
    private BigDecimal bossDealerShareRate;
}
