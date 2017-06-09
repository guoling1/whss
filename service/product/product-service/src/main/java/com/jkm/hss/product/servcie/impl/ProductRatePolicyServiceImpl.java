package com.jkm.hss.product.servcie.impl;

import com.google.common.base.Optional;
import com.jkm.hss.product.dao.ProductRatePolicyDao;
import com.jkm.hss.product.entity.ProductRatePolicy;
import com.jkm.hss.product.enums.EnumPolicyType;
import com.jkm.hss.product.servcie.ProductRatePolicyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yuxiang on 2017-06-09.
 */
@Slf4j
@Service
public class ProductRatePolicyServiceImpl implements ProductRatePolicyService {


    @Autowired
    private ProductRatePolicyDao productRatePolicyDao;
    /**
     * {@inheritDoc}
     * @param productRatePolicy
     */
    @Override
    public void insert(ProductRatePolicy productRatePolicy) {
        this.productRatePolicyDao.insert(productRatePolicy);
    }

    /**
     * {@inheritDoc}
     * @param productRatePolicy
     */
    @Override
    public void update(ProductRatePolicy productRatePolicy) {
        this.productRatePolicyDao.update(productRatePolicy);
    }

    /**
     * {@inheritDoc}
     * @param productId
     * @return
     */
    @Override
    public List<ProductRatePolicy> selectByProductId(long productId) {
        final List<ProductRatePolicy> list = this.productRatePolicyDao.selectByProductId(productId);
        for (ProductRatePolicy productRatePolicy : list){
            if (productRatePolicy.getPolicyType().equals(EnumPolicyType.WITHDRAW)){
                productRatePolicy.setProductTradeRateT1(productRatePolicy.getProductTradeRateT1().setScale(2));
                productRatePolicy.setMerchantMaxRateT1(productRatePolicy.getMerchantMaxRateT1().setScale(2));
                productRatePolicy.setMerchantMinRateT1(productRatePolicy.getMerchantMinRateT1().setScale(2));
                productRatePolicy.setProductTradeRateD1(productRatePolicy.getProductTradeRateD1().setScale(2));
                productRatePolicy.setMerchantMaxRateD1(productRatePolicy.getMerchantMaxRateD1().setScale(2));
                productRatePolicy.setMerchantMinRateD1(productRatePolicy.getMerchantMinRateD1().setScale(2));
                productRatePolicy.setProductTradeRateD0(productRatePolicy.getProductTradeRateD0().setScale(2));
                productRatePolicy.setMerchantMaxRateD0(productRatePolicy.getMerchantMaxRateD0().setScale(2));
                productRatePolicy.setMerchantMinRateD0(productRatePolicy.getMerchantMinRateD0().setScale(2));
            }
        }

        return list;
    }

    /**
     * 根据代理类型查询代理商政策
     *
     * @param policyType
     * @return
     */
    @Override
    public Optional<ProductRatePolicy> selectByPolicyType(String policyType) {
        return Optional.fromNullable(productRatePolicyDao.selectByPolicyType(policyType));
    }
}
