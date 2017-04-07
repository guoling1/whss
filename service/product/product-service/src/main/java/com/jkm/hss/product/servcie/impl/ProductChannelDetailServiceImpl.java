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
     * @param detail
     */
    @Override
    public void update(ProductChannelDetail detail) {
        this.productChannelDao.update(detail);
    }

    /**
     * 查询某支付方式下的费率
     *
     * @param productId
     * @param channelType
     * @return
     */
    @Override
    public Optional<ProductChannelDetail> selectRateByProductIdAndChannelType(long productId, int channelType) {
        return Optional.fromNullable(this.productChannelDao.selectRateByProductIdAndChannelType(productId,channelType));
    }

    /**
     * {@inheritDoc}
     *
     * @param detail
     */
    @Override
    public void updateOrAdd(ProductChannelDetail detail) {

        if (detail.getId() == 0){
            this.productChannelDao.init(detail);
        }
        this.productChannelDao.update(detail);
    }

    /**
     * 根据通道查询
     *
     * @param productId
     * @param channelTypeSign
     * @return
     */
    @Override
    public Optional<ProductChannelDetail> selectRateByProductIdAndChannelTypeSign(long productId, int channelTypeSign) {
        return Optional.fromNullable(this.productChannelDao.selectRateByProductIdAndChannelTypeSign(productId,channelTypeSign));
    }
}
