package com.jkm.hss.product.servcie.impl;

import com.jkm.hss.product.dao.ProductChannelGatewayDao;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.ProductChannelGateway;
import com.jkm.hss.product.enums.EnumGatewayType;
import com.jkm.hss.product.enums.EnumProductChannelGatewayStatus;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.servcie.ProductChannelGatewayService;
import com.jkm.hss.product.servcie.ProductService;
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
    @Autowired
    private ProductService productService;
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
    public List<ProductChannelGateway>  selectByProductTypeAndGatewayAndProductIdAndDealerId(EnumProductType enumProductType, EnumGatewayType enumGatewayType, long productId, long dealerId) {
        return this.productChannelGatewayDao.selectByProductTypeAndGatewayAndProductIdAndDealerId(enumProductType.getId(), enumGatewayType.getId(),productId,dealerId);
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

    @Override
    public void recommend(ProductChannelGateway request) {
        this.productChannelGatewayDao.recommend(request);
    }

    /**
     * {@inheritDoc}
     * @param dealerId
     */
    @Override
    public void initOemGateway(long dealerId) {

        final Product product = this.productService.selectByType(EnumProductType.HSS.getId()).get();
        final List<ProductChannelGateway> list = this.productChannelGatewayDao.selectByProductTypeAndGatewayAndProductIdAndDealerId(EnumProductType.HSS.getId(),
                EnumGatewayType.PRODUCT.getId(), product.getId(), dealerId);
        for (ProductChannelGateway gateway : list){
            gateway.setDealerId(dealerId);
            this.productChannelGatewayDao.addNew(gateway);
        }
    }

    /**
     * {@inheritDoc}
     * @param id
     */
    @Override
    public void deleteGateway(long id) {
        this.productChannelGatewayDao.deleteGateway(id, EnumProductChannelGatewayStatus.DELETE.getId());
    }

}
