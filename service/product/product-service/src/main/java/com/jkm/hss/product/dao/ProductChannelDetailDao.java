package com.jkm.hss.product.dao;

import com.jkm.hss.product.entity.ProductChannelDetail;
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
}
