package com.jkm.hss.product.servcie.impl;

import com.google.common.base.Optional;
import com.jkm.hss.product.dao.ProductChannelDetailDao;
import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-25.
 */
@Service
public class ProductChannelDetailServiceImpl implements ProductChannelDetailService {

    @Autowired
    private ProductChannelDetailDao productChannelDao;

    /**
     * {@inheritDoc}
     * @param productChannelDetail
     */
    @Override
    public void init(ProductChannelDetail productChannelDetail) {
        this.productChannelDao.init(productChannelDetail);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param channelSignId
     * @return
     */
    @Override
    public Optional<ProductChannelDetail> selectByProductIdAndChannelId(long id, int channelSignId) {
        return Optional.fromNullable(this.productChannelDao.selectByProductIdAndChannelId(id, channelSignId));
    }

    /**
     * {@inheritDoc}
     *
     * @param productId
     * @return
     */
    @Override
    public List<ProductChannelDetail> selectByProductId(long productId) {
        return this.productChannelDao.selectByProductId(productId);
    }

    /**
     * {@inheritDoc}
     *
     * @param payChannel
     * @return
     */
    @Override
    public List<ProductChannelDetail> selectByChannelTypeSign(int payChannel) {
        return this.productChannelDao.selectByChannelTypeSign(payChannel);
    }

    /**
     * {@inheritDoc}
     * @param detail
     */
    @Override
    public void update(ProductChannelDetail detail) {
        this.productChannelDao.update(detail);
    }
}
