package com.jkm.hss.product.dao;

import com.jkm.hss.product.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-23.
 */
@Repository
public interface ProductDao {
    /**
     * 初始化
     */
    void init(Product product);

    /**
     *
     * @param id
     * @return
     */
    Product selectById(@Param("id") long id);

    /**
     * 查询
     * @return
     */
    List<Product> selectAll();

    /**
     * 更新
     * @param product
     */
    void update(Product product);
}
