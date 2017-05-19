package com.jkm.hss.product.servcie;

import com.jkm.hss.product.entity.ProductChannelGateway;
import com.jkm.hss.product.enums.EnumGatewayType;
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
    List<ProductChannelGateway>  selectByProductTypeAndGatewayAndProductIdAndDealerId(EnumProductType enumProductType,
                                                                           EnumGatewayType enumGatewayType,long productId,long dealerId);

    /**
     * 修改
     *
     * @param productChannelGateway
     */
    void update(ProductChannelGateway productChannelGateway);

    /**
     * 推荐通道
     * @param request
     */
    void recommend(ProductChannelGateway request);
    /**
     * 初始化分公司网关通道
     * @param dealerId
     */
    void initOemGateway(long dealerId);

    /**
     * 删除网关
     * @param id
     */
    void deleteGateway(long id);
}
