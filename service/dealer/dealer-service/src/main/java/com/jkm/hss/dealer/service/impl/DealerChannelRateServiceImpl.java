package com.jkm.hss.dealer.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.dao.DealerChannelRateDao;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-25.
 */
@Service
public class DealerChannelRateServiceImpl implements DealerChannelRateService{

    @Autowired
    private DealerChannelRateDao dealerChannelRateDao;

    /**
     * {@inheritDoc}
     * @param dealerChannelRate
     */
    @Override
    public void init(DealerChannelRate dealerChannelRate) {
        this.dealerChannelRateDao.init(dealerChannelRate);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param productId
     * @return
     */
    @Override
    public List<DealerChannelRate> selectByDealerIdAndProductId(long id, long productId) {
        return this.dealerChannelRateDao.selectByDealerIdAndProductId(id, productId);
    }

    @Override
    public List<DealerChannelRate> selectByDealerId(long dealerId) {
        return this.dealerChannelRateDao.selectByDealerId(dealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @param payChannel
     * @return
     */
    @Override
    public List<DealerChannelRate> selectByDealerIdAndPayChannelSign(long dealerId, int payChannel) {
        return this.dealerChannelRateDao.selectByDealerIdAndPayChannelSign(dealerId, payChannel);
    }

    /**
     * 根据代理商id、产品id与通道标识查询代理商费率
     *
     * @param dealerId
     * @param productId
     * @param channelType
     * @return
     */
    @Override
    public Optional<DealerChannelRate> selectByDealerIdAndProductIdAndChannelType(long dealerId, long productId, int channelType) {
        return Optional.fromNullable(this.dealerChannelRateDao.selectByDealerIdAndProductIdAndChannelType(dealerId,productId,channelType));
    }
}
