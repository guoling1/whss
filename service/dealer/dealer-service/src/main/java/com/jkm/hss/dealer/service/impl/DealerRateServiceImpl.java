package com.jkm.hss.dealer.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.dao.DealerChannelRateDao;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.service.DealerRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-24.
 */
@Service
public class DealerRateServiceImpl implements DealerRateService {

    @Autowired
    private DealerChannelRateDao dealerChannelRateDao;
    /**
     * {@inheritDoc}
     *
     * @param dealerChannelRate
     */
    @Override
    @Transactional
    public void init(DealerChannelRate dealerChannelRate) {
        this.dealerChannelRateDao.init(dealerChannelRate);
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerChannelRate
     * @return
     */
    @Override
    public int update(final DealerChannelRate dealerChannelRate) {
        return this.dealerChannelRateDao.update(dealerChannelRate);
    }


    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @param channelSignId
     * @return
     */
    @Override
    public List<DealerChannelRate> selectByDealerIdAndChannelId(long dealerId, int channelSignId) {
        return this.dealerChannelRateDao.selectByDealerIdAndChannelId(dealerId, channelSignId);
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @return
     */
    @Override
    public List<DealerChannelRate> getByDealerId(final long dealerId) {
        return this.dealerChannelRateDao.selectByDealerId(dealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @param productId
     * @param channelType
     * @return
     */
    @Override
    public Optional<DealerChannelRate> getByDealerIdAndProductIdAndChannelType(final long dealerId, final long productId, final int channelType) {
        return Optional.fromNullable(this.dealerChannelRateDao.selectByDealerIdAndProductIdAndChannelType(dealerId, productId, channelType));
    }
    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @param productId
     * @return
     */
    @Override
    public List<DealerChannelRate> getByDealerIdAndProductId(final long dealerId, final long productId) {
        return this.dealerChannelRateDao.selectByDealerIdAndProductId(dealerId, productId);
    }
}
