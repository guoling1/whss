package com.jkm.hss.product.helper.response;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.product.enums.EnumBalanceTimeType;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductChannelDetailStatus;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ProductAndBasicResponse{
    /**
     * 产品编码
     */
    private long id;
    /**
     * 通道标识
     * {@link EnumPayChannelSign}
     */
    private int channelTypeSign;
    /**
     * 通道名称
     */
    private String channelShortName;
    /**
     * 支付方式
     */
    private String supportWay;
}
