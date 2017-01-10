package com.jkm.hss.product.servcie;

import com.google.common.base.Optional;
import com.jkm.hss.product.entity.Product;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-23.
 */
public interface ProductService {

    /**
     * 初始化产品
     * @param product
     */
    void init(Product product);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Optional<Product> selectById(long id);

    /**
     * 查询产品
     * @return
     */
    List<Product> selectAll();

    /**
     * 更新
     * @param product
     */
    void update(Product product);

    /**
     * 根据id查询
     * @param type
     * @return
     */
    Optional<Product> selectByType(String type);
}
