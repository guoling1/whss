package com.jkm.hss.product.servcie;

import com.google.common.base.Optional;
import com.jkm.hss.product.entity.UpgradeRecommendRules;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Thinkpad on 2016/12/30.
 */
public interface UpgradeRecommendRulesService {
    /**
     * 初始化
     * @param upgradeRecommendRules
     */
    int insert(UpgradeRecommendRules upgradeRecommendRules);

    /**
     * 修改
     * @param upgradeRecommendRules
     * @return
     */
    int update(UpgradeRecommendRules upgradeRecommendRules);

    /**
     * 根据编码查询
     * @param id
     * @return
     */
    Optional<UpgradeRecommendRules> selectById(long id);

    /**
     * 查询所有记录
     * @return
     */
    List<UpgradeRecommendRules> selectAll();

    /**
     * 根据产品编码查找记录
     * @param productId
     * @return
     */
    Optional<UpgradeRecommendRules> selectByProductId(long productId);

    /**
     * 查询达标标准
     * @return
     */
    BigDecimal selectInviteStandard();

}
