package com.jkm.hss.dealer.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xingliujie on 2017/5/8.
 */
@Data
public class DealerProfit extends BaseEntity{
    /**
     * 代理商编码
     */
    private long dealerId;
    /**
     * 产品编码
     */
    private long productId;
    /**
     * 通道唯一标识...
     * {@link EnumPayChannelSign}
     */
    private Integer channelTypeSign;
    /**
     * 分润空间
     */
    private BigDecimal profitSpace;
}
