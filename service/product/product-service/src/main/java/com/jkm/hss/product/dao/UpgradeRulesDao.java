package com.jkm.hss.product.dao;

import com.google.common.base.Optional;
import com.jkm.hss.product.entity.UpgradeRules;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/29.
 */
@Repository
public interface UpgradeRulesDao {
    /**
     * 初始化
     * @param upgradeRules
     */
    int insert(UpgradeRules upgradeRules);

    /**
     * 修改
     * @param upgradeRules
     * @return
     */
    int update(UpgradeRules upgradeRules);

    /**
     * 根据编码查询
     * @param id
     * @return
     */
    UpgradeRules selectById(@Param("id") long id);

    /**
     * 查询所有记录
     * @return
     */
    List<UpgradeRules> selectAll();

    /**
     * 根据产品编码查找当前编码下的记录
     * @param productId
     * @return
     */
    List<UpgradeRules> selectByProductId(@Param("productId") long productId);

    /**
     * 根据产品编码和类型查询
     * @param productId
     * @param type
     * @return
     */
    UpgradeRules selectByProductIdAndType(@Param("productId") long productId, @Param("type") long type);
}
