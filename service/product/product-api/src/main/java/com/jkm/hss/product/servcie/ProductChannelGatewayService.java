package com.jkm.hss.product.servcie;

import com.google.common.base.Optional;
import com.jkm.hss.product.entity.ProductChannelGateway;
import com.jkm.hss.product.enums.EnumProductType;

import java.util.List;

/**
 * Created by yuxiang on 2017-03-31.
 */
public interface ProductChannelGatewayService {

    /**
     * 新增产品通道网关
     * @param productChannelGateway
     */
    void addNew(ProductChannelGateway productChannelGateway);

    /**
     * 根据产品类型查询 产品通道网关
     *
     * @param enumProductType
     * @return
     */
    List<ProductChannelGateway>  selectByProductTypeAndProductId(EnumProductType enumProductType, long productId);

    /**
     * 修改
     *
     * @param productChannelGateway
     */
    void update(ProductChannelGateway productChannelGateway);

    /**
     * 查询
     * @param channelTypeSign
     * @return
     */
    ProductChannelGateway selectByChannelSign(int channelTypeSign);
}
