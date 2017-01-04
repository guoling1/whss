package com.jkm.hss.product.servcie;

import com.google.common.base.Optional;
import com.jkm.hss.product.entity.UpgradeResult;
import com.jkm.hss.product.entity.UpgradeRules;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/29.
 */
public interface UpgradeRulesService {
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
    Optional<UpgradeRules> selectById(long id);

    /**
     * 查询所有记录
     * @return
     */
    List<UpgradeRules> selectAll(long productId);

    /**
     * 查询所有记录
     * @return
     */
    List<UpgradeResult> selectUpgradeList(long productId,int level);

    /**
     * 根据产品编码和类型查询
     * @param productId
     * @param type
     * @return
     */
    Optional<UpgradeRules> selectByProductIdAndType(long productId,int type);

}
