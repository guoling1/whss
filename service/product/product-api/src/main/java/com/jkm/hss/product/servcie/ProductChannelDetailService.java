package com.jkm.hss.product.servcie;

import com.google.common.base.Optional;
import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.helper.response.ProductAndBasicResponse;
import org.apache.ibatis.annotations.Param;

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

    /**
     *
     * @param detail
     */
    void updateOrAdd(ProductChannelDetail detail);

    /**
     * 根据通道查询
     * @param productId
     * @param channelTypeSign
     * @return
     */
    Optional<ProductChannelDetail> selectRateByProductIdAndChannelTypeSign(long productId,  int channelTypeSign);


    /**
     * 根据产品编码查询产品和基本通道列表
     * @param productId
     * @return
     */
    List<ProductAndBasicResponse> getProductChannelList(long productId);


}
