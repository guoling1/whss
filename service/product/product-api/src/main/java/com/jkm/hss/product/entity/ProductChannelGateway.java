package com.jkm.hss.product.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by yuxiang on 2017-03-30.
 *
 * {@link com.jkm.hss.product.enums.EnumProductChannelGatewayStatus}
 */
@Data
public class ProductChannelGateway extends BaseEntity {

    /**
     * 网关类型，，
     *
     * {@link com.jkm.hss.product.enums.EnumGatewayType}
     */
    private String gatewayType;

    /**
     * 代理商网关模板id
     */
    private long dealerGatewayId;

    /**
     * 产品id
     */
    private long productId;

    /**
     * 产品类型 {@link com.jkm.hss.product.enums.EnumProductType}
     */
    private String productType;

    /**
     *通道标示
     */
    private int channelSign;

    /**
     * 展示名称
     */
    private String viewChannelName;

    /**
     * 通道简称(后台展示的名称)
     */
    private String channelShortName;

    /**
     *  分公司id ， 若为好收收 则 0
     */
    private long dealerId;

}
