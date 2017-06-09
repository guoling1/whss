package com.jkm.hss.product.dao;

import com.jkm.hss.product.entity.ProductRatePolicy;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuxiang on 2017-06-09.
 */
@Repository
public interface ProductRatePolicyDao {

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
}
