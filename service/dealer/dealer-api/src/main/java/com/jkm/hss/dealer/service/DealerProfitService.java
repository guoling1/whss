package com.jkm.hss.dealer.service;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.entity.DealerProfit;
import com.jkm.hss.dealer.helper.response.DealerProfitSettingResponse;

import java.util.List;

/**
 * Created by xingliujie on 2017/5/8.
 */
public interface DealerProfitService {
    /**
     * 插入
     *
     * @param dealerProfit
     */
    void insert(DealerProfit dealerProfit);
    /**
     * 修改
     *
     * @param dealerProfit
     */
    void update(DealerProfit dealerProfit);

    /**
     * 根据代理商编码和产品编码查询代理桑合伙人推荐默认设置
     * @param dealerId
     * @param productId
     * @return
     */
    List<DealerProfitSettingResponse> selectDealerByDealerIdAndProductId(long dealerId, long productId);
    /**
     * 根据代理商编码和产品编码查询合伙人推荐默认设置
     * @param productId
     * @return
     */
    List<DealerProfitSettingResponse> selectByDealerIdAndProductId(long productId);

    /**
     * 查询某通道是否已经设置过分润空间
     * @param dealerId
     * @param productId
     * @param channelTypeSign
     * @return
     */
    Optional<DealerProfit> selectByDealerIdAndProductIdAndChannelTypeSign(long dealerId, long productId,Integer channelTypeSign);
}
