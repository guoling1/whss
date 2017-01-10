package com.jkm.hss.product.servcie.impl;

import com.google.common.base.Optional;
import com.jkm.hss.product.dao.UpgradeRecommendRulesDao;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.UpgradeRecommendRules;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.servcie.ProductService;
import com.jkm.hss.product.servcie.UpgradeRecommendRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Thinkpad on 2016/12/30.
 */
@Service
public class UpgradeRecommendRulesServiceImpl implements UpgradeRecommendRulesService{
    @Autowired
    private UpgradeRecommendRulesDao upgradeRecommendRulesDao;
    @Autowired
    private ProductService productService;
    /**
     * 初始化
     *
     * @param upgradeRecommendRules
     */
    @Override
    public int insert(UpgradeRecommendRules upgradeRecommendRules) {
        return upgradeRecommendRulesDao.insert(upgradeRecommendRules);
    }

    /**
     * 修改
     *
     * @param upgradeRecommendRules
     * @return
     */
    @Override
    public int update(UpgradeRecommendRules upgradeRecommendRules) {
        return upgradeRecommendRulesDao.update(upgradeRecommendRules);
    }

    /**
     * 根据编码查询
     *
     * @param id
     * @return
     */
    @Override
    public Optional<UpgradeRecommendRules> selectById(long id) {
        return Optional.fromNullable(upgradeRecommendRulesDao.selectById(id));
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    @Override
    public List<UpgradeRecommendRules> selectAll() {
        return upgradeRecommendRulesDao.selectAll();
    }

    /**
     * 根据产品编码查找记录
     *
     * @param productId
     * @return
     */
    @Override
    public Optional<UpgradeRecommendRules> selectByProductId(long productId) {
        return Optional.fromNullable(upgradeRecommendRulesDao.selectByProductId(productId));
    }

    /**
     * 查询达标标准
     *
     * @return
     */
    @Override
    public BigDecimal selectInviteStandard() {
        Optional<Product> productOptional = productService.selectByType(EnumProductType.HSS.getId());
        UpgradeRecommendRules  upgradeRecommendRules=  upgradeRecommendRulesDao.selectByProductId(productOptional.get().getId());
        return upgradeRecommendRules.getInviteStandard();
    }
}
