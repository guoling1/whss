package com.jkm.hss.product.dao;

import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.helper.response.ProductAndBasicResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-25.
 */
@Repository
public interface ProductChannelDetailDao {


    /**
     * 初始化
     */
    void init(ProductChannelDetail productChannelDetail);

    /**
     * 查询
     * @param id
     * @param channelSignId
     * @return
     */
    ProductChannelDetail selectByProductIdAndChannelId(@Param("id") long id, @Param("channelSignId") int channelSignId);

    /**
     * 查询
     * @param productId
     * @return
     */
    List<ProductChannelDetail> selectByProductId(@Param("productId") long productId);

    /**
     * 查询
     * @param payChannel
     * @return
     */
    List<ProductChannelDetail> selectByChannelTypeSign(@Param("payChannel") int payChannel);

    /**
     * 更新
     * @param detail
     */
    void update(ProductChannelDetail detail);

    /**
     * 查询某支付方式下的费率
     * @param productId
     * @param channelType
     * @return
     */
    ProductChannelDetail selectRateByProductIdAndChannelType(@Param("productId") long productId, @Param("channelType") int channelType);
    /**
     * 根据通道查询
     * @param productId
     * @param channelTypeSign
     * @return
     */
    ProductChannelDetail selectRateByProductIdAndChannelTypeSign(@Param("productId") long productId, @Param("channelTypeSign") int channelTypeSign);

    /**
     * 查询
     * @param productId
     * @return
     */
    List<ProductAndBasicResponse> getProductChannelList(@Param("productId") long productId);
}
