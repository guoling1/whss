package com.jkm.hss.product.servcie;

import com.google.common.base.Optional;
import com.jkm.hss.product.entity.ProductRatePolicy;

import java.util.List;


/**
 * Created by yuxiang on 2017-06-09.
 */
public interface ProductRatePolicyService {

    /**
     * 新增
     * @param productRatePolicy
     */
    void insert(ProductRatePolicy productRatePolicy);

    /**
     * 修改
     * @param productRatePolicy
     */
    void update(ProductRatePolicy productRatePolicy);


    /**
     * 查询
     * @param productId
     * @return
     */
    List<ProductRatePolicy> selectByProductId(long productId);

    /**
     * 根据代理类型查询代理商政策
     * @param policyType
     * @return
     */
    Optional<ProductRatePolicy> selectByPolicyType(String policyType);
}
