package com.jkm.hss.product.servcie.impl;

import com.google.common.base.Optional;
import com.jkm.hss.product.dao.ProductDao;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.servcie.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-23.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;
    /**
     * {@inheritDoc}
     *
     * @param product
     */
    @Override
    public void init(Product product) {
        this.productDao.init(product);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Product> selectById(long id) {
        return Optional.fromNullable(this.productDao.selectById(id));
    }

    @Override
    public List<Product> selectAll() {
        return this.productDao.selectAll();
    }

    /**
     * 更新产品
     * @param product
     */
    @Override
    public void update(Product product) {
        this.productDao.update(product);
    }

    /**
     * 根据id查询
     *
     * @param type
     * @return
     */
    @Override
    public Optional<Product> selectByType(String type) {
        return Optional.fromNullable(this.productDao.selectByType(type));
    }
}
