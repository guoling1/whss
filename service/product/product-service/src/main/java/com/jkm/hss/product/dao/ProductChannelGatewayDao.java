package com.jkm.hss.product.dao;

import com.jkm.hss.product.entity.ProductChannelGateway;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuxiang on 2017-03-31.
 */
@Repository
public interface ProductChannelGatewayDao {


    void addNew(ProductChannelGateway productChannelGateway);

    List<ProductChannelGateway> selectByProductTypeAndGatewayAndProductId(@Param("type") String type, @Param("gatewayType") String gatewayType , @Param("productId") long productId);

    void update(ProductChannelGateway productChannelGateway);

    /**
     * 推荐通道
     * @param request
     */
    void recommend(ProductChannelGateway request);
}
