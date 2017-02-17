package com.jkm.hss.dealer.service;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.entity.DealerChannelRate;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-24.
 */
public interface DealerRateService {

    /**
     * 初始化
     */
    void init(DealerChannelRate dealerChannelRate);

    /**
     * 更新
     *
     * @param dealerChannelRate
     */
    int update(DealerChannelRate dealerChannelRate);

    /**
     * 根据
     * @param dealerId
     * @param channelSignId
     * @return
     */
    List<DealerChannelRate> selectByDealerIdAndChannelId(long dealerId, int channelSignId);

    /**
     * 根据代理商id 查询所有
     *
     * @param dealerId
     * @return
     */
    List<DealerChannelRate> getByDealerId(long dealerId);

    /**
     * 按代理商id, 产品id, 通道类型查询
     *
     * @param dealerId
     * @param productId
     * @param channelType
     * @return
     */
    Optional<DealerChannelRate> getByDealerIdAndProductIdAndChannelType(long dealerId, long productId, int channelType);
    /**
     * 按代理商id, 产品id
     *
     * @param dealerId
     * @param productId
     * @return
     */
    List<DealerChannelRate> getByDealerIdAndProductId(long dealerId, long productId);
    /**
     * 二级代理修改产品费率
     * @param dealerChannelRate
     */
    void updateSecondDealerRate(DealerChannelRate dealerChannelRate);

}
