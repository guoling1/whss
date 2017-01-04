package com.jkm.hss.product.dao;

import com.jkm.hss.product.entity.UpgradeRecommendRules;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/30.
 */
@Repository
public interface UpgradeRecommendRulesDao {
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
    UpgradeRecommendRules selectById(@Param("id") long id);

    /**
     * 查询所有记录
     * @return
     */
    List<UpgradeRecommendRules> selectAll();

    /**
     * 根据产品编码查找记录
     * @return
     */
    UpgradeRecommendRules selectByProductId(@Param("productId") long productId);


}
