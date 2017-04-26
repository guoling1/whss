package com.jkm.hss.product.servcie.impl;

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
        List<ProductChannelGateway> list = this.productChannelGatewayDao.selectByProductTypeAndGatewayAndProductId(enumProductType.getId(), enumGatewayType.getId(),productId);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (("").equals(list.get(i).getRecommend())){
                    list.get(i).setRecommend(0);
                }
            }
        }
        return list;
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

}
