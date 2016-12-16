package com.jkm.hss.dealer.service;

import com.jkm.hss.dealer.entity.DealerChannelRate;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-25.
 */
public interface DealerChannelRateService {

    /**
     * 初始化
     * @param dealerChannelRate
     */
    void init(DealerChannelRate dealerChannelRate);

    /**
     * 查询代理商通道费率
     * @param id
     * @param productId
     * @return
     */
    List<DealerChannelRate> selectByDealerIdAndProductId(long id, long productId);

    /**
     * 根据代理商id查询其通道列表
     * @param dealerId
     * @return
     */
    List<DealerChannelRate> selectByDealerId(long dealerId);

    /**
     * 根据代理商id与通道标识查询代理商费率
     * @param dealerId
     * @param payChannel
     * @return
     */
    List<DealerChannelRate> selectByDealerIdAndPayChannelSign(long dealerId, int payChannel);
}
