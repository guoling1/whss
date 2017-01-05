package com.jkm.hss.product.servcie;

import com.google.common.base.Optional;
import com.jkm.hss.product.entity.GlobalConfig;
import com.jkm.hss.product.entity.UpgradeRecommendRules;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/30.
 */
public interface GlobalConfigService {
    /**
     * 初始化
     * @param globalConfig
     */
    int insert(GlobalConfig globalConfig);

    /**
     * 修改
     * @param globalConfig
     * @return
     */
    int update(GlobalConfig globalConfig);

    /**
     * 根据编码查询
     * @param id
     * @return
     */
    Optional<GlobalConfig> selectById(long id);

    /**
     * 查询所有记录
     * @return
     */
    List<GlobalConfig> selectAll();
}
