package com.jkm.hss.dealer.service;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import org.apache.commons.lang3.tuple.Triple;

import java.math.BigDecimal;
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

    /**
     * 根据代理商id、产品id与通道标识查询代理商费率
     * @param dealerId
     * @param productId
     * @param channelType
     * @return
     */
    Optional<DealerChannelRate> selectByDealerIdAndProductIdAndChannelType(long dealerId, long productId, int channelType);

    /**
     * 获取app商户费率
     * @param dealerId
     * @return
     */
    Triple<BigDecimal, BigDecimal, BigDecimal> getMerchantRateByDealerId(long dealerId, long productId);

    /**
     * 查询代理商绑定的产品编码
     * @param dealerId
     * @param sysType
     * @return
     */
    Long getDealerBindProductId(long dealerId, String sysType);
}
