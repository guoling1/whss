package com.jkm.hss.product.servcie.impl;

import com.google.common.base.Optional;
import com.jkm.hss.product.dao.ProductChannelGatewayDao;
import com.jkm.hss.product.entity.ProductChannelGateway;
import com.jkm.hss.product.enums.EnumGatewayType;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.servcie.ProductChannelGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yuxiang on 2017-03-31.
 */
@Service
public class ProductChannelGatewayServiceImpl implements ProductChannelGatewayService {

    @Autowired
    private ProductChannelGatewayDao productChannelGatewayDao;


    /**
     * {@inheritDoc}
     * @param productChannelGateway
     */
    @Override
    public void addNew(ProductChannelGateway productChannelGateway) {

        this.productChannelGatewayDao.addNew(productChannelGateway);
    }

    /**
     * {@inheritDoc}
     *
     * @param enumProductType
     * @return
     */
    @Override
    public List<ProductChannelGateway>  selectByProductTypeAndGatewayAndProductId(EnumProductType enumProductType, EnumGatewayType enumGatewayType, long productId) {
        return this.productChannelGatewayDao.selectByProductTypeAndGatewayAndProductId(enumProductType.getId(), enumGatewayType.getId(),productId);
    }

    /**
     * {@inheritDoc}
     *
     * @param productChannelGateway
     */
    @Override
    public void update(ProductChannelGateway productChannelGateway) {

        this.productChannelGatewayDao.update(productChannelGateway);
    }

}
