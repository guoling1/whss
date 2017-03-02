package com.jkm.hss.product.servcie;

import com.google.common.base.Optional;
import com.jkm.hss.product.entity.ProductChannelDetail;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-25.
 */
public interface ProductChannelDetailService {

    /**
     * 初始化
     */
    void init(ProductChannelDetail productChannelDetail);

    /**
     * 根据产品id和通道标识查询
     * @param productId
     * @param channelSignId
     * @return
     */
    Optional<ProductChannelDetail> selectByProductIdAndChannelId(long productId, int channelSignId);

    /**
     * 根据产品id查询 ？？？
     * @param productId
     * @return
     */
    List<ProductChannelDetail> selectByProductId(long productId);

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
    Optional<ProductChannelDetail> selectRateByProductIdAndChannelType(long productId,int channelType);
}
